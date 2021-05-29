import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.nsu.fit.telegramdownloader.DownloaderBot;

import java.io.IOException;

public class TestScript implements Runnable {
    private final String mode;

    public TestScript(String mode) {
        this.mode = mode;
    }

    @Override
    public void run() {
        String line = "python3 testfiles/checktest.py " + mode;
        CommandLine cmdLine = CommandLine.parse(line);
        DefaultExecutor executor = new DefaultExecutor();
        try {
            executor.execute(cmdLine);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static DownloaderBot intiBot() throws TelegramApiException {
        DownloaderBot bot = new DownloaderBot();
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(bot);
        return bot;
    }
}
