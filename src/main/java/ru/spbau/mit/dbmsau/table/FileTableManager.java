package ru.spbau.mit.dbmsau.table;

import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.pages.PagesList;
import ru.spbau.mit.dbmsau.table.exception.TableManagerException;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FileTableManager extends TableManager {

    private final String TABLE_FILE_EXTENSION = ".tbl";

    public FileTableManager(Context context) {
        super(context);
    }

    @Override
    public void init() {
        File[] files = context.getRootDir().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(TABLE_FILE_EXTENSION);
            }
        });

        for (File f : files) {
            Table table = loadTableFromFile(f);
            tables.put(table.getName(), table);
        }
    }

    @Override
    public void createNewTable(Table table) throws TableManagerException {
        if (tables.containsKey(table.getName())) {
            throw new TableManagerException("Table `" + table.getName() + "` already exists");
        }

        createTablePagesLists(table);

        try {
            saveTableToFile(table);
        } catch (IOException e) {
            throw new TableManagerException("IO: " + e.getMessage());
        }

        tables.put(table.getName(), table);
    }

    private void createTablePagesLists(Table table) {
        table.setFullPagesListHeadPageId(PagesList.createNewList(context).getHeadPageId());
        table.setNotFullPagesListHeadPageId(PagesList.createNewList(context).getHeadPageId());
    }

    private Table loadTableFromFile(File file) {
        try {
            Scanner is = new Scanner(new FileInputStream(file));
            String name = is.nextLine();
            Integer fullPageListHeadPageId = is.nextInt();
            Integer notFullPageListHeadPageId = is.nextInt();
            Integer columnsAmount = is.nextInt();
            is.nextLine();

            ArrayList<Column> columns = new ArrayList<>();

            for (int i = 0; i < columnsAmount; i++) {
                String columnName = is.nextLine();
                Integer type = is.nextInt();
                Integer length = is.nextInt();

                is.nextLine();

                if (length == -1) {
                    length = null;
                }

                columns.add(new Column(columnName, new Type(type, length)));
            }

            Table table = new Table(name, columns, fullPageListHeadPageId, notFullPageListHeadPageId);
            return table;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveTableToFile(Table table) throws IOException {
        PrintStream os = new PrintStream(new FileOutputStream(
                context.getRootDir().getPath() + "/" + table.getName() + TABLE_FILE_EXTENSION
        ));

        os.println(table.getName());
        os.println(table.getFullPagesListHeadPageId().toString() + " " + table.getNotFullPagesListHeadPageId());
        os.println(table.getColumns().size());

        for (Column column : table.getColumns()) {
            os.println(column.getName());
            os.print(column.getType().getType().toString() + " ");

            if (column.getType().getLength() != null) {
                os.print(column.getType().getLength());
            } else {
                os.print("-1");
            }

            os.println();
        }

        os.close();
    }
}
