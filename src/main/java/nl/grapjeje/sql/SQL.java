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
        String[] parts = cmd.split(" ");
        if (parts.length < 2) return;

        String cmdWord = parts[0].toUpperCase();
        switch (cmdWord) {
            case "INSERT":
                char c = parts[1].charAt(0);
                Data.writeChar(c);
                break;
            case "SELECT":
                System.out.println(Data.readChar());
                break;
        }
    }
}
