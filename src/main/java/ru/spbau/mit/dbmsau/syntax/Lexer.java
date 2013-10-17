package ru.spbau.mit.dbmsau.syntax;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;

public class Lexer extends GeneratedLexer {
    public Lexer(Reader in) {
        super(in);
    }

    public Lexer(InputStream in) {
        super(in);
    }

    public Lexer(String s) {
        super(new ByteArrayInputStream(s.getBytes()));
    }
}
