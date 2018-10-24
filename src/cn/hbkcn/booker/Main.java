package cn.hbkcn.booker;

import java.util.PrimitiveIterator;
import java.util.Scanner;

// Create at 2018-9-16 15:22 by 3243430237@qq.com
// TODO: 启动
/*
所有书的链接都是要电脑端的网页的链接，手机上下载需要先切换UA，获得电脑端网页的链接。
*/

public class Main {
    private static Scanner input = new Scanner(System.in);
    private static ConsoleProgressBar bar;
    public static void main(String[] args) {
        System.out.println("小说下载器 v1.0 Build by 3243430237@qq.com");
        System.out.println();
        while (true){
            System.out.println(
                    "1.起点小说网(https://www.qidian.com/)\n" +
                    "2.笔趣阁(http://www.biquge.com.tw/)\n" +
                    "3.新笔趣阁(https://www.xbiquge6.com/)");
            System.out.print("选择网站: ");
            int website = input.nextInt();
            String url = "";
            String path = "";
            switch (website) {
                case 1:
                    System.out.print("请输入小说详情页链接：");
                    url = input.next();
                    System.out.print("请输入保存路径：");
                    path = input.next();

                    qidian(url, new Saver(path));
                    break;
                case 2:
                    System.out.print("请输入小说详情页链接：");
                    url = input.next();
                    System.out.print("请输入保存路径：");
                    path = input.next();

                    biquge(url, new Saver(path));
                    break;
                case 3:
                    System.out.print("请输入小说详情页链接：");
                    url = input.next();
                    System.out.print("请输入保存路径：");
                    path = input.next();

                    xbiquge(url, new Saver(path));
                    break;
                default:
                    System.out.println("错误的选择。");
                    break;
            }

        }
    }

    public static void qidian(String url, Saver saver){
        new QiDian(url, saver).saveBook(0, (now, all, title) -> {
            bar = new ConsoleProgressBar(0, all, 30);
            bar.show(now, title);
        });
    }

    public static void biquge(String url, Saver saver) {
        new BiQuGe(url, saver).saveBook(0, (now, all, title) -> {
            bar = new ConsoleProgressBar(0, all, 30);
            bar.show(now, title);
        });
    }

    public static void xbiquge(String url, Saver saver) {
        new XBiQuGe(url, saver).saveBook(0, (now, all, title) -> {
            bar = new ConsoleProgressBar(0, all, 30);
            bar.show(now, title);
        });
    }
}
