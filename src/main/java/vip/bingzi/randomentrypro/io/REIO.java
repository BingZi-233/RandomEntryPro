package vip.bingzi.randomentrypro.io;

import vip.bingzi.randomentrypro.RandomEntryPro;
import vip.bingzi.randomentrypro.listener.REUtil;

import java.io.File;

public class REIO {
    public static String[] getPathFileList(String s) {
        File file = new File(s);
        if (!file.exists()) {
            REUtil.INSTANCE.getLogger().warn("! 指定的文件夹不存在，正在进行创建");
            file.mkdir();
            RandomEntryPro.INSTANCE.getPlugin().saveResource("gui/main.yml", false);
        }
        return file.list();
    }
}
