package name.zwx.aop_ioc.AOP;

public class NodeListAdapter implements IntercepterLink {

    @Override
    public boolean add(IntercepterMethodDefinition imd) {
        return false;
    }

    @Override
    public boolean dealBefore(Object[] args) {
        return false;
    }

    @Override
    public Object dealAfter(Object result) {
        return null;
    }

    @Override
    public void dealException(Throwable e) {
    }
}
