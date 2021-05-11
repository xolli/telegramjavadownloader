package ru.nsu.fit.telegramdownloader.condition;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nsu.fit.telegramdownloader.Controller;
import ru.nsu.fit.telegramdownloader.buttons.InlineKeyboardSelectProtocol;
import ru.nsu.fit.telegramdownloader.utils.UrlHandler;

public class UrlChecker extends Condition {
    public UrlChecker(Long chatID, Controller controller) {
        super(chatID, controller);
    }

    @Override
    public void recv(Update update) throws TelegramApiException {

        if (UrlHandler.isUrl(update.getMessage().getText())) {

            message.setText("is url!. Choose the protocol to download");
            message.setReplyMarkup((new InlineKeyboardSelectProtocol()).getInlineKeyboard());
            //urlHandler.downloadFile(update.getMessage().getText());
            controller.setCondition(chatID,this,new SelectProtocol(chatID,controller));
        } else {
            message.setText("is not url");
        }
        super.recv(update);
    }
}
