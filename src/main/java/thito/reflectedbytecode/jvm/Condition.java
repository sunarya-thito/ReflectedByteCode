package thito.reflectedbytecode.jvm;

import org.objectweb.asm.*;
import thito.reflectedbytecode.Code;

public class Condition {

    private Label endPoint;
    private Label elsePoint;
    private JavaValidatorHelper.ActiveCode activeCondition = () -> {
        MethodVisitor visitor = Code.getCode().getCodeVisitor();
        visitor.visitLabel(elsePoint == null ? endPoint : elsePoint);
    };

    public Condition(Label endPoint) {
        JavaValidatorHelper.getState().markActive(activeCondition);
        this.endPoint = endPoint;
    }

    public Condition Then(Runnable body) {
        JavaValidatorHelper.ValidatorState state = JavaValidatorHelper.getState();
        state.markInactive(activeCondition);
        body.run();
        state.markActive(activeCondition);
        return this;
    }

    public void Else(Runnable body) {
        MethodVisitor visitor = Code.getCode().getCodeVisitor();
        JavaValidatorHelper.ValidatorState state = JavaValidatorHelper.getState();
        state.markInactive(activeCondition);
        elsePoint = new Label();
        visitor.visitJumpInsn(Opcodes.GOTO, elsePoint);
        visitor.visitLabel(endPoint);
        body.run();
        visitor.visitLabel(elsePoint);
    }

}
