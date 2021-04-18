package vip.bingzi.randomentrypro.util;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import vip.bingzi.randomentrypro.RandomEntryPro;

public class REVault {
    private static Economy econ = null;

    public static boolean setupEconomy() {
        if (RandomEntryPro.INSTANCE.getPlugin().getServer().getPluginManager().getPlugin("Vault") == null) {
            REUtil.INSTANCE.getLogger().warn("未找到Vault");
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            REUtil.INSTANCE.getLogger().warn("未找到Economy.class");
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }

    public static Economy getEconomy() {
        return econ;
    }
}
