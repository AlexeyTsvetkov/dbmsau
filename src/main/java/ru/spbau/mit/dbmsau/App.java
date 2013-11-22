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

        if (args.length == 0) {
            System.err.println("Usage: dbmsau <path>");
            System.exit(1);
        }

        Context context = Context.loadContextFromPath(args[0]);

        SyntaxAnalyzer analyzer = new SyntaxAnalyzer(System.in);

        try {
            for (AbstractSQLCommand command : analyzer) {
                command.setContext(context);

                SQLCommandResult result = command.execute();

                if (result.isOk())  {

                    if (result.isIterable()) {
                        for (String resultLine : result) {
                            System.out.println(resultLine);
                        }
                    }

                    System.out.println("ok");
                } else {
                    System.out.println("is not ok");
                }
            }
        } catch (Throwable e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        context.onQuit();
    }
}
