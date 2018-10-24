package cn.hbkcn.booker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

// Create at 2018-10-5 12:45 by 3243430237@qq.com
// TODO: 下载起点小说

/*
如果要下载的书有部分是vip章节的话，vip章节只能下载前几十个字。
 */
public class QiDian implements BookDownloader {
    private static final ArrayList<String> titles = new ArrayList<>();
    private static final ArrayList<String> urls = new ArrayList<>();
    private String url = "";
    private Saver saver;

    public QiDian(String url, Saver saver) {
        this.url = url;
        this.saver = saver;
    }

    @Override
    public void saveBook(long time, DownloadListener listener) {
        getBookTitleAndUrls();
        int all = titles.size();
        for (int i = 0; i < all; i++) {
            String title = titles.get(i);
            String url = urls.get(i);
            String content = getBookContents(url);
            saver.save(title, content);
            listener.onDownload(i + 1, all, title);
            if (time > 0){
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getBookTitleAndUrls() {
        try {
            // url + #Catalog 是目录页
            String uurl = url + "#Catalog";
            Document doc = Jsoup.connect(uurl).get();

            // 拿到书名
            String bookName = doc.select("div.book-info").get(0)
                    .select("h1").get(0)
                    .select("em").get(0).text();
            saver.setBookName(bookName);

            Elements elements = doc.select("div.volume");
            for (Element ele : elements) {
                // 拿到卷名
                String page = ele.select("h3").get(0).ownText();
                // 拿到的是"正文卷共92章"这种格式，这里去掉"共92章"
                page = page.substring(0, page.indexOf("共"));

                Elements el = ele.select("ul.cf");
                for (Element e : el) {
                    Elements i = e.select("a");
                    for (Element element : i) {
                        // “卷名 标题”格式
                        String title = page + " " + element.text();
                        // 链接，这里少了https，给他补上
                        String url = "https:" + element.attr("href");
                        // System.out.println(page + " " + title + " of " + url);
                        titles.add(title);
                        urls.add(url);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getBookContents(String url){
        StringBuffer buffer = new StringBuffer();
        try {
            Document doc = Jsoup.connect(url).get();
            // 这里div的class本来是“read-content j_readContent”的，有空格识别不了
            Element element = doc.select("div.read-content").get(0);
            Elements elements = element.select("p");
            for (Element ele : elements) {
                buffer.append(ele.text());
                buffer.append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}
