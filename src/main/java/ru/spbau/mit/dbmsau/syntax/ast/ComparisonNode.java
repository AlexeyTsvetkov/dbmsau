package ru.spbau.mit.dbmsau.syntax.ast;

public class ComparisonNode extends ASTNode {
    private final TerminalNode identifier;
    private final TerminalNode sign;
    private final TerminalNode rvalue;

    public ComparisonNode(TerminalNode identifier, TerminalNode sign, TerminalNode rvalue) {
        this.identifier = identifier;
        this.sign = sign;
        this.rvalue = rvalue;
    }

    public TerminalNode getIdentifier() {
        return identifier;
    }

    public TerminalNode getSign() {
        return sign;
    }

    public TerminalNode getRValue() {
        return rvalue;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
