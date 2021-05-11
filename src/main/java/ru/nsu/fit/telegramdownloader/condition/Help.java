package ru.nsu.fit.telegramdownloader.condition;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nsu.fit.telegramdownloader.Controller;

public class Help extends Condition{
    public Help(Long chatID, Controller controller) {
        super(chatID, controller);
    }
    public void recv(Update update) throws TelegramApiException {
        message.setText("Zdes' mozhno skachivat' some files iz torrent, and drugie explanations");
        super.recv(update);
        controller.setCondition(chatID,this,new Start(chatID,controller));
    }
}
