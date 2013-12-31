package ru.spbau.mit.dbmsau.index;

import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.index.exception.IndexException;
import ru.spbau.mit.dbmsau.table.Table;

import java.io.*;
import java.util.Scanner;

public class FileIndexManager extends IndexManager {
    private static final String INDEX_FILE_EXTENSION = ".idx";


    public FileIndexManager(Context context) {
        super(context);
    }

    @Override
    public void init() {

        File[] files = context.getRootDir().listFiles(
            new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(INDEX_FILE_EXTENSION);
                }
            }
        );

        for (File f : files) {
            Index index = loadIndexFromFile(f);
            addIndex(index);
        }

        super.init();
    }

    private Index loadIndexFromFile(File file) {
        try {
            Scanner is = new Scanner(new FileInputStream(file));

            String name = is.nextLine();
            String tableName = is.nextLine();

            int columnsCount = is.nextInt();
            int[] columnIndexes = new int[columnsCount];

            for (int i = 0; i < columnsCount; i++) {
                columnIndexes[i] = is.nextInt();
            }

            int rootPageId = is.nextInt();

            Table table = context.getTableManager().getTable(tableName);

            return new BTreeIndex(name, table, columnIndexes, rootPageId, context);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void saveIndex(Index index) throws IndexException {
        try {
            saveIndexToFile(index);
        } catch (IOException e) {
            throw new IndexException(e.getMessage());
        }
    }

    private void saveIndexToFile(Index index) throws IOException {
        PrintStream os = new PrintStream(
            new FileOutputStream(
                context.getRootDir().getPath() + "/" + index.getName() + INDEX_FILE_EXTENSION
            )
        );

        os.println(index.getName());
        os.println(index.getTable().getName());
        os.println(Integer.valueOf(index.getColumnIndexes().length).toString());

        for (int columnIndex : index.getColumnIndexes()) {
            os.println(columnIndex);
        }

        if (index instanceof BTreeIndex) {
            BTreeIndex bTreeIndex = (BTreeIndex) index;
            os.println(bTreeIndex.getRootPageId());
        }

        os.close();
    }
}
