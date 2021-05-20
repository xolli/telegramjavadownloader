package ru.nsu.fit.telegramdownloader.condition;

import org.apache.commons.io.FilenameUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nsu.fit.telegramdownloader.Controller;
import ru.nsu.fit.telegramdownloader.buttons.KeyboardUserMenu;
import ru.nsu.fit.telegramdownloader.implementers.GetStat;
import ru.nsu.fit.telegramdownloader.implementers.Helper;
import ru.nsu.fit.telegramdownloader.implementers.TorrentDownloader;
import ru.nsu.fit.telegramdownloader.implementers.UrlDownloader;
import ru.nsu.fit.telegramdownloader.utils.UrlHandler;

import java.net.MalformedURLException;

public class UserMenu extends Condition {
    private KeyboardUserMenu keyboard;
    public UserMenu(Long chatID, Controller controller) throws TelegramApiException {
        super(chatID, controller);
        keyboard = new KeyboardUserMenu();
        message.setReplyMarkup(keyboard.getKeyboard());
        message.setText("authorization was successful");
        super.send();
    }


    public void recv(Update update) throws TelegramApiException, MalformedURLException {
        this.update = update;
        message.setReplyMarkup(keyboard.getKeyboard());
        checkTorrentDocument();
        checkButtons();
        checkURL();
    }


    private void checkTorrentDocument() throws TelegramApiException {
        if (update.getMessage().hasDocument()){
            System.out.println("noooooo");
            String torrentFileName = update.getMessage().getDocument().getFileName();
            if (!FilenameUtils.getExtension(torrentFileName).equals("torrent")) {
                message.setText("This file is not a torrent-file");
                return;
            }

            Thread torrentThread = new Thread(new TorrentDownloader(update.getMessage().getDocument(), torrentFileName,
                controller.getBot(), chatID.toString(), controller.getStat(), chatID));
            torrentThread.start();
        }
    }

    private void checkURL() throws MalformedURLException, TelegramApiException {
        if(update.getMessage().hasText() && UrlHandler.isUrl(update.getMessage().getText())){
            Thread downloadThread = new Thread(new UrlDownloader(chatID.toString(),
                update.getMessage().getText(), controller.getBot(), controller.getStat(), update.getMessage().getFrom().getId()));
            downloadThread.start();
        }

    }


    private void checkButtons() throws TelegramApiException {
        if(update.getMessage().hasText()){

            String pressedButton = update.getMessage().getText();
            if(pressedButton.equals(KeyboardUserMenu.STATS) || "/stat".equals(pressedButton)){
                GetStat.sendMyStat(controller.getBot(),controller.getStat(),update.getMessage().getFrom().getId(),message);
            } else if(pressedButton.equals(KeyboardUserMenu.HELP)){
                Helper.sendHelp(controller.getBot(),message);
            }
        }
    }


}
