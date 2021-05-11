package ru.nsu.fit.telegramdownloader.condition;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nsu.fit.telegramdownloader.Controller;

public class Start extends Condition {

    public Start(Long chatID, Controller controller) {
        super(chatID, controller);
    }

    @Override
    public void recv(Update update) throws TelegramApiException {
        message.setText("Hi! type URL for downloading file");
        super.recv(update);
        controller.setCondition(chatID,this,new UrlChecker(chatID,controller));
    }
}
