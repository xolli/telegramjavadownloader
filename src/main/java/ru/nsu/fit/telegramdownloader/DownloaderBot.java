package ru.nsu.fit.telegramdownloader;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;

import org.apache.commons.validator.routines.UrlValidator;
import ru.nsu.fit.telegramdownloader.utils.UrlHandler;


public class DownloaderBot extends TelegramLongPollingBot {
    private final UrlHandler urlHandler;

    public DownloaderBot() {
        urlHandler = new UrlHandler();
    }

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            try {
            if (UrlHandler.isUrl(update.getMessage().getText())) {
                    sendMessage("is url. Start download!", update.getMessage().getChatId().toString());
                    urlHandler.downloadFile(update.getMessage().getText());
            } else {
                sendMessage("is not url", update.getMessage().getChatId().toString());
            }
            } catch (TelegramApiException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String text, String chatId) throws TelegramApiException {
        SendMessage message = new SendMessage(chatId, text);
        execute(message);
    }

    @Override
    public String getBotUsername() {
        return "Downloader Bot";
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
        return token.replaceAll("\n","");
    }
}
