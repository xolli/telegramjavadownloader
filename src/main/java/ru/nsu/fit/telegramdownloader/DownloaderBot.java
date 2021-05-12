package ru.nsu.fit.telegramdownloader;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.net.MalformedURLException;

import ru.nsu.fit.telegramdownloader.implementers.UrlDownloader;
import ru.nsu.fit.telegramdownloader.utils.UrlHandler;


public class DownloaderBot extends TelegramLongPollingBot {
    private final UrlHandler urlHandler;
    private final AuthorisationUtils authorisationUtils;

    public DownloaderBot() {
        urlHandler = new UrlHandler();
        authorisationUtils = AuthorisationUtils.getInstance();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            try {
            if (UrlHandler.isUrl(update.getMessage().getText()) && authorisationUtils.trustedUser(update.getMessage().getFrom().getId())) {
                    Thread downloadThread = new Thread(new UrlDownloader(update.getMessage().getChatId().toString(), update.getMessage().getText(), this));
                    downloadThread.start();
            } else if (!authorisationUtils.trustedUser(update.getMessage().getFrom().getId())) {
                sendMessage("You are not trusted user", update.getMessage().getChatId().toString());
            } else {
                sendMessage("is not url", update.getMessage().getChatId().toString());
            }
            } catch (TelegramApiException | MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }



    public Message sendMessage(String text, String chatId) throws TelegramApiException {
        SendMessage message = new SendMessage(chatId, text);
        return execute(message);
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
