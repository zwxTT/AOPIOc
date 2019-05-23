package name.zwx.aop_ioc.IoC;

/**
 * 代理以及原对象
 */
public class AnnotationBean implements BeanElement {
    private Object object;
    private Object proxy;
    private boolean DI;

    public AnnotationBean(Object object, Object proxy, boolean DI) {
        this.object = object;
        this.proxy = proxy;
        this.DI = DI;
    }

    public AnnotationBean(Object object, Object proxy) {
        this(object, proxy, false);
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public void setProxy(Object proxy) {
        this.proxy = proxy;
    }

    public void setDI(boolean DI) {
        this.DI = DI;
    }

    @Override
    public Object getObject() {
        return object;
    }

    @SuppressWarnings(value="unchecked")
    @Override
    public <T> T getProxy() {
        return (T) proxy;
    }

    @Override
    public boolean isDI() {
        return DI;
    }
}
