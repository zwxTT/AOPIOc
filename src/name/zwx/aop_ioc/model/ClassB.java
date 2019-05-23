package name.zwx.aop_ioc.model;

import name.zwx.aop_ioc.annotation.AutoWired;
import name.zwx.aop_ioc.annotation.Component;
import name.zwx.aop_ioc.annotation.Value;
@Component
public class ClassB {
    @Value(value = "我是B")
    private String name;
    @AutoWired
    private ClassC classC;

    public ClassB() {}
    public String getClassB(String name) {
        return "56";
    }

    @Override
    public String toString() {
        return "ClassB" + ":" + name + ":" + classC;
    }
}
