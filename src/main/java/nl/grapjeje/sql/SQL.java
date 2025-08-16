package nl.grapjeje.sql;

import nl.grapjeje.SQLCommand;
import nl.grapjeje.sql.commands.CreateCommand;
import nl.grapjeje.sql.commands.InsertCommand;

import java.util.Scanner;

public class SQL {

    public void init() {
        // Register Scanner
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while(true) {
                String line = scanner.nextLine();
                this.execute(line);
            }
        }).start();

        // Register Commands
        new InsertCommand();
        new CreateCommand();
    }

    public void execute(String input) {
        input = input.trim().toUpperCase();

        for (SQLCommand cmd : SQLCommand.getCommands()) {
            if (input.startsWith(cmd.getName())) {
                String args = input.substring(cmd.getName().length()).trim();
                cmd.execute(args);
                return;
            }
        }
    }
}
