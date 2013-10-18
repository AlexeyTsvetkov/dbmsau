package ru.spbau.mit.dbmsau;

import ru.spbau.mit.dbmsau.command.AbstractSQLCommand;
import ru.spbau.mit.dbmsau.command.SQLCommandResult;
import ru.spbau.mit.dbmsau.syntax.SyntaxAnalyzer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        SyntaxAnalyzer analyzer = new SyntaxAnalyzer(System.in);

        try {
            for (AbstractSQLCommand command : analyzer) {
                SQLCommandResult result = command.execute();

                if (result.isOk())  {
                    System.out.println("ok");
                } else {
                    System.out.println("is not ok");
                }
            }
        } catch (Throwable e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
