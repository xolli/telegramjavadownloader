package ru.nsu.fit.telegramdownloader.condition;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nsu.fit.telegramdownloader.Controller;
import ru.nsu.fit.telegramdownloader.buttons.Keyboard;

import java.net.MalformedURLException;

public abstract class Condition {
    protected Long chatID;
    protected Controller controller;
    protected SendMessage message;
    protected Update update;


    public Condition(Long chatID, Controller controller){
        this.chatID = chatID;
        this.controller = controller;
        message = new SendMessage();
    }

    public void send() throws TelegramApiException {
        message.setChatId(chatID.toString());
        controller.sendMess(message);
    }

    public abstract void recv(Update update) throws TelegramApiException, MalformedURLException;
}
