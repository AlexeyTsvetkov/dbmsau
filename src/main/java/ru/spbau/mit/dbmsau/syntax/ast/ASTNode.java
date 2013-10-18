package ru.spbau.mit.dbmsau.syntax.ast;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Field;
import java.util.LinkedList;

abstract public class ASTNode {
    protected ASTNode[] children;
    
    protected Integer leftLine,leftColumn,rightLine,rightColumn;
    
    public ASTNode() {

    }
    
    public ASTNode(ASTNode[] chldrn) {
        children = chldrn;
    }

    public ASTNode[] getChildren() {
        LinkedList< ASTNode > childrenList = new LinkedList< ASTNode >();
        Class curClass = this.getClass();
        while (!curClass.getName().equals(ASTNode.class.getName())) {
            for (Field f : curClass.getDeclaredFields() ) {
                if (ASTNode.class.isAssignableFrom(f.getType())) {
                    f.setAccessible(true);
                    try {
                        Object o = f.get(this);
                        if (o != null) {
                            childrenList.add((ASTNode)f.get(this));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    f.setAccessible(false);
                }
            }
            curClass = curClass.getSuperclass();
        }
        
        children = new ASTNode[childrenList.size()];
        
        childrenList.toArray(children);
        return children;
    }
    
    public String getNodeInfo() {
        return this.getClass().getSimpleName();
    }

    public Integer getLeftColumn() {
        return leftColumn;
    }

    public Integer getLeftLine() {
        return leftLine;
    }

    public Integer getRightColumn() {
        return rightColumn;
    }

    public Integer getRightLine() {
        return rightLine;
    }
    
    public String getPosition() {
        return "line " + getLeftLine().toString() + ", column " + getLeftColumn().toString(); 
    }
    
    public void accept(ASTNodeVisitor visitor) {
        throw new NotImplementedException();
    }
}
