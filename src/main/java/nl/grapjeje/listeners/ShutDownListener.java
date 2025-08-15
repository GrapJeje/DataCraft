package nl.grapjeje.listeners;

import nl.grapjeje.Main;
import nl.grapjeje.WorldSave;

public class ShutDownListener {

    public void shutDown() {
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
                WorldSave.get().save(Main.getInstanceContainer())));
    }
}
