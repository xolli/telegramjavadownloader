package ru.nsu.fit.telegramdownloader.utils;

import org.apache.commons.validator.routines.UrlValidator;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlHandler {
    static public boolean isUrl(String text) {
        String[] schemes = {"http","https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        return urlValidator.isValid(text);
    }

    static public String getFileName(String address) throws MalformedURLException {
        URL url = new URL(address);
        String path = url.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }
}
