package ru.spbau.mit.dbmsau.syntax.ast;

public class ComparisonNode extends ASTNode {
    private final ColumnAccessorNode columnAccessor;
    private final TerminalNode sign;
    private final TerminalNode rvalue;
    private final boolean reversed;

    public ComparisonNode(ColumnAccessorNode columnAccessor, TerminalNode sign, TerminalNode rvalue, boolean reversed) {
        this.columnAccessor = columnAccessor;
        this.sign = sign;
        this.rvalue = rvalue;
        this.reversed = reversed;
    }

    public ColumnAccessorNode getColumnAccessor() {
        return columnAccessor;
    }

    public String getSign() {
        if (reversed) {
            switch (sign.getLexemeValue()) {
                case ">":
                    return "<";
                case "<":
                    return ">";
                case ">=":
                    return "<=";
                case "<=":
                    return ">=";
            }
        }

        return sign.getLexemeValue();
    }

    public TerminalNode getRValue() {
        return rvalue;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
