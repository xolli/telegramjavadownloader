package ru.nsu.fit.telegramdownloader.implementers;

import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nsu.fit.telegramdownloader.DownloaderBot;
import ru.nsu.fit.telegramdownloader.utils.FilesUtils;
import ru.nsu.fit.telegramdownloader.utils.UrlHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class UrlDownloader implements Runnable {
    private final String DOWNLOAD_FOLDER = "downloadTelegramBot";
    private final long FILE_LIMIT = 50 * 1024 * 1024;
    private final Integer statusMessageId;
    private final String url;
    private final String chatId;
    private final DownloaderBot bot;
    private final long fileSize;

    public UrlDownloader(String chatId, String url, DownloaderBot bot) throws TelegramApiException, MalformedURLException {
        this.url = url;
        this.bot = bot;
        statusMessageId = bot.sendMessage("Wait...", chatId).getMessageId();
        this.chatId = chatId;
        FilesUtils.mkDir(DOWNLOAD_FOLDER);
        fileSize = getFileSize(new URL(url));
    }

    @Override
    public void run() {
        String filename = null;
        try {
            filename = DownloadFile();
            uploadFile(filename);
        } catch (IOException e) {
            try {
                updateStatus(e.getMessage());
            } catch (TelegramApiException telegramApiException) {
                telegramApiException.printStackTrace();
            }
        } catch (TelegramApiException e) {
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

    private String DownloadFile() throws IOException, TelegramApiException {
        String filename = UrlHandler.getFileName(url);
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(DOWNLOAD_FOLDER + '/' + filename)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            long countBytesRead = 0;
            long oldProgressBytes = 0;
            long oldPercent = 0;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
                countBytesRead += bytesRead;
                String newSize = humanReadableByteCountBin(countBytesRead);
                if (fileSize != -1 && oldPercent > (countBytesRead / fileSize * 100)) {
                    updateStatus( (countBytesRead / fileSize * 100) + "% downloaded from file \"" + filename + "\"");
                    oldPercent = (countBytesRead / fileSize * 100);
                } else if (countBytesRead - oldProgressBytes > 1024 * 1024 * 3) {
                    updateStatus( newSize + " downloaded from file \"" + filename + "\"");
                    oldProgressBytes = countBytesRead;
                }
            }
        }
        updateStatus("File " + UrlHandler.getFileName(url) + " downloaded! Start upload file");
        return DOWNLOAD_FOLDER + '/' + UrlHandler.getFileName(url);
    }

    // https://stackoverflow.com/questions/3758606/how-can-i-convert-byte-size-into-a-human-readable-format-in-java
    private static String humanReadableByteCountBin(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " B";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format("%.1f %ciB", value / 1024.0, ci.current());
    }

    private void uploadFile(String filename) throws TelegramApiException, IOException {
        if (FilesUtils.getFileSize(filename) < FILE_LIMIT) {
            _uploadFile(filename);
        } else {
            uploadSplitFiles(filename);
        }
    }

    // https://stackoverflow.com/questions/19177994/java-read-file-and-split-into-multiple-files
    private void uploadSplitFiles(String filename) throws IOException, TelegramApiException {
        RandomAccessFile raf = new RandomAccessFile(filename, "r");
        long numSplits = FilesUtils.getFileSize(filename) / FILE_LIMIT + 1;
        long sourceSize = raf.length();
        long bytesPerSplit = sourceSize/numSplits ;
        long remainingBytes = sourceSize % numSplits;

        int maxReadBufferSize = 8 * 1024; // 8KB
        for(int destIx = 1; destIx <= numSplits; destIx++) {
            BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(filename + "_" + destIx));
            if(bytesPerSplit > maxReadBufferSize) {
                long numReads = bytesPerSplit / maxReadBufferSize;
                long numRemainingRead = bytesPerSplit % maxReadBufferSize;
                for(int i = 0; i < numReads; i++) {
                    readWrite(raf, bw, maxReadBufferSize);
                }
                if(numRemainingRead > 0) {
                    readWrite(raf, bw, numRemainingRead);
                }
            } else {
                readWrite(raf, bw, bytesPerSplit);
            }
            bw.close();
            _uploadFile(filename + "_" + destIx);
            Files.delete(Paths.get(filename + "_" + destIx));
        }
        if(remainingBytes > 0) {
            BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream("split."+(numSplits+1)));
            readWrite(raf, bw, remainingBytes);
            bw.close();
        }
        raf.close();
    }

    static void readWrite(RandomAccessFile raf, BufferedOutputStream bw, long numBytes) throws IOException {
        byte[] buf = new byte[(int) numBytes];
        int val = raf.read(buf);
        if(val != -1) {
            bw.write(buf);
        }
    }

    private void _uploadFile(String filename) throws TelegramApiException {
        SendDocument sendFile = new SendDocument(chatId, new InputFile(new File(filename)));
        sendFile.setReplyToMessageId(statusMessageId);
        bot.execute(sendFile);
    }

    private void updateStatus(String text) throws TelegramApiException {
        EditMessageText editMessageText = new EditMessageText(text);
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(statusMessageId);
        bot.execute(editMessageText);
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
}
