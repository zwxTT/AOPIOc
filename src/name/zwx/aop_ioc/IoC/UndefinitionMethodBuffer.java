package name.zwx.aop_ioc.IoC;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class UndefinitionMethodBuffer {
    private Object object;
    private String beanId;
    private Method method;
    private Parameter[] parameters;
    private Class<?> returnType;

    public UndefinitionMethodBuffer(Object object, String beanId,
                                    Method method, Parameter[] parameters,
                                    Class<?> returnType) {
        this.object = object;
        this.beanId = beanId;
        this.method = method;
        this.parameters = parameters;
        this.returnType = returnType;
    }

    public Object getObject() {
        return object;
    }

    public void setObkject(Object object) {
        this.object = object;
    }

    public String getBeanId() {
        return beanId;
    }

    public void setBeanId(String beanId) {
        this.beanId = beanId;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Parameter[] getParameters() {
        return parameters;
    }

    public void setParameters(Parameter[] parameters) {
        this.parameters = parameters;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }
}
