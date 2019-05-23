package name.zwx.aop_ioc.AOP;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 拦截器
 */
public class AopFactory {
    Advice advice;

    public AopFactory() {
    }

    public void setAdvice(Advice advice) {
        this.advice = advice;
    }

    //放在这里以示警告，不能用类产生代理，这样会产生新的对象
//    protected <E> E getCGLProxy(Class<?> klass) throws IllegalAccessException, InstantiationException {
//        return getCGLProxy(klass, klass.newInstance());
//    }


    public <E> E getCGLProxy(Object object) {
        return getCGLProxy(object.getClass(), object);
    }

    /**
     * 产生代理
     * @param clazz
     * @param obj
     * @param <E>
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <E> E getCGLProxy(Class<?> clazz, Object obj) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object object, Method method, Object[] args,
                                    MethodProxy methodProxy) throws Throwable {
                Object result = getResult(obj, method, args);
                return result;
            }
        });

        return (E) enhancer.create();
    }
    private Object getResult(Object object, Method method, Object[] args) {
        advice = advice == null ? new AdviceAdapter() : advice;
        Object result = null;
        // 置前拦截
        if (advice.dealBefore(method, args)) {
            try {
                result = method.invoke(object, args);
                // 置后拦截
                advice.dealAfter(method, result);
            } catch (Throwable e) {
                e.printStackTrace();
                // 异常拦截
                advice.dealException(method, e);
            }
        }
        return result;
    }
}
