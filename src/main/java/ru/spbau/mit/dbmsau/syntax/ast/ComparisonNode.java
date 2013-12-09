package ru.spbau.mit.dbmsau.syntax.ast;

public class ComparisonNode extends ASTNode {
    private final TerminalNode column;
    private final TerminalNode sign;
    private final TerminalNode rvalue;

    public ComparisonNode(TerminalNode column, TerminalNode sign, TerminalNode rvalue) {
        this.column = column;
        this.sign = sign;
        this.rvalue = rvalue;
    }

    public TerminalNode getColumn() {
        return column;
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
