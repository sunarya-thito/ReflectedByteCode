import org.objectweb.asm.*;
import org.objectweb.asm.util.*;

import java.io.*;
import java.util.*;

import static org.objectweb.asm.Opcodes.*;

public class ErrorTest {
    public static List<Player> list() {
        return null;
    }
    public static class Player {

    }
    public static void main(String[] args) throws Throwable {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        CheckClassAdapter w = new CheckClassAdapter(writer);
        w.visit(V1_8, ACC_PUBLIC, "test", null, "java/lang/Object", null);
        MethodVisitor v = w.visitMethod(ACC_PUBLIC | ACC_STATIC, "test", Type.getMethodDescriptor(Type.getType(Object.class), Type.getType(int.class), Type.getType(Object.class), Type.getType(Object[].class)), null, null);
        Label L14 = new Label(), L15 = new Label(), L16 = new Label(), L17 = new Label(), L18 = new Label();
        v.visitCode();
        v.visitVarInsn(ALOAD, 2);
        v.visitInsn(ARRAYLENGTH);
        v.visitInsn(ACC_PROTECTED);
        v.visitJumpInsn(IF_ICMPNE, L14);
        v.visitTypeInsn(NEW, "java/util/ArrayList");
        v.visitInsn(DUP);
        v.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false);
        v.visitVarInsn(ASTORE, 3);
        v.visitTypeInsn(NEW, "java/util/ArrayList");
        v.visitInsn(DUP);
        v.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false);
        v.visitVarInsn(ASTORE, 4);
        v.visitMethodInsn(INVOKESTATIC, "ErrorTest", "list", "()Ljava/util/List;", false);
        v.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "iterator", "()Ljava/util/Iterator;", true);
        v.visitVarInsn(ASTORE, 5);
        v.visitLabel(L15);
        v.visitVarInsn(ALOAD, 5);
        v.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true);
        v.visitJumpInsn(IFEQ, L16);
        v.visitVarInsn(ALOAD, 5);
        v.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;", true);
        v.visitVarInsn(ASTORE, 7);
        v.visitTypeInsn(CHECKCAST, "org/bukkit/entity/Player");
        v.visitVarInsn(ALOAD, 7);
        v.visitVarInsn(ASTORE, 8);
        v.visitVarInsn(ALOAD, 8);
        v.visitVarInsn(ASTORE, 6);
        v.visitVarInsn(ALOAD, 4);
        v.visitVarInsn(ALOAD, 6);
        v.visitMethodInsn(INVOKEINTERFACE, "org/bukkit/entity/HumanEntity", "getName", "()Ljava/lang/String;", true);
        v.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
        v.visitInsn(POP);
        v.visitJumpInsn(GOTO, L17);
        v.visitLabel(L16);
        v.visitJumpInsn(GOTO, L18);
        v.visitLabel(L17);
        v.visitJumpInsn(GOTO, L15);
        v.visitLabel(L18);
        v.visitVarInsn(ALOAD, 3);
        v.visitVarInsn(ALOAD, 4);
        v.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "addAll", "(Ljava/util/Collection;)Z", false);
        v.visitInsn(POP);
        v.visitVarInsn(ALOAD, 3);
        v.visitInsn(ARETURN);
        v.visitLabel(L14);
        v.visitInsn(ACC_PUBLIC);
        v.visitInsn(ARETURN);
        v.visitMaxs(4, 8);
        v.visitEnd();
        try (FileOutputStream o = new FileOutputStream("D:/test.class")) {
            o.write(writer.toByteArray());
            System.out.println(new String(writer.toByteArray()));
        }
    }
}
