package thito.reflectedbytecode.jvm;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import thito.reflectedbytecode.Code;

public class While {

    private Label endPoint, loopPoint;

    public While(Label endPoint, Label loopPoint) {
        this.endPoint = endPoint;
        this.loopPoint = loopPoint;
    }

    public void Break() {
        Code.getCode().getCodeVisitor().visitJumpInsn(Opcodes.GOTO, endPoint);
    }
    public void Continue() {
        Code.getCode().getCodeVisitor().visitJumpInsn(Opcodes.GOTO, loopPoint);
    }

}
