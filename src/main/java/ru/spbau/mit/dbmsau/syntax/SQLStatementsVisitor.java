package ru.spbau.mit.dbmsau.syntax;

import ru.spbau.mit.dbmsau.command.AbstractSQLCommand;
import ru.spbau.mit.dbmsau.table.Column;
import ru.spbau.mit.dbmsau.command.CreateTableCommand;
import ru.spbau.mit.dbmsau.table.Type;
import ru.spbau.mit.dbmsau.syntax.ast.*;

import java.util.LinkedList;
import java.util.List;

public class SQLStatementsVisitor extends ASTNodeVisitor {

    private AbstractSQLCommand lastCommand;

    public AbstractSQLCommand buildCommand(ASTNode node) {
        node.accept(this);

        return lastCommand;
    }

    public void setLastCommand(AbstractSQLCommand lastCommand) {
        this.lastCommand = lastCommand;
    }

    @Override
    public void visit(CreateTableStatementNode node) {
        List<Column> columnDescriptions = new LinkedList<>();

        for (ColumnDescriptionNode columnDescriptionNode : node.getColumns() ) {
            Type typeDescription = Type.getType(
                    columnDescriptionNode.getType().getTypeIdentifier(),
                    columnDescriptionNode.getType().getLength()
            );
            columnDescriptions.add(new Column(columnDescriptionNode.getIdentifier(), typeDescription));
        }

        CreateTableCommand command = new CreateTableCommand(node.getTableName(), columnDescriptions);

        setLastCommand(command);
    }
}
