package ru.nsu.fit.telegramdownloader;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

public class AuthorisationUtils {
    private HashSet<Long> admins;
    private HashMap<String, Long> tokens;// token -> user

    private AuthorisationUtils() {
        admins = getAdmins();
        tokens = getTokens();
    }

    public boolean isAdmin(Long userId) {
        return admins.contains(userId);
    }

    public boolean trustedUser(Long userId) {
        return tokens.containsValue(userId);
    }

    private HashSet<Long> getAdmins() {
        HashSet<Long> admins = new HashSet<>();
        BufferedReader br;
        try {
            File adminsFile = new File(getClass().getClassLoader().getResource("adminlist.txt").getFile());
            br = new BufferedReader(new FileReader(adminsFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return admins;
        }
        try {
            String line = br.readLine();
            while (line != null) {
                admins.add(Long.parseLong(line));
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return admins;
    }

    private HashMap<String, Long> getTokens() {
        HashMap<String, Long> tokens = new HashMap<>();
        BufferedReader br;
        try {
            File tokensFile = new File(getClass().getClassLoader().getResource("tokenlist.txt").getFile());
            br = new BufferedReader(new FileReader(tokensFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return tokens;
        }
        try {
            String line = br.readLine();
            while (line != null) {
                tokens.put(line.substring(0, line.indexOf(':')), Long.parseLong(line.substring(line.indexOf(':') + 1)));
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tokens;
    }

    private static class AuthorisationUtilsHolder {
        private final static AuthorisationUtils instance = new AuthorisationUtils();
    }

    public static AuthorisationUtils getInstance() {
        return AuthorisationUtilsHolder.instance;
    }


}
