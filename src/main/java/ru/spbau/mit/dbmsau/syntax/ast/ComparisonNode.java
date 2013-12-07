package ru.spbau.mit.dbmsau.syntax.ast;

public class ComparisonNode extends ASTNode {
    private final TerminalNode comparison;
    private final TerminalNode identifier;
    private final TerminalNode rvalue;

    public ComparisonNode(TerminalNode identifier, TerminalNode comparison, TerminalNode rvalue) {
        this.comparison = comparison;
        this.identifier = identifier;
        this.rvalue = rvalue;
    }

    public TerminalNode getComparison() {
        return comparison;
    }

    public TerminalNode getIdentifier() {
        return identifier;
    }

    public TerminalNode getRValue() {
        return rvalue;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
