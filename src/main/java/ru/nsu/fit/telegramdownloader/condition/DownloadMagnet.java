package ru.nsu.fit.telegramdownloader.condition;

import ru.nsu.fit.telegramdownloader.Controller;

public class DownloadMagnet extends Condition {
    public DownloadMagnet(Long chatID, Controller controller) {
        super(chatID, controller);
    }
}
