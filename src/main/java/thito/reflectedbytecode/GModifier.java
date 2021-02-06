package thito.reflectedbytecode;

public class GModifier<T extends GMember> {
    protected T member;

    public GModifier(T member) {
        this.member = member;
    }

    GModifier<T> or(int bit) {
        member.setModifiers(member.getModifiers() | bit);
        return this;
    }

    GModifier<T> nAnd(int bit) {
        member.setModifiers(member.getModifiers() & ~bit);
        return this;
    }

    public T done() {
        return member;
    }
}
