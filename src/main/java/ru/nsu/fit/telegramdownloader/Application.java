package ru.nsu.fit.telegramdownloader;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Application {
    public static void main(String[] args) throws TelegramApiException {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new DownloaderBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}