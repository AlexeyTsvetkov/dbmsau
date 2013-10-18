package ru.spbau.mit.dbmsau.syntax;

import ru.spbau.mit.dbmsau.command.AbstractSQLCommand;
import ru.spbau.mit.dbmsau.command.create_table.ColumnDescription;
import ru.spbau.mit.dbmsau.command.create_table.CreateTableCommand;
import ru.spbau.mit.dbmsau.command.create_table.TypeDescription;
import ru.spbau.mit.dbmsau.command.exception.CommandExecutionException;
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
        List< ColumnDescription > columnDescriptions = new LinkedList<>();

        for (ColumnDescriptionNode columnDescriptionNode : node.getColumns() ) {
            TypeDescription typeDescription = new TypeDescription(
                    columnDescriptionNode.getType().getTypeIdentifier(),
                    columnDescriptionNode.getType().getLength()
            );
            columnDescriptions.add(new ColumnDescription(columnDescriptionNode.getIdentifier(), typeDescription));
        }

        CreateTableCommand command = new CreateTableCommand(node.getTableName(), columnDescriptions);

        setLastCommand(command);
    }
}
