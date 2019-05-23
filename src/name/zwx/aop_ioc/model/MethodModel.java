package name.zwx.aop_ioc.model;

import name.zwx.aop_ioc.annotation.Bean;
import name.zwx.aop_ioc.annotation.Component;
import name.zwx.aop_ioc.annotation.Value;

@Component
public class MethodModel {
    public MethodModel() {
    }
    @Bean
    public ClassD getClassD(@Value(value = "我是D") String name) {
        ClassD c = new ClassD(name);
        return c;
    }
}
