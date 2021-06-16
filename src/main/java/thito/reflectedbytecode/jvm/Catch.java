package thito.reflectedbytecode.jvm;

import org.objectweb.asm.*;
import thito.reflectedbytecode.*;

import java.lang.reflect.*;

public class Catch {
    private Try aTry;

    public Catch(Try aTry) {
        this.aTry = aTry;
    }

    public void Caught(Body<Caught> accessor) {
        if (aTry.body != null) throw new IllegalStateException("already caught");
        aTry.body = accessor;
        Label start = new Label();
        Label handler = new Label();
        Label end = new Label();
        MethodVisitor visitor = Code.getCode().getCodeVisitor();
        visitor.visitTryCatchBlock(start, handler, handler, aTry.type.getTypeName().replace('.', '/'));
        visitor.visitLabel(start);
        aTry.mainBody.run();
        visitor.visitJumpInsn(Opcodes.GOTO, end);
        visitor.visitLabel(handler);
        ILocalField field = Code.getCode().getLocalFieldMap().createField(IClass.fromClass(aTry.type));
        visitor.visitVarInsn(Opcodes.ASTORE, ((LField) field).getLocalIndex());
        if (aTry.body != null) {
            aTry.body.declareBody(new Caught(((LField) field).getLocalIndex(), aTry.type));
        }
        visitor.visitLabel(end);
    }
}
