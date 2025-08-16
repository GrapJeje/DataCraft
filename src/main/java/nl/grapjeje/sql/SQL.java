package nl.grapjeje.sql;

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
    }

    public void execute(String cmd) {
        cmd = cmd.trim();
        String[] parts = cmd.split(" ", 2);
        String cmdWord = parts[0].toUpperCase();

        switch (cmdWord) {
            case "INSERT":
                if (parts.length < 2) return;
                Data.writeString(parts[1]);
                break;
            case "SELECT":
                System.out.println(Data.readString());
                break;
            case "CREATE":
                if (parts.length < 2) return;
                Data.Table table = new Data.Table();
                table.create(parts[1]);
                break;
        }
    }
}
