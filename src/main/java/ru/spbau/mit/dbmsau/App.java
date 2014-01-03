package ru.spbau.mit.dbmsau;

import ru.spbau.mit.dbmsau.command.AbstractSQLCommand;
import ru.spbau.mit.dbmsau.command.SQLCommandResult;
import ru.spbau.mit.dbmsau.command.exception.CommandExecutionException;
import ru.spbau.mit.dbmsau.exception.UserError;
import ru.spbau.mit.dbmsau.syntax.SyntaxAnalyzer;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {

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
                    long start = System.currentTimeMillis();
                    result = command.execute();

                    if (result.isOk()) {

                        if (result.isIterable()) {
                            for (String resultLine : result) {
                                System.out.println(resultLine);
                            }
                        }

                        long end = System.currentTimeMillis();

                        System.out.println(
                            String.format(
                                "OK, %d ms, %d rows affected",
                                end - start,
                                result.getRowsAffected()
                            )
                        );
                    } else {
                        System.out.println("is not ok");
                    }
                } catch (UserError error) {
                    System.out.println(error.getMessage());
                    continue;
                } catch (CommandExecutionException error) {
                    System.out.println(error.getMessage());
                    continue;
                }
            }

            context.onQuit();
        } catch (Throwable e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
