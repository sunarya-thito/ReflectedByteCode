package thito.reflectedbytecode;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.*;
import org.objectweb.asm.tree.*;
import thito.reflectedbytecode.jvm.*;

import java.lang.ref.*;
import java.util.*;

public class Code implements AutoCloseable {
    private static final ThreadLocal<WeakReference<Code>> currentCode = new ThreadLocal<>();

    static Code pushCode(int modifiers, MethodVisitor visitor, GMethod method) {
        return pushCode(new Code(modifiers, visitor, method));
    }
    static Code pushCode(int modifiers, MethodVisitor visitor, GConstructor constructor) {
        return pushCode(new Code(modifiers, visitor, constructor));
    }
    static Code pushCode(Code code) {
        currentCode.set(new WeakReference<>(code));
        return code;
    }
    public static Code getCode() {
        WeakReference<Code> code = currentCode.get();
        if (code == null) throw new IllegalStateException("code must be inside of a body");
        Code c = code.get();
        if (c == null) throw new IllegalStateException("code must be inside of a body");
        return c;
    }

    private IClass returnType;
    private MethodVisitor visitor;
    private int modifiers;
    private int labelIndex;
    private boolean markReturn, markSuperConstructor;
    private Map<Object, Label> labelMap = new HashMap<>();
    private LocalFieldMap localFieldMap;

    Code(int modifiers, MethodVisitor visitor, GMethod method) {
        localFieldMap = new LocalFieldMap(method);
        this.modifiers = modifiers;
        this.visitor = visitor;
        this.returnType = method.getReturnType();
    }

    Code(int modifiers, MethodVisitor visitor, GConstructor method) {
        localFieldMap = new LocalFieldMap(method);
        this.modifiers = modifiers;
        this.visitor = visitor;
        this.returnType = Java.Class(void.class);
    }

    public boolean isReturnVoid() {
        return returnType == null || returnType.getDescriptor().equals("V");
    }

    public IClass getReturnType() {
        return returnType;
    }

    public LocalFieldMap getLocalFieldMap() {
        return localFieldMap;
    }

    public MethodVisitor getCodeVisitor() {
        return visitor;
    }

    int getModifiers() {
        return modifiers;
    }


    public boolean isMarkedReturn() {
        return markReturn;
    }

    public boolean isMarkedSuperConstructor() {
        return markSuperConstructor;
    }

    public void markSuperConstructor() {
        markSuperConstructor = true;
    }

    public void markReturn() {
        markReturn = true;
    }

    public Label getLabel(String name) {
        return labelMap.computeIfAbsent(name, x -> new Label());
    }

    public Label requestLabel() {
        return labelMap.computeIfAbsent(labelIndex++, x -> new Label());
    }

    public void close() {
        JavaValidatorHelper helper = (JavaValidatorHelper) visitor;
        helper.compile();
        visitor.visitMaxs(Short.MAX_VALUE, Short.MAX_VALUE);
        visitor.visitEnd();
        WeakReference<Code> current = currentCode.get();
        if (current != null) {
            Code code = current.get();
            if (code == this) {
                currentCode.set(null);
            }
        }
    }
}
