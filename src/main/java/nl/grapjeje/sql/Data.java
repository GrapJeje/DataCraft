package nl.grapjeje.sql;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import nl.grapjeje.Main;

import java.util.HashMap;
import java.util.Map;

public class Data {
    private static final Map<Block, Integer> woolData = new HashMap<>();
    public static final Map<Integer, Block> valueToWool = new HashMap<>();

    static {
        woolData.put(Block.WHITE_WOOL, 0);
        woolData.put(Block.ORANGE_WOOL, 1);
        woolData.put(Block.MAGENTA_WOOL, 2);
        woolData.put(Block.LIGHT_BLUE_WOOL, 3);
        woolData.put(Block.YELLOW_WOOL, 4);
        woolData.put(Block.LIME_WOOL, 5);
        woolData.put(Block.PINK_WOOL, 6);
        woolData.put(Block.GRAY_WOOL, 7);
        woolData.put(Block.LIGHT_GRAY_WOOL, 8);
        woolData.put(Block.CYAN_WOOL, 9);
        woolData.put(Block.PURPLE_WOOL, 10);
        woolData.put(Block.BLUE_WOOL, 11);
        woolData.put(Block.BROWN_WOOL, 12);
        woolData.put(Block.GREEN_WOOL, 13);
        woolData.put(Block.RED_WOOL, 14);
        woolData.put(Block.BLACK_WOOL, 15);

        for (Map.Entry<Block, Integer> entry : woolData.entrySet()) {
            valueToWool.put(entry.getValue(), entry.getKey());
        }
    }

    public static void writeChar(char c) {
        Pos startPos = new Pos(0, 0, 0);
        for (int i = 0; i < 4; i++) {
            int nibble = ((int) c >> (12 - i*4)) & 0xF;
            Block wool = valueToWool.get(nibble);
            Pos pos = startPos.add(i, 0, 0);
            Main.getInstanceContainer().setBlock(pos, wool);
        }
    }

    public static char readChar() {
        int value = 0;
        Pos startPos = new Pos(0, 0, 0);
        for (int i = 0; i < 4; i++) {
            Pos pos = startPos.add(i, 0, 0);
            Block block = Main.getInstanceContainer().getBlock(pos);
            int nibble = woolData.getOrDefault(block, 0);
            value |= (nibble << (12 - i*4));
        }
        return (char)value;
    }
}
