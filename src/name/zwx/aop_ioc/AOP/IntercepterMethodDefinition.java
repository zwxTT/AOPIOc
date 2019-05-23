package name.zwx.aop_ioc.AOP;

import java.lang.reflect.Method;

/**
 * 拦截方法的定义
 */
public class IntercepterMethodDefinition {
    private Method method;
    private Object object;

    public IntercepterMethodDefinition(Method method, Object object) {
        this.method = method;
        this.object = object;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
