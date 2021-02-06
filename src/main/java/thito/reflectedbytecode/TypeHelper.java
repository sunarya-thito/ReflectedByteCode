package thito.reflectedbytecode;

import java.lang.reflect.Type;

public class TypeHelper {
    public static String getTypeDescriptor(Type type) {
        if (type instanceof Class) {
            return org.objectweb.asm.Type.getDescriptor((Class<?>) type);
        }
        if (type instanceof IClass) {
            return ((IClass) type).getDescriptor();
        }
//        throw new UnsupportedOperationException("unknown type descriptor");
        return "L" + type.getTypeName().replace('.', '/') + ";";
    }
    public static String getInternalName(Type type) {
        return type.getTypeName().replace('.', '/');
    }
}
