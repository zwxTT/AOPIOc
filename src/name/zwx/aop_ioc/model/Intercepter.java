package name.zwx.aop_ioc.model;

import name.zwx.aop_ioc.annotation.After;
import name.zwx.aop_ioc.annotation.Aspect;
import name.zwx.aop_ioc.annotation.Before;

@Aspect
public class Intercepter {
    @Before(klass = ClassA.class, method = "getClassA")
    public boolean before(String arg) {
        System.out.println("执行[getClassA]方法之前" + arg);
        return true;
    }
    @Before(klass = ClassA.class, method = "getClassA")
    public boolean doBefore(String arg) {
        System.out.println("未执行该方法");
        return true;
    }

    @Before(klass = ClassA.class, method = "getClassA")
    public boolean doBefores(String arg) {
        System.out.println("未执行该方法2");
        return true;
    }

    @After(klass = ClassA.class, method = "getClassA", parameters = {String.class})
    public String after(String arg) {
        System.out.println("执行getClassA方法之后" + arg);
        return arg;
    }
}
