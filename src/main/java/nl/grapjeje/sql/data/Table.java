package nl.grapjeje.sql.data;

import lombok.*;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.world.DimensionType;
import nl.grapjeje.Main;

import java.util.*;

import static nl.grapjeje.sql.Data.readString;
import static nl.grapjeje.sql.Data.writeString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Table {
    // Layer 1,2 -> Name
    private Chunk homeChunk = null;
    private static final int MAX_TABLE_LENGTH = 128;

    public static Table getByName(String name) {
        InstanceContainer instance = Main.getInstanceContainer();
        for (Chunk chunk : instance.getChunks()) {
            Pos startPos = new Pos(chunk.getChunkX() << 4, -64, chunk.getChunkZ() << 4);
            String stored = readString(startPos);
            if (stored.equals(name)) {
                return new Table(chunk);
            }
        }
        return null;
    }

    public void create(String name, List<Column> columns) {
        if (name.length() > MAX_TABLE_LENGTH) {
            System.out.println("[SQL] Failed to create table: Name '" + name + "' is too long!");
            return;
        }
        if (tableNameAlreadyExists(name)) {
            System.out.println("[SQL] Failed to create table: Name '" + name + "' already exists!");
            return;
        }
        this.homeChunk = getFirstEmptyChunk();
        writeString(new Pos(homeChunk.getChunkX() << 4, -64, homeChunk.getChunkZ() << 4), name);

        int y = -62;
        for (Column col : columns) {
            Pos colPos = new Pos(homeChunk.getChunkX() << 4, y, homeChunk.getChunkZ() << 4);
            writeString(colPos, col.getName() + ":" + col.getType().name());
            y += col.getType().getHeight();
        }
        System.out.println("[SQL] Table '" + name + "' successfully created with " + columns.size() + " columns!");
    }

    public List<Column> getColumns() {
        List<Column> columns = new ArrayList<>();
        int y = -62;
        while (true) {
            Pos pos = new Pos(homeChunk.getChunkX() << 4, y, homeChunk.getChunkZ() << 4);
            String colData = readString(pos);
            if (colData == null || colData.isEmpty()) break;

            String[] parts = colData.split(":");
            String name = parts[0];
            DataType type = DataType.valueOf(parts[1]);
            columns.add(new Column(name, type));
            y += type.getHeight();
        }
        return columns;
    }

    private Chunk getSouthChunk(int southOffset) {
        return Main.getInstanceContainer().getChunk(homeChunk.getChunkX(), homeChunk.getChunkZ() + southOffset);
    }

    public void insert(Row row) {
        List<Column> columns = getColumns();
        int southChunkOffset = 1;

        while (true) {
            Chunk targetChunk = getSouthChunk(southChunkOffset);
            int nextRowIndex = getNextRowIndexInChunk(targetChunk, columns);

            if (nextRowIndex < getMaxRowsPerChunk()) {
                int y = -62 + nextRowIndex;
                for (Column col : columns) {
                    Pos rowPos = new Pos(targetChunk.getChunkX() << 4, y, targetChunk.getChunkZ() << 4);
                    writeString(rowPos, row.getValue(col.toString()));
                    y += col.getType().getHeight();
                }
                break;
            } else {
                southChunkOffset++;
            }
        }
    }

    private int getNextRowIndexInChunk(Chunk chunk, List<Column> columns) {
        int y = 0;
        while (y < getMaxRowsPerChunk()) {
            Pos pos = new Pos(chunk.getChunkX() << 4, -62 + y, chunk.getChunkZ() << 4);
            if (readString(pos) == null || readString(pos).isEmpty()) return y;
            y++;
        }
        return getMaxRowsPerChunk();
    }

    private int getMaxRowsPerChunk() {
        return 128;
    }

    private int getNextRowIndex() {
        int y = 0;
        while (true) {
            Pos pos = new Pos(homeChunk.getChunkX() << 4, -62 + y, (homeChunk.getChunkZ() << 4) - 1 - y);
            if (readString(pos) == null || readString(pos).isEmpty()) return y;
            y++;
        }
    }

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
            if (chunk != null && isChunkEmpty(chunk, dimension)) return chunk;
            chunkX++;
        }
    }

    private static boolean isChunkEmpty(Chunk chunk, DimensionType dimension) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = dimension.minY(); y < dimension.maxY(); y++) {
                    if (!chunk.getBlock(x, y, z).isAir()) return false;
                }
            }
        }
        return true;
    }
}
