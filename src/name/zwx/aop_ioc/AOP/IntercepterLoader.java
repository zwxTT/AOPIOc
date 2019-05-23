package name.zwx.aop_ioc.AOP;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 拦截方法的保存及获取
 */
public class IntercepterLoader implements IntercepterFactory {
    /**
     * 已目标拦截方法为键，拦截器链为值
     */
    private static final Map<Method, IntercepterLink> beforeMap;
    private static final Map<Method, IntercepterLink> afterMap;
    private static final Map<Method, IntercepterLink> excepterMap;

    static {
        beforeMap = new HashMap<>();
        afterMap = new HashMap<>();
        excepterMap = new HashMap<>();
    }

    public IntercepterLoader() {
    }

    private void addIntercepterMap(Map<Method, IntercepterLink> intercepterMap,
                                   Method targetMethod, IntercepterMethodDefinition imd) {
        IntercepterLink intercepterLink = intercepterMap.get(targetMethod);
        if (intercepterLink == null) {
            synchronized (intercepterMap) {
                if (intercepterLink == null) {
                    intercepterLink = new NodeListMethodChain(imd);
                }
            }
            intercepterMap.put(targetMethod, intercepterLink);
        } else {
            intercepterLink.add(imd);
        }
    }

    @Override
    public void addBeforeIntercepter(Method targetMethod, IntercepterMethodDefinition methodDefinition) {
        addIntercepterMap(beforeMap, targetMethod, methodDefinition);
    }

    @Override
    public void addAfterIntercepter(Method targetMethod, IntercepterMethodDefinition methodDefinition) {
        addIntercepterMap(afterMap, targetMethod, methodDefinition);
    }

    @Override
    public void addExceptionIntercepter(Method targetMethod, IntercepterMethodDefinition methodDefinition) {
        addIntercepterMap(excepterMap, targetMethod, methodDefinition);
    }

    @Override
    public IntercepterLink getBeforeIntercepterList(Method targetMethod) {
        return beforeMap.get(targetMethod);
    }

    @Override
    public IntercepterLink getAfterIntercepterList(Method targetMethod) {
        return afterMap.get(targetMethod);
    }

    @Override
    public IntercepterLink getExcepterIntercepterList(Method targetMethod) {
        return excepterMap.get(targetMethod);
    }
}
