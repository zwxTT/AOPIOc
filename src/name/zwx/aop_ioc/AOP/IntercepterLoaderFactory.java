package name.zwx.aop_ioc.AOP;

import name.zwx.aop_ioc.annotation.After;
import name.zwx.aop_ioc.annotation.Aspect;
import name.zwx.aop_ioc.annotation.Before;
import name.zwx.aop_ioc.annotation.Throwable;
import name.zwx.aop_ioc.exception.BadReturnTypeException;
import name.zwx.aop_ioc.exception.IntercepterMethodParasTypeNotMatchException;
import name.zwx.util.PackageScanner;

import java.lang.reflect.Method;

public class IntercepterLoaderFactory extends IntercepterLoader {

    public IntercepterLoaderFactory() {
    }

    /**
     * 扫描带有Aspect注解的类
     * @param packageName
     */
    public void scanMethodForPackage(String packageName) {
        new PackageScanner() {
            @Override
            public void dealClass(Class<?> klass) {
                if (!klass.isAnnotationPresent(Aspect.class)) {
                    return;
                }
                Object object = null;
                try {
                    object = klass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Method[] methods = klass.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(Before.class)) {
                        Before before = method.getAnnotation(Before.class);
                        parseBeforeIntercepter(method, klass, before, object);
                    } else if (method.isAnnotationPresent(After.class)) {
                        After after = method.getAnnotation(After.class);
                        parseAfterIntercepter(method, klass, after, object);
                    } else if (method.isAnnotationPresent(Throwable.class)) {
                        Throwable throwable = method.getAnnotation(Throwable.class);
                        parseThrowableIntercepter(method, klass, throwable, object);
                    } else {
                        continue;
                    }
                }
            }
        }.packageScan(packageName);
    }

    /**
     * 添加置前拦截，若拦截该方法的执行，返回值为false，否则返回true
     * @param method
     * @param klass
     * @param before
     * @param object
     */
    private void parseBeforeIntercepter(Method method, Class<?> klass,
                                        Before before, Object object) {
        Class<?> methodReturnType = method.getReturnType();
        if (!methodReturnType.equals(boolean.class)) {
            try {
                throw new BadReturnTypeException("拦截方法[" + method.getName() + "]的返回值类型应为boolean");
            } catch (BadReturnTypeException e) {
                e.printStackTrace();
            }
        }
        Class<?> targetClass = before.klass();
        String targetMethodName = before.method();
        Class<?>[] paras = method.getParameterTypes();
        try {
            Method targetMethod = targetClass.getDeclaredMethod(targetMethodName, paras);
            addBeforeIntercepter(targetMethod, new IntercepterMethodDefinition(method, object));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加置后拦截
     * @param method 拦截方法
     * @param klass 拦截类
     * @param after
     * @param object 拦截对象
     */
    private void parseAfterIntercepter(Method method, Class<?> klass,
                                       After after, Object object) {
        Class<?> targetClass = after.klass();
        String targetMethodName = after.method();
        Class<?>[] paras = after.parameters();
        try {
            Method targetMethod = targetClass.getDeclaredMethod(targetMethodName, paras);
            Class<?> targetReturnType = targetMethod.getReturnType();
            Class<?> returnType = method.getReturnType();
            Class<?>[] parasType = method.getParameterTypes();
            if (parasType.length != 1 || !returnType.equals(targetReturnType)
                    || !targetReturnType.equals(parasType[0])) {
                throw new IntercepterMethodParasTypeNotMatchException("方法["
                        + method.getName() + "]的参数类型不正确");
            }
            addAfterIntercepter(targetMethod, new IntercepterMethodDefinition(method, object));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加置后拦截
     * @param method
     * @param klass
     * @param throwable
     * @param object
     */
    private void parseThrowableIntercepter(Method method,
                                           Class<?> klass, Throwable throwable, Object object) {
        Class<?> targetClass = throwable.klass();
        String targetMethodName = throwable.method();
        Class<?>[] paras = throwable.parameters();
        Class<?>[] methodParas = method.getParameterTypes();
        try {
            Method targetMethod = targetClass.getDeclaredMethod(targetMethodName, paras);
            if (methodParas.length != 1 || !methodParas[0].equals(java.lang.Throwable.class)) {
                throw new IntercepterMethodParasTypeNotMatchException
                        ("异常处理方法[" + method.getName() + "]参数不正确");
            }
            addExceptionIntercepter(targetMethod, new IntercepterMethodDefinition(method, object));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
