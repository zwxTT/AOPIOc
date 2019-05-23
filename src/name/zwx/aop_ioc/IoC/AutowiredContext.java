package name.zwx.aop_ioc.IoC;

import name.zwx.aop_ioc.annotation.AutoWired;
import name.zwx.aop_ioc.annotation.Value;
import name.zwx.aop_ioc.exception.FileNotWiredException;
import name.zwx.aop_ioc.exception.ParameterNotMatchException;

import java.lang.reflect.Field;

/**
 * 注入成员
 */
public class AutowiredContext extends AbstractContext {

    public AutowiredContext() {
    }

    public AutowiredContext(String packageName) throws FileNotWiredException, ParameterNotMatchException {
        super(packageName);
    }

    @Override
    public <E> E getProxy(Class<?> clazz) throws FileNotWiredException,
            ParameterNotMatchException {
        return get(clazz.getName());
    }

    @Override
    public Object getProxy(String className) throws FileNotWiredException,
            ParameterNotMatchException {
        return get(className);
    }

    private <E> E get(String className) throws FileNotWiredException, ParameterNotMatchException {
        AnnotationBean annotationBean = beanMap.get(className);
        if (annotationBean == null) {
            throw new FileNotWiredException("成员[" + className + "]没有注入");
        }

        if (!annotationBean.isDI() && annotationBean instanceof BeanElement) {
            autoWired(annotationBean);
        }
        return annotationBean.getProxy();
    }

    private void autoWired(AnnotationBean annotationBean) throws ParameterNotMatchException {
        annotationBean.setDI(true);
        Object object = annotationBean.getObject();
        Class<?> klass = object.getClass();
        Field[] fields = klass.getDeclaredFields();
        Object result = null;
        for (Field field : fields) {
            if (field.isAnnotationPresent(Value.class)) {
                result = injectVaule(field);
            } else if(field.isAnnotationPresent(AutoWired.class)) {
                result = injectBean(field);
            } else {
                continue;
            }
            try {
                field.setAccessible(true);
                field.set(object, result);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 注入带Value注解的成员
     * @param field
     * @return
     * @throws ParameterNotMatchException
     */
    private Object injectVaule(Field field) throws ParameterNotMatchException {
        Class<?> type = field.getType();
        if (!(type.isPrimitive() || type.equals(String.class))) {
            throw new ParameterNotMatchException("成员[" + type.getName() + "]的类型不匹配");
        }
        String value = field.getAnnotation(Value.class).value();
        return TypeConversation.getTypeValue(value, type.getSimpleName());
    }
    /**

     * 注入带Bean成员的注解
     * @param field
     * @return
     * @throws ParameterNotMatchException
     */
    private Object injectBean(Field field) throws ParameterNotMatchException {
        Class<?> type = field.getType();
        AnnotationBean annotationBean = beanMap.get(type.getName());
        if (annotationBean == null) {
            return null;
        }
        //若成员类型未注入，继续采用递归注入
        if (!annotationBean.isDI() && annotationBean instanceof BeanElement) {
            autoWired(annotationBean);
        }
        return annotationBean.getProxy();
    }
}
