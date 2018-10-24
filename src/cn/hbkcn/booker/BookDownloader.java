package cn.hbkcn.booker;

// Create at 2018-10-5 12:31 by 3243430237@qq.com
// TODO: download books.
public interface BookDownloader {
    void saveBook(long time, DownloadListener listener);
}
