package thito.reflectedbytecode;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class GAnnotation<T extends GMember> {

    public static boolean isAnnotationVisible(Type type, boolean defaultVisibility) {
        Class<?> clazz;
        if (type instanceof Class) {
            clazz = (Class<?>) type;
        } else if (type instanceof KClass) {
            clazz = ((KClass) type).getDelegated();
        } else {
            return defaultVisibility;
        }
        Retention retention = clazz.getAnnotation(Retention.class);
        return retention != null && retention.value() == RetentionPolicy.RUNTIME;
    }

    private T member;
    protected Map<String, Object> map = new HashMap<>();

    public GAnnotation(T member) {
        this.member = member;
    }

    public GAnnotation<T> value(String key, Object value) {
        map.put(key, value);
        return this;
    }

    public GAnnotation<T> value(Object value) {
        return value("value", value);
    }

    public T done() {
        return member;
    }
}
