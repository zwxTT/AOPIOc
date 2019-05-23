package name.zwx.aop_ioc.AOP;

import java.lang.reflect.Method;

public class AdviceHander extends AdviceAdapter {
    IntercepterLoader intercepterLoder;
    public AdviceHander() {
    }

    public AdviceHander setIntercepterLoder(IntercepterLoader intercepterLoder) {
        this.intercepterLoder = intercepterLoder;
        return this;
    }

    /**
     * 通过targetMethod在intercepterLoder中获取拦截器链，
     * 调用NodeListMethodChain类中的执行拦截的方法
     * @param targetMethod
     * @param args 目标方法的参数
     * @return
     */
    @Override
    public boolean dealBefore(Method targetMethod, Object[] args) {
        IntercepterLink methodChain = intercepterLoder.getBeforeIntercepterList(targetMethod);
        if (methodChain == null) {
            return true;
        }
        return methodChain.dealBefore(args);
    }

    @Override
    public Object dealAfter(Method targetMethod, Object result) {
        IntercepterLink methodChain = intercepterLoder.getAfterIntercepterList(targetMethod);
        if (methodChain == null) {
            return result;
        }
        return methodChain.dealAfter(result);
    }

    @Override
    public void dealException(Method targetMethod, Throwable e) {
        IntercepterLink methodChain = intercepterLoder.getAfterIntercepterList(targetMethod);
        if (methodChain == null) {
            return;
        }
        methodChain.dealException(e);
    }
}
