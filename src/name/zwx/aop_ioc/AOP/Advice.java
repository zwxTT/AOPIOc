package name.zwx.aop_ioc.AOP;

import java.lang.reflect.Method;

public interface Advice {
    boolean dealBefore(Method method, Object[] arg);
    Object dealAfter(Method method, Object result);
    void dealException(Method method, Throwable e);
}
