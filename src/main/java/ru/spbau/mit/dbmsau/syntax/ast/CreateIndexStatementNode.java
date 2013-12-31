package ru.spbau.mit.dbmsau.syntax.ast;

import java.util.List;

public class CreateIndexStatementNode extends ASTNode {
    private TerminalNode indexName;
    private TerminalNode tableName;
    private List<TerminalNode> columns;

    public CreateIndexStatementNode(TerminalNode indexName, TerminalNode tableName, List<TerminalNode> columns) {
        this.indexName = indexName;
        this.tableName = tableName;
        this.columns = columns;
    }

    public TerminalNode getIndexName() {
        return indexName;
    }

    public TerminalNode getTableName() {
        return tableName;
    }

    public List<TerminalNode> getColumns() {
        return columns;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
