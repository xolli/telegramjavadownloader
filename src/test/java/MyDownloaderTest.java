import org.junit.Test;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nsu.fit.telegramdownloader.DownloaderBot;
import ru.nsu.fit.telegramdownloader.Statistics;
import ru.nsu.fit.telegramdownloader.buttons.KeyboardUserMenu;
import ru.nsu.fit.telegramdownloader.implementers.TorrentDownloader;
import ru.nsu.fit.telegramdownloader.implementers.UrlDownloader;

import java.io.IOException;

public class MyDownloaderTest {

    @Test
    public void urlDownloadTest() throws TelegramApiException, IOException, InterruptedException {
        Thread testDoneThread = new Thread(new TestScript("done"));
        testDoneThread.start();
        DownloaderBot bot = TestScript.intiBot();
        UrlDownloader downloader = new UrlDownloader("1483105750", "http://speedtest.ftp.otenet.gr/files/test100k.db", bot, new Statistics(), 1483105750L, new KeyboardUserMenu());
        Thread downloaderUrl = new Thread(downloader);
        downloaderUrl.start();
        downloaderUrl.join();
        testDoneThread.join(10000L);
    }

    @Test
    public void magnetlinkTest() throws TelegramApiException, IOException, InterruptedException {
        Thread testDoneThread = new Thread(new TestScript("done"));
        testDoneThread.start();
        DownloaderBot bot = TestScript.intiBot();
        TorrentDownloader downloader = new TorrentDownloader("magnet:?xt=urn:btih:790E721E08404EE39F38B250289D485CFF9A2DC3&tr=http%3A%2F%2Fbt4.t-ru.org%2Fann%3Fmagnet&dn=%5BOther%5D%20%D0%9A%D0%BE%D0%BC%D0%BF%D0%B0%D0%BA%D1%82%D0%BD%D1%8B%D0%B9%20%D1%80%D0%B5%D1%88%D0%B0%D1%82%D0%B5%D0%BB%D1%8C%20%D1%81%D1%83%D0%B4%D0%BE%D0%BA%D1%83%20(Sudoku)%20%5B62b%5D",
                bot, "1483105750", new Statistics(), 1483105750L, new KeyboardUserMenu());
        Thread downloaderMagnetlink = new Thread(downloader);
        downloaderMagnetlink.start();
        downloaderMagnetlink.join();
        testDoneThread.join(10000L);
    }

    @Test
    public void torrentTest() throws TelegramApiException, InterruptedException {
        Thread testDoneThread = new Thread(new TestScript("done"));
        testDoneThread.start();
        DownloaderBot bot = TestScript.intiBot();
        TorrentDownloader downloader = new TorrentDownloader(new Document("BQACAgQAAxkBAAIDsWCxeZhgSY8qhEHrnFRsY__3OSTrAALBCwACtt-JUTdW__hOzd0UHwQ", "AgADwQsAArbfiVE", null, "sudoku.torrent", "application/x-bittorrent", 518),
                "testname", bot, "1483105750", new Statistics(), 1483105750L, new KeyboardUserMenu());
        Thread downloaderMagnetlink = new Thread(downloader);
        downloaderMagnetlink.start();
        downloaderMagnetlink.join();
        testDoneThread.join(10000L);
    }
}
