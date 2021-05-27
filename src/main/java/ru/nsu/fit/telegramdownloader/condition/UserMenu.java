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
import java.util.HashSet;

public class UserMenu extends Condition {
    private KeyboardUserMenu keyboard;
    private final HashSet<TorrentDownloader> downloadTorrents;
    private final HashSet<UrlDownloader> downloadUrl;
    public UserMenu(Long chatID, Controller controller) throws TelegramApiException {
        super(chatID, controller);
        keyboard = new KeyboardUserMenu();
        message.setReplyMarkup(keyboard.getKeyboard());
        message.setText("Authorization was successful");
        super.send();
        downloadTorrents = new HashSet<>();
        downloadUrl = new HashSet<>();
    }

    @Override
    public void recv(Update update) throws TelegramApiException, MalformedURLException {
        this.update = update;
        checkContent();
        message.setReplyMarkup(keyboard.getKeyboard());
    }

    protected void checkContent() throws TelegramApiException, MalformedURLException {
        checkTorrentDocument();
        checkButtons();
        checkURL();
        checkMagnetLink();
    }

    private void checkTorrentDocument() throws TelegramApiException {
        if (update.getMessage().hasDocument()){
            String torrentFileName = update.getMessage().getDocument().getFileName();
            if (!FilenameUtils.getExtension(torrentFileName).equals("torrent")) {
                message.setText("This file is not a torrent-file");
                return;
            }
            TorrentDownloader newTorrent = new TorrentDownloader(update.getMessage().getDocument(), torrentFileName,
                    controller.getBot(), chatID.toString(), controller.getStat(), chatID, keyboard);
            downloadTorrents.add(newTorrent);
            Thread torrentThread = new Thread(newTorrent);
            torrentThread.start();
        }
    }

    private void checkMagnetLink() throws TelegramApiException {
        if (update.getMessage().hasText() &&
                TorrentDownloader.validateMagnetLink(update.getMessage().getText())) {
            TorrentDownloader newTorrent = new TorrentDownloader(update.getMessage().getText(), controller.getBot(),
                    chatID.toString(), controller.getStat(), chatID, keyboard);
            downloadTorrents.add(newTorrent);
            Thread torrentThread = new Thread(newTorrent);
            torrentThread.start();
        }
    }

    private void checkURL() throws MalformedURLException, TelegramApiException {
        if(update.getMessage().hasText() && UrlHandler.isUrl(update.getMessage().getText())){
            UrlDownloader newUrl = new UrlDownloader(chatID.toString(),
                    update.getMessage().getText(), controller.getBot(), controller.getStat(), update.getMessage().getFrom().getId(), keyboard);
            downloadUrl.add(newUrl);
            Thread downloadThread = new Thread(newUrl);
            downloadThread.start();
        }
    }

    private void checkButtons() throws TelegramApiException {
        if(update.getMessage().hasText()){
            String pressedButton = update.getMessage().getText();
            if (pressedButton.equals(KeyboardUserMenu.STATS) || "/stat".equals(pressedButton)){
                GetStat.sendMyStat(controller.getBot(),controller.getStat(),update.getMessage().getFrom().getId(),message);
            } else if(pressedButton.equals(KeyboardUserMenu.HELP)){
                Helper.sendHelp(controller.getBot(),message);
            } else if (pressedButton.equals(KeyboardUserMenu.STOP_ALL)) {
                for (TorrentDownloader torrent : downloadTorrents) {
                    torrent.stopDownload();
                }
                downloadTorrents.clear();
                for (UrlDownloader urlDownloader : downloadUrl) {
                    urlDownloader.stop();
                }
                downloadUrl.clear();
            }
        }
    }
}
