package nl.grapjeje.sql.commands;

import nl.grapjeje.SQLCommand;
import nl.grapjeje.sql.data.Column;
import nl.grapjeje.sql.data.Row;
import nl.grapjeje.sql.data.Table;

import java.util.List;

public class InsertCommand extends SQLCommand {
    public InsertCommand() {
        super("insert into");
    }

    @Override
    public void execute(String args) {
        args = args.trim();
        if (args.isEmpty()) return;

        String[] parts = args.split("(?i)\\s+VALUES\\s+", 2);
        if (parts.length < 2) {
            System.out.println("Invalid syntax. Use: INSERT INTO tableName VALUES val1,val2,...");
            return;
        }

        String tableName = parts[0].trim();
        String[] values = parts[1].split(",");

        Table table = Table.getByName(tableName);
        if (table == null) {
            System.out.println("Table '" + tableName + "' does not exist!");
            return;
        }

        List<Column> columns = table.getColumns();
        if (columns.size() != values.length) {
            System.out.println("Column count does not match value count!");
            return;
        }

        Row row = new Row();
        for (int i = 0; i < columns.size(); i++) {
            row.set(columns.get(i).getName(), values[i].trim());
        }

        table.insert(row);
    }
}
