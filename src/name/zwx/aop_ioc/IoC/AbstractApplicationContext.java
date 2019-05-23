package name.zwx.aop_ioc.IoC;

import name.zwx.aop_ioc.AOP.AdviceHander;
import name.zwx.aop_ioc.AOP.AopFactory;
import name.zwx.aop_ioc.AOP.IntercepterLoader;
import name.zwx.aop_ioc.exception.BeanException;
import name.zwx.aop_ioc.exception.FileNotWiredException;
import name.zwx.aop_ioc.exception.ParameterNotMatchException;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractApplicationContext {
    /**
     * 以类名称为键，代理类AnnotationBean为值
     */
    protected static final Map<String, AnnotationBean> beanMap;
    /**
     * 以注解值为键，以类名称为值
     */
    protected static final Map<String, String> beanNameMap;
    protected AopFactory aopFactory;

    static {
        beanMap = new HashMap<>();
        beanNameMap = new HashMap<>();
    }
    protected AbstractApplicationContext() {
        aopFactory = new AopFactory();
        aopFactory.setAdvice(new AdviceHander().setIntercepterLoder(new IntercepterLoader()));
    }

    /**
     * 添加两个Map
     * @param clazz
     * @param annotationBean
     * @param beanId
     * @throws BeanException
     */
    protected void addBeanMap(Class<?> clazz, AnnotationBean annotationBean, String beanId) throws BeanException {
        AnnotationBean bean = beanMap.get(clazz.getName());
        if (bean != null) {
            throw new BeanException("bean[" + clazz.getName() + "]已经存在");
        }
        beanMap.put(clazz.getName(), annotationBean);

        if (beanId == null){
            return;
        }
        String id = beanNameMap.get(beanId);
        if (id != null) {
            throw new BeanException("beanName[" + beanId + "]已经存在");
        }
        beanNameMap.put(clazz.getName(), beanId);
    }

    /**
     * 得到代理
     * @param clazz
     * @param <E>
     * @return
     * @throws FileNotWiredException
     * @throws ParameterNotMatchException
     */
    @SuppressWarnings("unchecked")
    protected <E> E getProxy(Class<?> clazz) throws FileNotWiredException, ParameterNotMatchException {
        return (E) beanMap.get(clazz.getName()).getProxy();
    }

    /**
     * 得到原对象
     * @param className
     * @return
     * @throws FileNotWiredException
     * @throws ParameterNotMatchException
     */
    protected Object getProxy(String className) throws FileNotWiredException, ParameterNotMatchException {
        return beanMap.get(className).getObject();
    }
}
