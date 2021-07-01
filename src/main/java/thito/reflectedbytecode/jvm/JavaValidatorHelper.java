package thito.reflectedbytecode.jvm;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import thito.reflectedbytecode.*;
import thito.reflectedbytecode.debug.*;

import java.util.*;

public class JavaValidatorHelper extends MethodVisitor {

    private List<TryCatchBlockNode> tryCatches = new ArrayList<>();
    private List<LocalVariableNode> localVariableNodes = new ArrayList<>();
    private List<AbstractInsnNode> nodes = new ArrayList<>();
    private BodyOwner method;
    private MethodVisitor mv;
    private boolean wasThrowing;
    private String descriptor;
    public JavaValidatorHelper(int api, int access, String descriptor, MethodVisitor methodVisitor, BodyOwner method) {
        super(api, methodVisitor = new MethodVisitorDebug(api, methodVisitor));
//        super(api, methodVisitor);
        this.descriptor = descriptor;
        this.method = method;
        this.mv = methodVisitor;
    }

    public void compile() {
        System.out.println("> "+descriptor);
        MethodVisitor methodVisitor = getMethodVisitor();
        for (TryCatchBlockNode node : getTryCatches()) {
            node.accept(methodVisitor);
        }
        for (AbstractInsnNode node : getNodes()) {
            node.accept(methodVisitor);
        }
    }
    public BodyOwner getMethod() {
        return method;
    }

    public MethodVisitor getMethodVisitor() {
        return mv;
    }

    public List<TryCatchBlockNode> getTryCatches() {
        return tryCatches;
    }

    public List<LocalVariableNode> getLocalVariableNodes() {
        return localVariableNodes;
    }

    public List<AbstractInsnNode> getNodes() {
        return nodes;
    }

    public boolean wasThrowing() {
        return wasThrowing;
    }

    public void preCheck() {
        wasThrowing = false;
    }

    @Override
    public void visitParameter(String name, int access) {
        preCheck();
        super.visitParameter(name, access);
    }

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        preCheck();
        return super.visitAnnotationDefault();
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        preCheck();
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        preCheck();
        return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override
    public void visitAnnotableParameterCount(int parameterCount, boolean visible) {
        preCheck();
        super.visitAnnotableParameterCount(parameterCount, visible);
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
        preCheck();
        return super.visitParameterAnnotation(parameter, descriptor, visible);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        preCheck();
        super.visitAttribute(attribute);
    }

    @Override
    public void visitCode() {
        preCheck();
        super.visitCode();
    }

    @Override
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        preCheck();
        nodes.add(new FrameNode(type, numLocal, local, numStack, stack));
    }

    @Override
    public void visitInsn(int opcode) {
        preCheck();
        nodes.add(new InsnNode(opcode));
        if (opcode == Opcodes.ATHROW) {
            wasThrowing = true;
        }
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        preCheck();
        nodes.add(new IntInsnNode(opcode, operand));
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        preCheck();
        nodes.add(new VarInsnNode(opcode, var));
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        preCheck();
        nodes.add(new TypeInsnNode(opcode, type));
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        preCheck();
        nodes.add(new FieldInsnNode(opcode, owner, name, descriptor));
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor) {
        preCheck();
        nodes.add(new MethodInsnNode(opcode, owner, name, descriptor));
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        preCheck();
        nodes.add(new MethodInsnNode(opcode, owner, name, descriptor, isInterface));
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        preCheck();
        nodes.add(new InvokeDynamicInsnNode(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments));
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        preCheck();
        nodes.add(new JumpInsnNode(opcode, new LabelNode(label)));
    }

    @Override
    public void visitLabel(Label label) {
        preCheck();
        nodes.add(new LabelNode(label));
    }

    @Override
    public void visitLdcInsn(Object value) {
        if (!(value instanceof Number || value instanceof Character ||
        value instanceof Boolean || value instanceof Type ||
        value instanceof Handle || value instanceof ConstantDynamic ||
        value instanceof String)) {
            throw new IllegalStateException(""+value);
        }
        preCheck();
        nodes.add(new LdcInsnNode(value));
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        preCheck();
        nodes.add(new IincInsnNode(var, increment));
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        preCheck();
        nodes.add(new TableSwitchInsnNode(min, max ,new LabelNode(dflt), Arrays.stream(labels).map(LabelNode::new).toArray(LabelNode[]::new)));
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        preCheck();
        nodes.add(new LookupSwitchInsnNode(new LabelNode(dflt), keys, Arrays.stream(labels).map(LabelNode::new).toArray(LabelNode[]::new)));
    }

    @Override
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
        preCheck();
        nodes.add(new MultiANewArrayInsnNode(descriptor, numDimensions));
    }

    @Override
    public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        preCheck();
        return super.visitInsnAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        preCheck();
        tryCatches.add(new TryCatchBlockNode(new LabelNode(start), new LabelNode(end), new LabelNode(handler), type));
    }

    @Override
    public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        preCheck();
        return super.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        preCheck();
        localVariableNodes.add(new LocalVariableNode(name, descriptor, signature, new LabelNode(start), new LabelNode(end), index));
    }

    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
        preCheck();
        return super.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, descriptor, visible);
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        preCheck();
        nodes.add(new LineNumberNode(line, new LabelNode(start)));
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        preCheck();
        super.visitMaxs(maxStack, maxLocals);
    }

    @Override
    public void visitEnd() {
        preCheck();
        super.visitEnd();
    }
}
