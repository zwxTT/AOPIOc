package name.zwx.aop_ioc.AOP;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 拦截器的具体执行
 */
public class NodeListMethodChain extends NodeListAdapter {
    private IntercepterMethodDefinition imd;
    private NodeListMethodChain next;
    private NodeListMethodChain last;

    public NodeListMethodChain() {
        this(null);
    }

    public NodeListMethodChain(IntercepterMethodDefinition imd) {
        this.imd = imd;
        next = null;
        last = this;
    }

    /**
     * 尾加拦截器
     * @param imd 拦截方法和拦截方法的对象
     * @return
     */
    @Override
    public boolean add(IntercepterMethodDefinition imd) {
        if (next == null) {
            next = new NodeListMethodChain(imd);
            last = next;
        } else {
            last = last.next = new NodeListMethodChain(imd);
            last.next = null;
        }
        return true;
    }

    /**
     * 执行置前拦截器方法
     * 从根节点开始执行
     * @param args
     * @return
     */
    @Override
    public boolean dealBefore(Object[] args) {
        boolean result = false;
        Method method = imd.getMethod();
        Object object = imd.getObject();
        result = innerInvoker(object, method, args);
        if (result && this.next != null) {
            result = this.next.dealBefore(args);
        }

        return result;
    }
    @SuppressWarnings("unchecked")
    private <E> E innerInvoker(Object object, Method method, Object[] args) {
        E result = null;
        try {
            result = (E) method.invoke(object, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return  result;
    }

    /**
     * 执行置后拦截器方法
     * @param result 执行的目标拦截方法的返回值
     * @return
     */
    @Override
    public Object dealAfter(Object result) {
        result = innerInvoker(imd.getObject(), imd.getMethod(), new Object[]{result});
        if (this.next != null) {
            result = this.next.dealAfter(result);
        }
        return result;
    }

    /**
     * 执行异常拦截方法
     * @param e
     */
    @Override
    public void dealException(Throwable e) {
        innerInvoker(imd.getObject(), imd.getMethod(), new Object[]{e});
        if (this.next != null) {
            dealException(e);
        }
    }
}
