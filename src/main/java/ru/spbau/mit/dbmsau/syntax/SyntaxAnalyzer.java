package ru.spbau.mit.dbmsau.syntax;
import ru.spbau.mit.dbmsau.syntax.exception.LexicalError;
import ru.spbau.mit.dbmsau.syntax.exception.SyntaxErrors;
import ru.spbau.mit.dbmsau.syntax.exception.SyntaxFatalError;

import java.io.InputStream;

public class SyntaxAnalyzer {

    private Object getParserResultByLexer(Lexer l) {
        Parser p = new Parser(l);

        Object result = null;
        try {
            result = p.parse().value;
        } catch (LexicalError e) {
            throw new SyntaxErrors(e.getMessage());
        } catch (SyntaxFatalError e) {
            throw new SyntaxErrors(p.getErrors());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        if (p.getErrors().size() > 0) {
            throw new SyntaxErrors(p.getErrors());
        }

        return result;
    }

    private void executeByLexer(Lexer l) {
        System.out.println((String)getParserResultByLexer(l));
    }

    private Lexer buildLexer(String s) {
        return new Lexer(s);
    }

    private Lexer buildLexer(InputStream is) {
        return new Lexer(is);
    }

    public void execute(String s) {
         executeByLexer(buildLexer(s));
    }

    public void execute(InputStream is) {
        executeByLexer(buildLexer(is));
    }
}

