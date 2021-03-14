package vip.bingzi.randomentrypro

import io.izzel.taboolib.loader.Plugin
import io.izzel.taboolib.module.config.TConfig
import io.izzel.taboolib.module.inject.TInject
import vip.bingzi.randomentrypro.listener.REView

object RandomEntryPro : Plugin() {
    // 配置文件
    @TInject(value = ["setting.yml"], locale = "LOCAL-PRIORITY")
    lateinit var setting: TConfig
        private set

    // 界面文件
    @TInject(value = ["gui.yml"], locale = "LOCAL-PRIORITY")
    lateinit var gui: TConfig
        private set

    // 主界面GUI
    lateinit var mainView: REView
    override fun onLoad() {

    }

    override fun onEnable() {

    }

    override fun onDisable() {

    }
}