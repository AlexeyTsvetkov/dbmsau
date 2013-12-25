package ru.spbau.mit.dbmsau.index;

import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.index.exception.IndexCreationError;
import ru.spbau.mit.dbmsau.index.exception.IndexManagerException;
import ru.spbau.mit.dbmsau.table.Table;

import java.io.*;
import java.util.*;

public class FileIndexManager extends IndexManager {
    private static final String INDEX_FILE_EXTENSION = ".idx";
    private HashMap<String,ArrayList<Index>> indexesByTables = new HashMap<>();
    private HashMap<String,Index> indexesByNames = new HashMap<>();

    public FileIndexManager(Context context) {
        super(context);
    }

    @Override
    public void createIndex(String name, Table table, List<String> columns) {
        int[] columnIndexes = table.getColumnIndexesByNames(columns);

        if (indexesByNames.containsKey(name)) {
            throw new IndexCreationError("index `" + name + "` already exists");
        }

        //for BTree only
        int rootPageId = context.getPageManager().doAllocatePage();
        BTreeIndex index = new BTreeIndex(name, table, columnIndexes, rootPageId);

        try {
            saveIndexToFile(index);
        } catch(IOException e) {
             throw new IndexManagerException("Can't save index");
        }

        addIndex(index);
    }

    @Override
    public Iterable<Index> getIndexesForTable(Table table) {
        Iterable<Index> result = indexesByTables.get(table.getName());

        if (result == null) {
            result = Collections.emptyList();
        }

        return result;
    }

    private void addIndex(Index index) {
        String tableName = index.getTable().getName();

        if (!indexesByTables.containsKey(tableName)) {
            indexesByTables.put(tableName, new ArrayList<Index>());
        }

        indexesByTables.get(tableName).add(index);
        indexesByNames.put(index.getName(), index);
    }

    @Override
    public void init() {
        super.init();
        File[] files = context.getRootDir().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(INDEX_FILE_EXTENSION);
            }
        });

        for (File f : files) {
            Index index = loadIndexFromFile(f);
            addIndex(index);
        }
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

            return new BTreeIndex(name, table, columnIndexes, rootPageId);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveIndexToFile(Index index) throws IOException {
        PrintStream os = new PrintStream(new FileOutputStream(
                context.getRootDir().getPath() + "/" + index.getName() + INDEX_FILE_EXTENSION
        ));

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
