package ru.nsu.fit.telegramdownloader.condition;

import ru.nsu.fit.telegramdownloader.Controller;

public class DownloadTorrent extends Condition {
    public DownloadTorrent(Long chatID, Controller controller) {
        super(chatID, controller);
        download();
    }


    private void download(){

    }

}
