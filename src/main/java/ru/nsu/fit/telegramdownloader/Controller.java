package ru.nsu.fit.telegramdownloader;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nsu.fit.telegramdownloader.condition.Condition;
import ru.nsu.fit.telegramdownloader.condition.Start;

import java.util.HashMap;
import java.util.Map;

public final class Controller {

    private Map<Long, Condition> users;
    private Long chatId;
    private DownloaderBot telegramBot;

    public Controller(){
        users = new HashMap<>();
    }

    public void recvMess(Update update) throws TelegramApiException {
        setChatId(update);

        if(!users.containsKey(chatId)){
            users.put(chatId,new Start(chatId,this));
        }
        users.get(chatId).recv(update);
    }

    public void sendMess(BotApiMethod<Message> messageNew) throws TelegramApiException {
        telegramBot.execute(messageNew);
    }

    public void setBot(DownloaderBot telegramBot){
        this.telegramBot = telegramBot;
    }

    public void setCondition(Long chatID, Condition condition, Condition newCondition){
        users.replace(chatID,condition, newCondition);
    }

    private void setChatId(Update update){
        if(update.hasMessage() && update.getMessage().hasText())
            chatId = update.getMessage().getChatId();
        else
            chatId = update.getCallbackQuery().getMessage().getChatId();
    }
}
