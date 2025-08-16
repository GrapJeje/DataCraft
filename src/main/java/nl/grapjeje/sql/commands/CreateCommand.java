package nl.grapjeje.sql.commands;

import nl.grapjeje.SQLCommand;
import nl.grapjeje.sql.Data;

public class CreateCommand extends SQLCommand {
    public CreateCommand() {
        super("create");
    }

    @Override
    public void execute(String args) {
        Data.Table table = new Data.Table();
        table.create(args);
    }
}
