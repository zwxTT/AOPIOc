package name.zwx.aop_ioc.AOP;

import java.lang.reflect.Method;

public class AdviceAdapter implements Advice {

    @Override
    public boolean dealBefore(Method method, Object[] arg) {
        return true;
    }

    @Override
    public Object dealAfter(Method method, Object result) {
        return result;
    }

    @Override
    public void dealException(Method method, Throwable e) {

    }
}
