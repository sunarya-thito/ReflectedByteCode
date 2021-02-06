package thito.reflectedbytecode;

import thito.reflectedbytecode.jvm.Java;

public interface ArrayReference extends Reference {

    default <T> T get(Object index) {
        return (T) Java.GetArrayElement(this, index);
    }

    default void set(Object index, Object element) {
        Java.SetArrayElement(this, index, element);
    }

    default Reference length() {
        return Java.ArrayLength(this);
    }

}
