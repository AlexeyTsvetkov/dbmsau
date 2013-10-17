package ru.spbau.mit.dbmsau.syntax;
import ru.spbau.mit.dbmsau.syntax.exception.LexicalError;
import ru.spbau.mit.dbmsau.syntax.exception.SyntaxErrorsException;
import ru.spbau.mit.dbmsau.syntax.exception.SyntaxFatalError;

import java.io.InputStream;

public class SyntaxAnalyzer {

    private Object getParserResultByLexer(Lexer l) throws SyntaxErrorsException {
        Parser p = new Parser(l);

        Object result = null;
        try {
            result = p.parse().value;
        } catch (LexicalError e) {
            throw new SyntaxErrorsException(e.getMessage());
        } catch (SyntaxFatalError e) {
            throw new SyntaxErrorsException(p.getErrors());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return result;
    }

    private void executeByLexer(Lexer l) throws SyntaxErrorsException {
        System.out.println((String)getParserResultByLexer(l));
    }

    private Lexer buildLexer(String s) {
        return new Lexer(s);
    }

    private Lexer buildLexer(InputStream is) {
        return new Lexer(is);
    }

    public void execute(String s) throws SyntaxErrorsException {
         executeByLexer(buildLexer(s));
    }

    public void execute(InputStream is) throws SyntaxErrorsException {
        executeByLexer(buildLexer(is));
    }
}

