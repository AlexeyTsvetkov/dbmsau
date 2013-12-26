package ru.spbau.mit.dbmsau.syntax.ast;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.LinkedList;
import java.util.List;

abstract public class ASTNodeVisitor {
    protected String curTerminalValue;

    public void visit(TerminalNode node) {
        curTerminalValue = node.getLexemeValue();
    }

    public void visit(CreateTableStatementNode node) {
        throw new NotImplementedException();
    }

    public void visit(InsertStatementNode node) {
        throw new NotImplementedException();
    }

    public void visit(SelectStatementNode node) {
        throw new NotImplementedException();
    }

    public void visit(DeleteStatementNode node) {
        throw new NotImplementedException();
    }

    public void visit(ComparisonNode node) {
        throw new NotImplementedException();
    }

    protected String getTerminalValue(ASTNode node) {
        node.accept(this);
        return curTerminalValue;
    }

    protected List<String> convertTerminalsListToStringList(List<TerminalNode> nodeList) {
        List<String> result = new LinkedList<>();

        for (TerminalNode node : nodeList) {
            result.add(node.getLexemeValue());
        }

        return result;
    }
}
