package ru.nsu.fit.telegramdownloader;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.net.MalformedURLException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class DownloaderBot extends TelegramLongPollingBot {
    static Logger LOGGER;
    static {
        try(FileInputStream ins = new FileInputStream("log.config")){
            LogManager.getLogManager().readConfiguration(ins);
            LOGGER = Logger.getLogger(DownloaderBot.class.getName());
        }catch (Exception ignore){
            ignore.printStackTrace();
        }
    }

    private final Controller controller;

    public DownloaderBot() {
        controller = new Controller();
        controller.setBot(this);
        LOGGER.log(Level.INFO, "Init bot");
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            try {
                controller.recvMess(update);
            } catch (TelegramApiException | MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    public Message sendMessage(String text, String chatId) throws TelegramApiException {
        SendMessage message = new SendMessage(chatId, text);
        return execute(message);
    }
    public Message sendMessage(SendMessage message,String text, String chatId) throws TelegramApiException {
        message.setText(text);
        message.setChatId(chatId);
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
            File tokenFile = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("token.txt")).getFile());
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

    public void stop() {
        try {
            controller.closeStat();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClosing() {
        super.onClosing();
        LOGGER.log(Level.INFO, "Stop bot");
    }
}
