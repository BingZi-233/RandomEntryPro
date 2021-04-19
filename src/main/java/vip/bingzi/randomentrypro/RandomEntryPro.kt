package vip.bingzi.randomentrypro

import io.izzel.taboolib.loader.Plugin
import io.izzel.taboolib.module.config.TConfig
import io.izzel.taboolib.module.inject.TInject
import io.izzel.taboolib.module.locale.logger.TLogger
import vip.bingzi.randomentrypro.io.REMenu.menuInspection
import vip.bingzi.randomentrypro.net.Metrics
import vip.bingzi.randomentrypro.util.REPlayerPoints
import vip.bingzi.randomentrypro.util.REUtil.logger
import vip.bingzi.randomentrypro.util.REVault

object RandomEntryPro : Plugin() {
    @TInject(value = ["pointsidentify.yml"], locale = "LOCALE-PRIORITY")
    lateinit var pointsidentify: TConfig
        private set

    @TInject(value = ["vaultidentify.yml"], locale = "LOCALE-PRIORITY")
    lateinit var vaultidentify: TConfig
        private set

    @TInject(value = ["setting.yml"], locale = "LOCALE-PRIORITY")
    lateinit var setting: TConfig
        private set

    override fun onLoad() {
        logger.info("正在进行预初始化中...")
        menuInspection(plugin, "${plugin.dataFolder}/gui")
        logger.info("预初始化完成")
    }

    override fun onEnable() {
        logger.info("正在进行初始化中...")
        logger.level = when (setting.getString("Settings.Logger")) {
            "VERBOSE" -> TLogger.VERBOSE
            "FINEST" -> TLogger.FINEST
            "FINE" -> TLogger.FINE
            "INFO" -> TLogger.INFO
            "WARN" -> TLogger.WARN
            "ERROR" -> TLogger.ERROR
            "FATAL" -> TLogger.FATAL
            else -> TLogger.INFO
        }
        logger.info("依赖载入状态：")
        logger.info("> Vault")
        logger.info("-   ${REVault.setupEconomy()}")
        logger.info("> PlayerPoints")
        logger.info("-   ${REPlayerPoints.hookPlayerPoints()}")
        // 匿名收集，服务由bStats提供
        Metrics(plugin, 11089)
        logger.info("初始化完成")
    }

    override fun onDisable() {
        logger.info("正在进行反初始化中")
        logger.info("反初始化完成")
    }
}