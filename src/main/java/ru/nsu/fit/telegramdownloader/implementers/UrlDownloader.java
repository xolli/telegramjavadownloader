package ru.nsu.fit.telegramdownloader.implementers;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nsu.fit.telegramdownloader.DownloaderBot;
import ru.nsu.fit.telegramdownloader.Statistics;
import ru.nsu.fit.telegramdownloader.exceptions.StopDownloadingException;
import ru.nsu.fit.telegramdownloader.buttons.Keyboard;
import ru.nsu.fit.telegramdownloader.utils.FilesUtils;
import ru.nsu.fit.telegramdownloader.utils.UrlHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

public class UrlDownloader extends StatusUpdater implements Runnable {
    private final String DOWNLOAD_FOLDER = "downloadTelegramBot";
    private final String url;
    private final long fileSize;
    private final Statistics stat;
    private final Long userId;
    private final AtomicBoolean downloading;
    private BufferedInputStream inputDownloading;

    public UrlDownloader(String chatId, String url, DownloaderBot bot, Statistics stat, Long userId, Keyboard keyboard)
            throws TelegramApiException, MalformedURLException {
        super("Wait...", bot, chatId);
        this.url = url;
        FilesUtils.mkDir(DOWNLOAD_FOLDER);
        fileSize = getFileSize(new URL(url));
        this.stat = stat;
        this.userId = userId;
        downloading = new AtomicBoolean(false);
    }

    @Override
    public void run() {
        String filename = null;
        try {
            filename = DownloadFile();
            downloading.set(true);
            updateStatus("Done!");
            uploadFile(filename);
            stat.updateUserStat(userId, FilesUtils.getFileSize(filename));
        } catch (IOException e) {
            try {
                updateStatus(e.getMessage());
            } catch (TelegramApiException telegramApiException) {
                telegramApiException.printStackTrace();
            }
        } catch (TelegramApiException | StopDownloadingException e) {
            e.printStackTrace();
        } finally {
            if (filename != null && !filename.endsWith("/")) {
                try {
                    Files.delete(Paths.get(filename));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String DownloadFile() throws IOException, TelegramApiException, StopDownloadingException {
        String filename = UrlHandler.getFileName(url);
        inputDownloading =  new BufferedInputStream(new URL(url).openStream());
        try (FileOutputStream fileOutputStream = new FileOutputStream(DOWNLOAD_FOLDER + '/' + filename)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            long countBytesRead = 0;
            long oldProgressBytes = 0;
            long oldPercent = 0;
            while ((bytesRead = inputDownloading.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
                countBytesRead += bytesRead;
                String newSize = FilesUtils.humanReadableByteCountBin(countBytesRead);
                if (fileSize != -1 && oldPercent > (countBytesRead / fileSize * 100)) {
                    updateStatus((countBytesRead / fileSize * 100) + "% downloaded from file \"" + filename + "\"");
                    oldPercent = (countBytesRead / fileSize * 100);
                } else if (countBytesRead - oldProgressBytes > 1024 * 1024 * 3) {
                    updateStatus(newSize + " downloaded from file \"" + filename + "\"");
                    oldProgressBytes = countBytesRead;
                }
            }
        } catch (IOException ex) {
            Files.delete(Paths.get(DOWNLOAD_FOLDER + '/' + UrlHandler.getFileName(url)));
            throw new StopDownloadingException();
        } finally {
            inputDownloading.close();
        }
        updateStatus("File " + UrlHandler.getFileName(url) + " downloaded! Start upload file");
        return DOWNLOAD_FOLDER + '/' + UrlHandler.getFileName(url);
    }

    // https://stackoverflow.com/questions/12800588/how-to-calculate-a-file-size-from-url-in-java
    private static long getFileSize(URL url) {
        URLConnection conn = null;
        try {
            conn = url.openConnection();
            if(conn instanceof HttpURLConnection) {
                ((HttpURLConnection)conn).setRequestMethod("HEAD");
            }
            conn.getInputStream();
            return conn.getContentLengthLong();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if(conn instanceof HttpURLConnection) {
                ((HttpURLConnection)conn).disconnect();
            }
        }
    }

    public void stop() throws TelegramApiException {
        if (!downloading.get() && inputDownloading != null) {
            try {
                inputDownloading.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            updateStatus("Stop downloading");
        }
    }
}
