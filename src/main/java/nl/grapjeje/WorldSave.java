package nl.grapjeje;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.anvil.AnvilLoader;

import java.io.File;

public class WorldSave {
    private static WorldSave worldSave;

    private final String currentDirectory = System.getProperty("user.dir");
    @Getter
    private final String worldsDirectory = currentDirectory + File.separator + "worlds";

    public static WorldSave get() {
        if (worldSave == null) worldSave = new WorldSave();
        return worldSave;
    }

    public void save(InstanceContainer instanceContainer) {
        System.out.println("Saving world...");
        instanceContainer.saveChunksToStorage().join();
    }

    public InstanceContainer load(String loadPath) {
        InstanceContainer instanceContainer = MinecraftServer.getInstanceManager().createInstanceContainer();
        AnvilLoader loader = new AnvilLoader(loadPath);
        instanceContainer.setChunkLoader(loader);

        // Create world folder
        File worldFolder = new File(loadPath);
        if (!worldFolder.exists()) worldFolder.mkdirs();

        return instanceContainer;
    }
}
