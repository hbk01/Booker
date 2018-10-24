package cn.hbkcn.booker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

// Create at 2018-9-16 15:23 by 3243430237@qq.com
// TODO: 下载笔趣阁书籍

/**
 * 此类提供下载笔趣阁（http://www.biquge.com.tw）的小说
 */
public class BiQuGe implements BookDownloader {
    // 书的链接
    private static String url = "";
    // 保存工具
    private static Saver saver;
    // 每章的标题
    private static final ArrayList<String> titles = new ArrayList<>();
    // 每章的链接
    private static final ArrayList<String> urls = new ArrayList<>();

    /**
     * 创建该对象。
     * @param url 书的简介页的链接
     * @param saver 保存的目录，savePath/书名/章节名.txt
     */
    public BiQuGe(String url, Saver saver){
        this.url = url;
        this.saver = saver;
    }

    /**
     * 下载该书
     * @param time 下载每一章后等待的时间，0为不等待。不设置等待时间可能会被网站加入黑名单
     * @param listener 下载监听器，每下载一个章节就会调用一次该监听器的onDownload方法
     */
    @Override
    public void saveBook(long time, DownloadListener listener) {
        // 获取书的章节列表
        getBookPages();

        for (int i = 0; i < titles.size(); i++) {
            String title = titles.get(i);
            String url = urls.get(i);
            listener.onDownload(i + 1, urls.size(), title);
            // System.out.printf("Download %d/%d -> %s\n", i+1, urls.size(), title);

            // 获取章节内容
            String content = getBookContents(url);
            // 写入文件
            saver.save(title, content);

            // 休眠time秒再获取下一章, time为0不休眠
            if (time > 0) {
                try {
                    // System.out.println("sleep " + time + " seconds.");
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        // System.out.println("Save complete.");
    }

    /**
     * 获取章节内容
     * @param url 章节链接
     * @return 内容
     */
    private static String getBookContents(String url) {
        try {
//            System.out.println("Url --> " + url);
            Document doc = Jsoup.connect(url).get();
            Element element = doc.getElementById("content");
            String html = element.html();

            // 修复换行及空格问题
            String str = html.replace("<br>", "");
            return str.replace("&nbsp;&nbsp;&nbsp;&nbsp;", "\t");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取书的所有章节标题及链接
     */
    private static void getBookPages() {
        try {
            Document doc = Jsoup.connect(url).get();
            // 获取书名
            String bookName = doc.select("h1").text();
            saver.setBookName(bookName);

            // 获取章节列表
            Element element = doc.getElementById("list");
            Elements a = element.select("a");
            for (Element ele : a) {
                // 将章节标题及链接保存
                String title = ele.text();
                String url = R.url.笔趣阁 + ele.attr("href");
                titles.add(title);
                urls.add(url);
                // System.out.println("url = " + url + " & title = " + title);
                // Thread.sleep(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
