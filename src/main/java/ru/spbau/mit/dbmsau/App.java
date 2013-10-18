package ru.spbau.mit.dbmsau;

import ru.spbau.mit.dbmsau.syntax.SyntaxAnalyzer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        SyntaxAnalyzer a = new SyntaxAnalyzer();

        try {
            a.execute(System.in);
        } catch (Throwable e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
