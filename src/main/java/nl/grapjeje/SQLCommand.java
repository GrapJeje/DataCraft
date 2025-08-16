package nl.grapjeje;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public abstract class SQLCommand {
    @Getter
    private static List<SQLCommand> commands = new ArrayList<>();
    private final String name;

    public SQLCommand(String cmdName) {
        this.name = cmdName;
        commands.add(this);
    }

    public String getName() {
        return name.toUpperCase();
    }

    public abstract void execute(String args);
}
