package ru.nsu.fit.telegramdownloader;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Application {
    public static void main(String[] args) throws TelegramApiException {

        Controller controller = new Controller();

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            DownloaderBot telegramBot = new DownloaderBot(controller);
            botsApi.registerBot(telegramBot);
            controller.setBot(telegramBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
