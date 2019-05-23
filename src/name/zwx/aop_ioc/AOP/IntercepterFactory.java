package name.zwx.aop_ioc.AOP;

import java.lang.reflect.Method;

public interface IntercepterFactory {
    void addBeforeIntercepter(Method method, IntercepterMethodDefinition methodDefinition);
    void addAfterIntercepter(Method method, IntercepterMethodDefinition methodDefinition);
    void addExceptionIntercepter(Method method, IntercepterMethodDefinition methodDefinition);

    IntercepterLink getBeforeIntercepterList(Method method);
    IntercepterLink getAfterIntercepterList(Method method);
    IntercepterLink getExcepterIntercepterList(Method method);
}
