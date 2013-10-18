package ru.spbau.mit.dbmsau.syntax;
import ru.spbau.mit.dbmsau.command.AbstractSQLCommand;
import ru.spbau.mit.dbmsau.syntax.ast.ASTNode;
import ru.spbau.mit.dbmsau.syntax.exception.LexicalError;
import ru.spbau.mit.dbmsau.syntax.exception.SyntaxErrors;
import ru.spbau.mit.dbmsau.syntax.exception.SyntaxFatalError;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

public class SyntaxAnalyzer implements Iterable< AbstractSQLCommand >, Iterator< AbstractSQLCommand > {

    private Parser parser;
    private ASTNode nextStatementNode = null;
    private SQLStatementsVisitor visitor = new SQLStatementsVisitor();

    public SyntaxAnalyzer(InputStream is) {
        setParserByLexer(buildLexer(is));
    }

    public SyntaxAnalyzer(String s) {
        setParserByLexer(buildLexer(s));
    }

    @Override
    public boolean hasNext() {
        nextStatementNode = (ASTNode)nextParserResult();

        return nextStatementNode != null;
    }

    @Override
    public AbstractSQLCommand next() {
        return visitor.buildCommand(nextStatementNode);
    }

    @Override
    public void remove() {

    }

    @Override
    public Iterator<AbstractSQLCommand> iterator() {
        return this;
    }

    private Object nextParserResult() {

        Object result = null;
        try {
            result = parser.parse().value;
        } catch (LexicalError e) {
            throw new SyntaxErrors(e.getMessage());
        } catch (SyntaxFatalError e) {
            throw new SyntaxErrors(parser.getErrors());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        if (parser.getErrors().size() > 0) {
            throw new SyntaxErrors(parser.getErrors());
        }

        return result;
    }

    private Lexer buildLexer(String s) {
        return new Lexer(s);
    }

    private Lexer buildLexer(InputStream is) {
        return new Lexer(is);
    }

    private void setParserByLexer(Lexer l) {
        parser = new Parser(l);
    }
}

