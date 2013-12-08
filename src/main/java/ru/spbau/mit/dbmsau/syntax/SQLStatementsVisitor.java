package ru.spbau.mit.dbmsau.syntax;

import ru.spbau.mit.dbmsau.command.*;
import ru.spbau.mit.dbmsau.table.Column;
import ru.spbau.mit.dbmsau.table.RecordComparisonClause;
import ru.spbau.mit.dbmsau.table.Type;
import ru.spbau.mit.dbmsau.syntax.ast.*;

import java.util.ArrayList;
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
            Integer typeLength = columnDescriptionNode.getType().getLength();
            Type typeDescription = Type.getType(
                    columnDescriptionNode.getType().getTypeIdentifier(),
                    typeLength == null ? Type.UNDEFINED_LENGTH : typeLength

            );
            columnDescriptions.add(new Column(columnDescriptionNode.getIdentifier(), typeDescription));
        }

        CreateTableCommand command = new CreateTableCommand(node.getTableName(), columnDescriptions);

        setLastCommand(command);
    }

    @Override
    public void visit(InsertStatementNode node) {
        List<String> columns = convertTerminalsListToStringList(node.getColumns());
        List<String> values = convertTerminalsListToStringList(node.getValues());

        setLastCommand(new InsertCommand(node.getTableName().getLexemeValue(), columns, values));
    }

    @Override
    public void visit(SelectStatementNode node) {
        String table = node.getTableFrom().getLexemeValue();

        WhereClauseNode where = node.getWhereClause();
        List<RecordComparisonClause> clauses = new ArrayList<>();

        if (where != null) {
            List<ComparisonNode> comparisons = where.getComparisons();

            for (ComparisonNode comparison : comparisons) {
                String identifier = comparison.getIdentifier().getLexemeValue();
                String sign = comparison.getSign().getLexemeValue();
                String value = comparison.getRValue().getLexemeValue();

                clauses.add(new RecordComparisonClause(identifier, sign, value));
            }
        }

        setLastCommand(new SelectCommand(table, clauses));
    }

    @Override
    public void visit(DeleteStatementNode node) {
        setLastCommand(new DeleteCommand(node.getTableName().getLexemeValue()));
    }
}
