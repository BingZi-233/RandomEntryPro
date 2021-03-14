package vip.bingzi.randomentrypro

import io.izzel.taboolib.loader.Plugin
import io.izzel.taboolib.module.config.TConfig
import io.izzel.taboolib.module.inject.TInject
import io.izzel.taboolib.module.locale.logger.TLogger
import vip.bingzi.randomentrypro.io.REIO
import vip.bingzi.randomentrypro.listener.REUtil.logger
import vip.bingzi.randomentrypro.listener.REView

object RandomEntryPro : Plugin() {
    // 配置文件
    @TInject(value = ["setting.yml"], locale = "LOCAL-PRIORITY")
    lateinit var setting: TConfig
        private set

    // 主界面GUI
    lateinit var mainView: REView
    override fun onLoad() {

    }

    override fun onEnable() {
        // 设置日志输出等级
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
        val pathFileList = REIO.getPathFileList("${plugin.dataFolder}\\gui\\")
        logger.info("获取到目录下拥有的文件: ")
        for (fileName in pathFileList) {
            logger.info("> $fileName")
        }
    }

    override fun onDisable() {

    }
}