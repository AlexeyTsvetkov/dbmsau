package ru.spbau.mit.dbmsau.syntax;

import ru.spbau.mit.dbmsau.command.*;
import ru.spbau.mit.dbmsau.command.where.ComparisonClause;
import ru.spbau.mit.dbmsau.command.where.WhereExpression;
import ru.spbau.mit.dbmsau.relation.Column;
import ru.spbau.mit.dbmsau.relation.ColumnAccessor;
import ru.spbau.mit.dbmsau.relation.Type;
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

        for (ColumnDescriptionNode columnDescriptionNode : node.getColumns()) {
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

    private ColumnAccessor buildColumnAccessorByNode(ColumnAccessorNode columnAccessorNode) {
        ColumnAccessor accessor;

        if (columnAccessorNode.getTableIdent() != null) {
            accessor = new ColumnAccessor(
                columnAccessorNode.getTableIdent().getLexemeValue(),
                columnAccessorNode.getColumnIdent().getLexemeValue()
            );
        } else {
            accessor = new ColumnAccessor(
                null,
                columnAccessorNode.getColumnIdent().getLexemeValue()
            );
        }

        return accessor;
    }

    private WhereExpression buildWhereExpression(List<ComparisonNode> comparisonNodes) {
        if (comparisonNodes == null) {
            return null;
        }

        List<ComparisonClause> clauses = new ArrayList<>();

        for (ComparisonNode node : comparisonNodes) {
            clauses.add(
                new ComparisonClause(
                    buildColumnAccessorByNode(node.getColumnAccessor()),
                    node.getRValue().getLexemeValue(),
                    node.getSign()
                )
            );
        }

        return new WhereExpression(clauses);
    }

    @Override
    public void visit(SelectStatementNode node) {
        String table = node.getTableFrom().getLexemeValue();


        ArrayList<ColumnAccessor> selectColumnAccessors = null;

        //column accessors after 'SELECT' word
        if (node.getAccessors() != null) {
            selectColumnAccessors = new ArrayList<>();
            for (ColumnAccessorNode columnAccessorNode : node.getAccessors()) {
                selectColumnAccessors.add(buildColumnAccessorByNode(columnAccessorNode));
            }
        }

        JoinDescription join = null;

        if (node.getJoin() != null) {
            join = new JoinDescription(
                node.getJoin().getTable().getLexemeValue(),
                buildColumnAccessorByNode(node.getJoin().getLeft()),
                buildColumnAccessorByNode(node.getJoin().getRight())
            );
        }

        setLastCommand(
            new SelectCommand(
                selectColumnAccessors, table,
                buildWhereExpression(node.getWhereClause()),
                join
            )
        );
    }

    @Override
    public void visit(DeleteStatementNode node) {
        setLastCommand(new DeleteCommand(node.getTableName().getLexemeValue(), buildWhereExpression(node.getWhereClause())));
    }
}
