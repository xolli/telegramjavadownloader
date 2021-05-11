package ru.nsu.fit.telegramdownloader.condition;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nsu.fit.telegramdownloader.Controller;

public class SelectProtocol extends Condition {
    public SelectProtocol(Long chatID, Controller controller) {
        super(chatID, controller);
    }

    public void recv(Update update) throws TelegramApiException {
        String protocol = update.getCallbackQuery().getData();
        System.out.println(protocol+"TORRENT"+protocol.equals("TORRENT"));
        if(protocol.equals("TORRENT")){
            System.out.println("lol");
            message.setText("Ok! downloading by .torrent");

            super.recv(update);
            controller.setCondition(chatID,this,new DownloadTorrent(chatID,controller));
        }
        if(protocol.equals("MAGNET")){
            message.setText("Ok! downloading by magnet-link");
            super.recv(update);
            controller.setCondition(chatID,this,new DownloadMagnet(chatID,controller));
        }

    }
}
