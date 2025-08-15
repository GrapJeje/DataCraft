package nl.grapjeje.sql;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import nl.grapjeje.Main;

import java.util.HashMap;
import java.util.Map;

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
        Pos startPos = new Pos(0, -64, 0);
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
        Pos startPos = new Pos(0, -64, 0);
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
}
