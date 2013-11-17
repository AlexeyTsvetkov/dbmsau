package ru.spbau.mit.dbmsau.syntax.ast;

import java.util.List;

public class InsertStatementNode extends ASTNode {
    private TerminalNode tableName;
    private List<TerminalNode> columns;
    private List<TerminalNode> values;

    public InsertStatementNode(TerminalNode tableName, List<TerminalNode> columns, List<TerminalNode> values) {
        this.tableName = tableName;
        this.columns = columns;
        this.values = values;
    }

    public TerminalNode getTableName() {
        return tableName;
    }

    public List<TerminalNode> getColumns() {
        return columns;
    }

    public List<TerminalNode> getValues() {
        return values;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
