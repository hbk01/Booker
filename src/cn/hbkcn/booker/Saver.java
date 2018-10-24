package cn.hbkcn.booker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// Create at 2018-10-5 14:11 by 3243430237@qq.com
// TODO: save books

/**
 * 该类用作保存下载好的章节。
 * 若章节名中包含不能作为文件名的字符，程序将统一替换为下划线(_)符号
 */
public class Saver {
    private String path = "";
    // 是否在章节前加数字前缀
    private boolean prefix = false;
    // 章节计数器
    private int count = 0;

    // 不能作为文件名的字符
    private String[] noName = {
            "\\", "/", ":", "*", "?", "\"", "<", ">", "|"
    };

    private Saver() {
    }

    public Saver(String path) {
        if (path.endsWith(File.separator)) {
            this.path = path;
        } else {
            this.path = path + File.separator;
        }
        new File(path).mkdirs();
    }

    protected void setBookName(String name){
        path = path + name + File.separator;
        new File(path).mkdirs();
    }

    /**
     * 是否在标题前加数字前缀
     * @param prefix true是
     */
    public void setPrefix(boolean prefix){
        this.prefix = prefix;
    }

    /**
     * 保存文件
     * @param title 标题
     * @param content 内容
     */
    public void save(String title, String content) {
        try {
            // 写入文件
            title = okTitle(title);
            String str = path + (prefix ? ++count + "." + title : title)+ ".txt";
            FileWriter writer = new FileWriter(str);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检测标题是否合法，若包含不合法字符，该字符将被替换为下划线(_)
     * @param title 要检测的标题
     * @return 检测后的标题，若标题合法，则该标题会与参数title相同
     */
    private String okTitle(String title){
        for (String str : noName) {
            if (title.contains(str)){
                title = title.replace(str, "_");
                System.out.println("Title %d isn't use to ");
            }
        }
        return title;
    }
}
