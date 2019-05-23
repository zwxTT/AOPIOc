package name.zwx.aop_ioc.model;

import name.zwx.aop_ioc.annotation.AutoWired;
import name.zwx.aop_ioc.annotation.Component;
import name.zwx.aop_ioc.annotation.Value;
@Component
public class ClassA {
    @Value(value = "我是A")
    private String name;
    @AutoWired
    private ClassB classB;

    public ClassA() {}
    public String getClassA(String arg) {
        return "34";
    }

    @Override
    public String toString() {
        return "ClassA" + ":" + name + ":" + classB;
    }
}
