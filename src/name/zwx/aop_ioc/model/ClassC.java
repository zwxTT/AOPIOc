package name.zwx.aop_ioc.model;

import name.zwx.aop_ioc.annotation.AutoWired;
import name.zwx.aop_ioc.annotation.Component;
import name.zwx.aop_ioc.annotation.Value;
@Component
public class ClassC {
    @Value(value = "我是C")
    private String name;
    @AutoWired
    private ClassA classA;

    public ClassC() {}

    public String getClassC(String name) {
        return "78";
    }

    @Override
    public String toString() {
        return "ClassC" + ":" + name;
    }
}
