package name.zwx.aop_ioc.IoC;

/**
 * 常规部分类型值的转换
 */
public class TypeConversation {
    public TypeConversation() {
    }

    public static Object getTypeValue(String value, String type) {
        if (type.equals("String")) {
            return String.valueOf(value);
        } else if (type.equals("int")) {
            return Integer.valueOf(value);
        } else if (type.equals("boolean")) {
            return Boolean.valueOf(value);
        } else {
            return Long.valueOf(value);
        }
    }
}
