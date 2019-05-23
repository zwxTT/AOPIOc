package name.zwx.aop_ioc.model;

import name.zwx.aop_ioc.IoC.AutowiredContext;
import name.zwx.aop_ioc.AOP.IntercepterLoaderFactory;
import name.zwx.aop_ioc.exception.FileNotWiredException;
import name.zwx.aop_ioc.exception.ParameterNotMatchException;

public class Test {
    public static void main(String[] args){
        try {
            AutowiredContext auto = new AutowiredContext("name.zwx.aop_ioc.model");
            ClassD classD = auto.getProxy(ClassD.class);
            System.out.println(classD);

            IntercepterLoaderFactory intercepterLoderFactory = new IntercepterLoaderFactory();
            intercepterLoderFactory.scanMethodForPackage("name.zwx.aop_ioc.model");

            ClassA classA = auto.getProxy(ClassA.class);
            String x = classA.getClassA("zwx");
            System.out.println(x);
        } catch (FileNotWiredException e) {
            e.printStackTrace();
        } catch (ParameterNotMatchException e) {
            e.printStackTrace();
        }
    }
}
