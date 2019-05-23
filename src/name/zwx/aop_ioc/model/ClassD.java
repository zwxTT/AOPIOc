package name.zwx.aop_ioc.model;

import name.zwx.aop_ioc.annotation.AutoWired;
import name.zwx.aop_ioc.annotation.Component;
import name.zwx.aop_ioc.annotation.Value;
public class ClassD {
    private String name;
    @AutoWired
    private ClassA classA;

    public ClassD() {}
    public ClassD(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ClassD" + ":" + name + ":" + classA;
    }
}
