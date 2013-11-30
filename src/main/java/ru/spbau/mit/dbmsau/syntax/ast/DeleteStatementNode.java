package ru.spbau.mit.dbmsau.syntax.ast;

public class DeleteStatementNode extends ASTNode {
    private TerminalNode tableName;

    public DeleteStatementNode(TerminalNode tableName) {
        this.tableName = tableName;
    }

    public TerminalNode getTableName() {
        return tableName;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
