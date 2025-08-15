package nl.grapjeje;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import nl.grapjeje.listeners.ShutDownListener;
import nl.grapjeje.sql.SQL;

import java.io.File;

public class Main {

    @Getter
    @Setter
    private static InstanceContainer instanceContainer;

    @Getter
    private static SQL sql;

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

        // Register SQL
        sql = new SQL();
        sql.init();

        // Register Events
        new ListenManager().init();

        // Register shutdown
        new ShutDownListener().shutDown();

        // Start
        server.start("0.0.0.0", 25565);
    }
}
