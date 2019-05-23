package name.zwx.aop_ioc.AOP;

public interface IntercepterLink {
    boolean add(IntercepterMethodDefinition imd);
    boolean dealBefore(Object[] args);
    Object dealAfter(Object result);
    void dealException(Throwable e);
}
