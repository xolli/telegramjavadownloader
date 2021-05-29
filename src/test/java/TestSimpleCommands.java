import org.junit.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nsu.fit.telegramdownloader.DownloaderBot;
import ru.nsu.fit.telegramdownloader.Statistics;
import ru.nsu.fit.telegramdownloader.implementers.GetStat;
import ru.nsu.fit.telegramdownloader.implementers.Helper;

public class TestSimpleCommands {
    @Test
    public void testPersonalStat() throws TelegramApiException, InterruptedException {
        DownloaderBot bot =TestScript.intiBot();
        GetStat.sendMyStat(bot, new Statistics(), 1483105750L, new SendMessage("1483105750", ""));
        Thread testScriptThread = new Thread(new TestScript("personalstat"));
        testScriptThread.start();
        testScriptThread.join(10000L);
    }

    @Test
    public void testAllStat() throws TelegramApiException, InterruptedException {
        DownloaderBot bot =TestScript.intiBot();
        GetStat.sendAllStat(bot, new Statistics(), 1483105750L, new SendMessage("1483105750", ""));
        Thread testScriptThread = new Thread(new TestScript("allstat"));
        testScriptThread.start();
        testScriptThread.join(10000L);
    }

    @Test
    public void testHelp() throws TelegramApiException, InterruptedException {
        DownloaderBot bot =TestScript.intiBot();
        Helper.sendHelp(bot, new SendMessage("1483105750", ""));
        Thread testScriptThread = new Thread(new TestScript("helper"));
        testScriptThread.start();
        testScriptThread.join();
    }
}
