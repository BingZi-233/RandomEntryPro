package vip.bingzi.randomentrypro.util;

import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.plugin.Plugin;
import vip.bingzi.randomentrypro.RandomEntryPro;

public class REPlayerPoints {
    private static PlayerPoints playerPoints;

    public static boolean hookPlayerPoints() {
        final Plugin plugin = RandomEntryPro.INSTANCE.getPlugin().getServer().getPluginManager().getPlugin("PlayerPoints");
        playerPoints = (PlayerPoints) plugin;
        return playerPoints != null;
    }

    public static PlayerPointsAPI getPlayerPointsAPI() {
        return playerPoints.getAPI();
    }
}
