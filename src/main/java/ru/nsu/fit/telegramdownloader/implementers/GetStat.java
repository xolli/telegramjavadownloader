package ru.nsu.fit.telegramdownloader.implementers;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nsu.fit.telegramdownloader.DownloaderBot;
import ru.nsu.fit.telegramdownloader.Statistics;
import ru.nsu.fit.telegramdownloader.utils.FilesUtils;

import java.util.HashMap;

public class GetStat {
    public static void sendMyStat(DownloaderBot bot, Statistics stat, Long userId, String chatId) throws TelegramApiException {
        bot.sendMessage("You download " + FilesUtils.humanReadableByteCountBin(stat.getUserStat(userId)), chatId);
    }

    public static void sendAllStat(DownloaderBot bot, Statistics stat, Long userId, String chatId) throws TelegramApiException {
        HashMap<Long, Long> statMap = stat.getStat();
        StringBuilder text = new StringBuilder();
        for (Long user : statMap.keySet()) {
            text.append(user).append(": ").append(FilesUtils.humanReadableByteCountBin(statMap.get(user))).append("\n");
        }
        bot.sendMessage(text.toString(), chatId);
    }
}
