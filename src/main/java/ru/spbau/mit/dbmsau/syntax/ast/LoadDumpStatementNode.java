package ru.spbau.mit.dbmsau.syntax.ast;

public class LoadDumpStatementNode extends ASTNode {
    private TerminalNode tableName;
    private TerminalNode path;

    public LoadDumpStatementNode(TerminalNode tableName, TerminalNode path) {
        this.tableName = tableName;
        this.path = path;
    }

    public TerminalNode getTableName() {
        return tableName;
    }

    public TerminalNode getPath() {
        return path;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
