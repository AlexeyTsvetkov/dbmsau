package ru.spbau.mit.dbmsau;

import ru.spbau.mit.dbmsau.command.AbstractSQLCommand;
import ru.spbau.mit.dbmsau.command.SQLCommandResult;
import ru.spbau.mit.dbmsau.exception.UserError;
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
            while (true) {
                System.out.print('>');

                SQLCommandResult result = null;

                try {
                    if (!analyzer.hasNext()) {
                        break;
                    }

                    AbstractSQLCommand command = analyzer.next();

                    command.setContext(context);

                    result = command.execute();
                } catch (UserError error) {
                    System.out.println(error.getMessage());
                    continue;
                }

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

            context.onQuit();
        } catch (Throwable e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
