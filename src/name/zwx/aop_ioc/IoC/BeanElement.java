package name.zwx.aop_ioc.IoC;

public interface BeanElement {
    Object getObject();
    <T> T getProxy();
    boolean isDI();
}
