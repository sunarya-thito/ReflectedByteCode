package thito.reflectedbytecode;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.lang.ref.WeakReference;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class Code implements AutoCloseable {
    private static final ThreadLocal<WeakReference<Code>> currentCode = new ThreadLocal<>();

    static Code pushCode(int modifiers, MethodVisitor visitor) {
        return pushCode(new Code(modifiers, visitor));
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

    private MethodVisitor visitor;
    private int modifiers;
    private int localIndex;
    private int labelIndex;
    private boolean markReturn, markSuperConstructor;
    private Map<Object, Label> labelMap = new HashMap<>();

    Code(int modifiers, MethodVisitor visitor) {
        this.modifiers = modifiers;
        this.visitor = visitor;
    }

    public MethodVisitor getCodeVisitor() {
        return visitor;
    }

    int getModifiers() {
        return modifiers;
    }

    public int requestLocalIndex() {
        return Modifier.isStatic(modifiers) ? localIndex++ : localIndex++ + 1;
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

    public void skipLocalIndex(int amount) {
        localIndex += amount;
    }

    public Label getLabel(String name) {
        return labelMap.computeIfAbsent(name, x -> new Label());
    }

    public Label requestLabel() {
        return labelMap.computeIfAbsent(labelIndex++, x -> new Label());
    }

    public void close() {
        visitor.visitMaxs(-1, -1);
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
