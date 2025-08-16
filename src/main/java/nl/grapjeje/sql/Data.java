package nl.grapjeje.sql;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.world.DimensionType;
import nl.grapjeje.Main;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Data {
    private static final Map<Block, Integer> woolData = new HashMap<>();
    private static final Map<Integer, Block> valueToWool = new HashMap<>();

    static {
        Block[] wools = {
                Block.WHITE_WOOL, Block.ORANGE_WOOL, Block.MAGENTA_WOOL, Block.LIGHT_BLUE_WOOL,
                Block.YELLOW_WOOL, Block.LIME_WOOL, Block.PINK_WOOL, Block.GRAY_WOOL,
                Block.LIGHT_GRAY_WOOL, Block.CYAN_WOOL, Block.PURPLE_WOOL, Block.BLUE_WOOL,
                Block.BROWN_WOOL, Block.GREEN_WOOL, Block.RED_WOOL, Block.BLACK_WOOL
        };
        for (int i = 0; i < wools.length; i++) {
            woolData.put(wools[i], i);
            valueToWool.put(i, wools[i]);
        }
    }

    private static Pos getCharPos(Pos startPos, int count) {
        int charsPerChunkX = 16 / 4;
        int charsPerChunkZ = 16;
        int charsPerLayer = charsPerChunkX * charsPerChunkZ;

        int layer = count / charsPerLayer;
        int withinLayer = count % charsPerLayer;
        int chunkZ = withinLayer / charsPerChunkX;
        int chunkX = withinLayer % charsPerChunkX;

        return startPos.add(chunkX * 4, layer, chunkZ);
    }

    public static void writeString(String text) {
        writeString(new Pos(0, -64, 0), text);
    }

    public static void writeString(Pos startPos, String text) {
        for (int count = 0; count < text.length(); count++) {
            Pos charPos = getCharPos(startPos, count);
            char c = text.charAt(count);

            for (int i = 0; i < 4; i++) {
                int nibble = (c >> (12 - i * 4)) & 0xF;
                Main.getInstanceContainer().setBlock(charPos.add(i, 0, 0), valueToWool.get(nibble));
            }
        }
    }

    public static String readString() {
        return readString(new Pos(0, -64, 0));
    }

    public static String readString(Pos startPos) {
        StringBuilder sb = new StringBuilder();
        int count = 0;

        while (true) {
            Pos charPos = getCharPos(startPos, count);
            int value = 0;

            for (int i = 0; i < 4; i++) {
                Block block = Main.getInstanceContainer().getBlock(charPos.add(i, 0, 0));
                int nibble = woolData.getOrDefault(block, 0);
                value |= nibble << (12 - i * 4);
            }

            char c = (char) value;
            if (c == 0) break;
            sb.append(c);
            count++;
        }

        return sb.toString();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Table {
        // Layer 1,2 -> name

        private Chunk chunk = null;

        private static final int MAX_TABLE_LENGTH = 128;

        public static Table getByName(String name) {
            InstanceContainer instance = Main.getInstanceContainer();
            for (Chunk chunk : instance.getChunks()) {
                Pos startPos = new Pos(chunk.getChunkX() << 4, -64, chunk.getChunkZ() << 4);
                String stored = readString(startPos);
                if (stored.equals(name)) return new Table(chunk);
            }
            return null;
        }

        // API
        public void create(String name) {
            if (name.length() > MAX_TABLE_LENGTH) {
                System.out.println("Table '" + name + "' is too long!");
                return;
            }
            if (tableNameAlreadyExists(name)) {
                System.out.println("Table '" + name + "' already exists!");
                return;
            }
            this.chunk = getFirstEmptyChunk();
            setName(name);
        }

        // Private instance methods
        private void setName(String name) {
            Pos startPos = getChunkAnchor();
            writeString(startPos, name);
        }

        private Pos getChunkAnchor() {
            Objects.requireNonNull(this.chunk, "Table chunk has not been set yet!");
            return new Pos(this.chunk.getChunkX() << 4, -64, this.chunk.getChunkZ() << 4);
        }

        // Static helpers
        private static boolean tableNameAlreadyExists(String name) {
            InstanceContainer instance = Main.getInstanceContainer();
            for (Chunk chunk : instance.getChunks()) {
                Pos startPos = new Pos(chunk.getChunkX() << 4, -64, chunk.getChunkZ() << 4);
                String stored = readString(startPos);
                if (stored.equals(name)) return true;
            }
            return false;
        }

        private static Chunk getFirstEmptyChunk() {
            InstanceContainer instance = Main.getInstanceContainer();
            DimensionType dimension = MinecraftServer.getDimensionTypeRegistry()
                    .get(instance.getDimensionType());

            int chunkX = 0;
            int chunkZ = 0;

            while (true) {
                Chunk chunk = instance.getChunk(chunkX, chunkZ);
                if (chunk != null && isChunkEmpty(chunk, dimension)) {
                    return chunk;
                }
                chunkX++;
            }
        }

        private static boolean isChunkEmpty(Chunk chunk, DimensionType dimension) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = dimension.minY(); y < dimension.maxY(); y++) {
                        if (!chunk.getBlock(x, y, z).isAir()) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    }
}
