package nl.grapjeje.listeners;

import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import nl.grapjeje.Listen;
import nl.grapjeje.Listener;
import nl.grapjeje.Main;

public class PlayerLoginListener implements Listener {

    @Listen
    public void onPlayerLogin(AsyncPlayerConfigurationEvent e) {
        final Player player = e.getPlayer();

        e.setSpawningInstance(Main.getInstanceContainer());
        player.getAttribute(Attribute.BLOCK_BREAK_SPEED).setBaseValue(0.0f);
        player.setGameMode(GameMode.SPECTATOR);
    }
}
