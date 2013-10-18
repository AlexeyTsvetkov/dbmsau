package ru.spbau.mit.dbmsau.syntax.ast;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

abstract public class ASTNodeVisitor {
    protected String curTerminalValue;

    public void visit(TerminalNode node) {
        curTerminalValue = node.getLexemeValue();
    }

    public void visit(CreateTableStatementNode node) {
        throw new NotImplementedException();
    }
    
    protected String getTerminalValue(ASTNode node) {
        node.accept(this);
        return curTerminalValue;
    }
}
