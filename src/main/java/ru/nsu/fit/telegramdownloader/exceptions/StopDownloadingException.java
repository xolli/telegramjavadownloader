package ru.nsu.fit.telegramdownloader.exceptions;

public class StopDownloadingException extends Exception {
    public StopDownloadingException() {
        super("User stop downloading");
    }
    public StopDownloadingException(Exception e) { super (e); }
}
