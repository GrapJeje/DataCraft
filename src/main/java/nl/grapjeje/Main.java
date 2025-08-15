package nl.grapjeje;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.ServerProcess;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;

import java.io.File;

/**
 * Main entry point for the DataCraft server.
 * Initializes Minestom, loads the world, registers listeners and starts the server.
 */
public class Main {

    @Getter
    @Setter
    private static InstanceContainer instanceContainer;

    public static void main(String[] args) {

        //  Initialize server
        MinecraftServer server = MinecraftServer.init();
        MinecraftServer.setBrandName("DataCraft");

        // Create instance
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer container = WorldSave.get().load(
                WorldSave.get().getWorldsDirectory() + File.separator + "data"
        );

        setInstanceContainer(container);
        instanceManager.registerInstance(container);

        // Register events
        new ListenManager().init();

        // Start
        server.start("0.0.0.0", 25565);
    }
}
