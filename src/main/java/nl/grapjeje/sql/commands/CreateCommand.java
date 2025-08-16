package nl.grapjeje.sql.commands;

import nl.grapjeje.SQLCommand;
import nl.grapjeje.sql.data.Column;
import nl.grapjeje.sql.data.DataType;
import nl.grapjeje.sql.data.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateCommand extends SQLCommand {
    public CreateCommand() {
        super("create");
    }

    @Override
    public void execute(String args) {
        args = args.trim();
        if (args.isEmpty()) return;

        String[] parts = args.split(" ", 2);
        String tableName = parts[0];

        List<Column> columns = new ArrayList<>();
        if (parts.length > 1) {
            String[] columnParts = parts[1].split(",");
            for (String col : columnParts) {
                // Verwacht format: "columnName:TYPE", bijv "username:TEXT"
                String[] colData = col.split(":");
                if (colData.length != 2) {
                    System.out.println("Invalid column format: " + col);
                    return;
                }

                String name = colData[0].trim();
                DataType type;
                try {
                    type = DataType.valueOf(colData[1].trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid data type for column: " + colData[1]);
                    return;
                }

                columns.add(new Column(name, type));
            }
        }

        Table table = new Table();
        table.create(tableName, columns);
    }
}
