package ru.nsu.fit.telegramdownloader.implementers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nsu.fit.telegramdownloader.Controller;
import ru.nsu.fit.telegramdownloader.DownloaderBot;
import ru.nsu.fit.telegramdownloader.condition.Authorisation;

public class Helper {
    public static void sendHelp(DownloaderBot bot, SendMessage message) throws TelegramApiException {
        message.setText("Бот для прокачивания ссылок и торрентов через телеграм");
        bot.execute(message);
    }
}
