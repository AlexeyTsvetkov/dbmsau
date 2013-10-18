package ru.spbau.mit.dbmsau.syntax.ast;

import java.util.List;

public class CreateTableStatementNode extends ASTNode {
    private String tableName;
    private List< ColumnDescriptionNode > columns;

    public CreateTableStatementNode(String tableName, List<ColumnDescriptionNode> columns) {
        this.tableName = tableName;
        this.columns = columns;
    }

    public CreateTableStatementNode(TerminalNode tableName, List<ColumnDescriptionNode> columns) {
        this(tableName.getLexemeValue(), columns);
    }

    public String getTableName() {
        return tableName;
    }

    public List<ColumnDescriptionNode> getColumns() {
        return columns;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
