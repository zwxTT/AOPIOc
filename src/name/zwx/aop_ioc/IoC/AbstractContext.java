package name.zwx.aop_ioc.IoC;

import name.zwx.aop_ioc.annotation.Bean;
import name.zwx.aop_ioc.annotation.Component;
import name.zwx.aop_ioc.annotation.Value;
import name.zwx.aop_ioc.exception.FileNotWiredException;
import name.zwx.aop_ioc.exception.ParameterNotMatchException;
import name.zwx.aop_ioc.exception.ReturnTypeNotMathException;
import name.zwx.util.PackageScanner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractContext extends AbstractApplicationContext {
    private List<UndefinitionMethodBuffer> methodBuffer;
    public AbstractContext() {
    }
    public AbstractContext(String packageName) throws FileNotWiredException, ParameterNotMatchException {
        scanPackage(packageName);
    }

    /**
     * 添加入map
     * @param klass
     * @param object
     * @param beanId
     */
    private void addBean(Class<?> klass, Object object, String beanId) {
        try {
            Object proxy = null;
            //不能使用类或的代理，使用类获得的注解又重新实例化了一次类，
            // 存入map中的对象并不是原来产生的对象，而是再次通过newInstance出来的对象
            proxy = aopFactory.getCGLProxy(object);
            AnnotationBean annotationBean = new AnnotationBean(object, proxy);
            addBeanMap(klass, annotationBean, beanId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 扫描包
     * @param packageName
     * @throws FileNotWiredException
     * @throws ParameterNotMatchException
     */
    protected void scanPackage(String packageName) throws FileNotWiredException, ParameterNotMatchException {
        new PackageScanner() {
            @Override
            public void dealClass(Class<?> klass) {
                if (!klass.isAnnotationPresent(Component.class)) {
                    return;
                }
                try {
                    Component component = klass.getAnnotation(Component.class);
                    String beanId = component.beanId();
                    Object object = klass.newInstance();
                    addBean(klass, object, beanId);
                    dealMethod(klass, object);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }.packageScan(packageName);
        for (UndefinitionMethodBuffer undefinitionMethod : methodBuffer) {
            dealUndefinitionMethodBuffer(undefinitionMethod);
        }
    }

    /**
     * 处理带参数的方法
     * @param undefinitionMethod
     * @throws FileNotWiredException
     * @throws ParameterNotMatchException
     */
    private void dealUndefinitionMethodBuffer(UndefinitionMethodBuffer undefinitionMethod) throws FileNotWiredException, ParameterNotMatchException {
        Object object = undefinitionMethod.getObject();
        Class<?> returnType = undefinitionMethod.getReturnType();
        String beanId = undefinitionMethod.getBeanId();
        Parameter[] parameters = undefinitionMethod.getParameters();
        Method method = undefinitionMethod.getMethod();
        Object[] paras = dealParameter(parameters);
        try {
            method.setAccessible(true);
            Object result = method.invoke(object, paras);
            addBean(returnType, result, beanId);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理参数
     * @param parameters
     * @return
     * @throws FileNotWiredException
     * @throws ParameterNotMatchException
     */
    private Object[] dealParameter(Parameter[] parameters) throws FileNotWiredException, ParameterNotMatchException {
        int count = parameters.length;
        Object[] result = new Object[count];
        for (int i = 0; i < count; i++) {
            Parameter parameter = parameters[i];
            Class<?> paraType = parameter.getType();
            if (parameter.isAnnotationPresent(Value.class)) {
                if (!(paraType.isEnum() || paraType.equals(String.class))) {
                    try {
                        throw new ParameterNotMatchException("参数[" + paraType.getName() + "]的类型不正确");
                    } catch (ParameterNotMatchException e) {
                        e.printStackTrace();
                    }
                }
                String value = parameter.getAnnotation(Value.class).value();
                result[i] = TypeConversation.getTypeValue(value, paraType.getSimpleName());
            } else {
                result[i] = getProxy(paraType.getName());
            }
        }
        return result;
    }

    /**
     * 处理方法
     * @param klass
     * @param object
     */
    private void dealMethod(Class<?> klass, Object object) {
        Method[] methods = klass.getDeclaredMethods();
        for (Method method : methods) {
            if (!method.isAnnotationPresent(Bean.class)) {
                continue;
            }
            Class<?> returnType = method.getReturnType();
            String beanId = method.getAnnotation(Bean.class).bean();
            if (method.getReturnType().equals(void.class)) {
                try {
                    throw new ReturnTypeNotMathException(method.getName() + "[" +"返回值类型不能为void"+ "]");
                } catch (ReturnTypeNotMathException e) {
                    e.printStackTrace();
                }
            }
            Parameter[] paramethers = method.getParameters();
            //处理没有参数的带Bean注解的方法
            if (paramethers.length <= 0) {
                try {
                    method.setAccessible(true);
                    Object result = method.invoke(object);
                    addBean(klass, result, beanId);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                //将带参数的方法存入buffer中
                methodBuffer = methodBuffer == null ? new ArrayList<>() : methodBuffer;
                UndefinitionMethodBuffer UndefinitionMehtod = new UndefinitionMethodBuffer(object,
                        beanId, method, paramethers, returnType);
                methodBuffer.add(UndefinitionMehtod);
            }
        }
    }
}
