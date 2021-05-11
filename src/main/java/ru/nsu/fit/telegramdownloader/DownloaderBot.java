package ru.nsu.fit.telegramdownloader;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.*;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nsu.fit.telegramdownloader.utils.UrlHandler;


public class DownloaderBot extends TelegramLongPollingBot {
    private final UrlHandler urlHandler;
    private final Controller controller;
    public DownloaderBot(Controller controller) {
        this.controller = controller;
        urlHandler = new UrlHandler();
    }

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if ((update.hasMessage() && update.getMessage().hasText()) || update.hasCallbackQuery()) {
            try {

                controller.recvMess(update);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            /*
            try {

            if (UrlHandler.isUrl(update.getMessage().getText())) {

                    sendMessage("is url. Start download!", update.getMessage().getChatId().toString());

                    //urlHandler.downloadFile(update.getMessage().getText());
            } else {
                sendMessage("is not url", update.getMessage().getChatId().toString());
            }


            } catch (TelegramApiException | IOException e) {
                e.printStackTrace();
            }*/
        }
    }

    /*public void sendMessage(String text, String chatId) throws TelegramApiException {
        SendMessage message = new SendMessage(chatId, text);
        message.setReplyMarkup((new InlineKeyboardSelectProtocol()).getInlineKeyboard());
        execute(message);
    }*/

    @Override
    public String getBotUsername() {
        return "MasterDownloaderBot";
    }

    // https://stackoverflow.com/questions/4716503/reading-a-plain-text-file-in-java
    @Override
    public String getBotToken() {
        String token = null;
        BufferedReader br;
        try {
            File tokenFile = new File(getClass().getClassLoader().getResource("token.txt").getFile());
            br = new BufferedReader(new FileReader(tokenFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            token = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        assert token != null;
        return token.replaceAll("\r?\n","");
    }
}
