package ru.nsu.fit.telegramdownloader;

import ru.nsu.fit.telegramdownloader.utils.FilesUtils;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Statistics implements AutoCloseable {
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    static Logger LOGGER;
    static {
        try(FileInputStream ins = new FileInputStream("log.config")){
            LogManager.getLogManager().readConfiguration(ins);
            LOGGER = Logger.getLogger(Statistics.class.getName());
        }catch (Exception ignore){
            ignore.printStackTrace();
        }
    }
    private final HashMap<Long, Long> data;

    public Statistics() {
        data = FilesUtils.readMapFileLL("stat.txt");
    }

    public Long getUserStat(Long userId) {
        lock.readLock().lock();
        try {
            return data.getOrDefault(userId, 0L);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void updateUserStat(Long userId, Long downloadData) {
        lock.writeLock().lock();
        try {
            data.put(userId, data.getOrDefault(userId, 0L) + downloadData);
        } finally {
            lock.writeLock().unlock();
        }
    }

    // https://stackoverflow.com/questions/50257374/how-do-i-write-multiple-lines-to-a-text-file-in-java
    @Override
    public void close() throws Exception {
        FileWriter fStream = new FileWriter("stat.txt", false);
        BufferedWriter info = new BufferedWriter(fStream);
        try {
            for (Map.Entry<Long, Long> user : data.entrySet()) {
                info.write(user.getKey() + ":" + user.getValue() + "\n");
            }
        } finally {
            info.close();
            fStream.close();
        }
    }

    public HashMap<Long, Long> getStat() {
        synchronized (data) {
            return new HashMap<>(data);
        }
    }
}
