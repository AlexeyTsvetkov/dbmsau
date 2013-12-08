package ru.spbau.mit.dbmsau.syntax.ast;

import ru.spbau.mit.dbmsau.syntax.LexerUtils;


public class TerminalNode extends ASTNode {
    protected Object value;
    protected Integer token;
    
    public TerminalNode(Object value, Integer token, Integer leftLine, Integer leftColumn, Integer rightLine, Integer rightColumn) {
        this.value = value;
        this.leftColumn = leftColumn;
        this.leftLine = leftLine;
        this.rightColumn = rightColumn;
        this.rightLine = rightLine;
        
        this.token = token;
    }
    
    public TerminalNode(Object value, Integer token, Integer leftLine, Integer leftColumn, Integer length) {
        this.value = value;
        this.leftColumn = leftColumn;
        this.leftLine = leftLine;
        this.rightColumn = leftColumn + length;
        this.rightLine = leftLine;
        
        this.token = token;
    }

    public TerminalNode(Object value) {
        this.value = value;
    }

    public int getToken() {
        return token;
    }
    
    public String getTokenName() {
        return LexerUtils.getLexemeTypeName(getToken());
    }   
    
    public String getLexemeValue() {
        return value.toString();
    }
    
    @Override
    public String toString() {
        return "'" + getLexemeValue() + "' at line " + leftLine.toString() + ", column " +leftColumn.toString();
    }

    @Override
    public String getNodeInfo() {
        return super.getNodeInfo() + "(" + getLexemeValue() + ")" ;
    }
    
    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
