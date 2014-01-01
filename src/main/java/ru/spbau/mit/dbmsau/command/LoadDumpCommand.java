package ru.spbau.mit.dbmsau.command;

import java_cup.runtime.Symbol;
import ru.spbau.mit.dbmsau.command.exception.CommandExecutionException;
import ru.spbau.mit.dbmsau.exception.UserError;
import ru.spbau.mit.dbmsau.relation.MemoryRelationRecord;
import ru.spbau.mit.dbmsau.syntax.LexemeType;
import ru.spbau.mit.dbmsau.syntax.Lexer;
import ru.spbau.mit.dbmsau.syntax.ast.TerminalNode;
import ru.spbau.mit.dbmsau.syntax.exception.LexicalError;
import ru.spbau.mit.dbmsau.table.Table;
import ru.spbau.mit.dbmsau.table.exception.RecordManagerException;

import java.io.FileInputStream;
import java.io.IOException;

public class LoadDumpCommand extends AbstractSQLCommand {
    private String tableName;
    private String path;
    private Lexer lexer;
    private Table table;
    private boolean eof;

    public LoadDumpCommand(String tableName, String path) {
        this.tableName = tableName;
        this.path = path;
    }

    private String readValue() {
        if (eof) {
            return null;
        }

        try {
            Symbol symbol = lexer.next_token();

            if (symbol.sym == LexemeType.EOF) {
                eof = true;
            }

            String value = ((TerminalNode) symbol.value).getLexemeValue();

            symbol = lexer.next_token();

            if (symbol.sym == LexemeType.EOF) {
                eof = true;
            } else if (symbol.sym != LexemeType.COMMA) {
                throw new UserError("Syntax: comma expected");
            }

            return value;

        } catch (IOException e) {
            throw new UserError("IO: " + e.getMessage());
        } catch (LexicalError e) {
            throw new UserError("Syntax: " + e.getMessage());
        }
    }

    private void readRecord(MemoryRelationRecord relationRecord) {
        for (int i = 0; i < table.getColumnsCount(); i++) {
            String value = readValue();
            if (value == null) {
                throw new UserError("value expected");
            }

            relationRecord.setValueFromString(i, value);
        }
    }

    @Override
    protected SQLCommandResult doExecute() throws CommandExecutionException {
        FileInputStream inputStream;

        try {
            inputStream = new FileInputStream(path);
        } catch (IOException e) {
            throw new UserError("IO: " + e.getMessage());
        }

        table = getTable(tableName);


        eof = false;
        lexer = new Lexer(inputStream);

        MemoryRelationRecord record = new MemoryRelationRecord(table);

        int rows = 0;

        while (!eof) {
            readRecord(record);

            rows++;

            try {
                getContext().getTableRecordManager().insert(table, record);
            } catch (RecordManagerException e) {
                throw new UserError("INSERT: " + e.getMessage());
            }
        }

        return new SQLCommandResult(rows);
    }
}
