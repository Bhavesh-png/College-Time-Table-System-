package com.college.service;

import com.college.model.TimetableEntry;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimetableService {
    private static List<TimetableEntry> list = Collections.synchronizedList(new ArrayList<>());
    private static final String FILE = "timetable.dat";

    static {
        load();
    }

    @SuppressWarnings("unchecked")
    private static synchronized void load() {
        File f = new File(FILE);
        if (!f.exists()) {
            list = Collections.synchronizedList(new ArrayList<>());
            return;
        }
        try (ObjectInputStream o = new ObjectInputStream(new FileInputStream(f))) {
            List<TimetableEntry> loaded = (List<TimetableEntry>) o.readObject();
            list = Collections.synchronizedList(new ArrayList<>(loaded));
        } catch (Exception e) {
            list = Collections.synchronizedList(new ArrayList<>());
        }
    }

    public static synchronized boolean isSlotAvailable(String day, String start) {
        for (TimetableEntry e : list) {
            if (e.getDay().equalsIgnoreCase(day) && e.getStart().equals(start)) {
                return false;
            }
        }
        return true;
    }

    public static synchronized boolean addEntry(TimetableEntry t) {
        if (!isSlotAvailable(t.getDay(), t.getStart())) {
            return false;
        }
        list.add(t);
        save();
        return true;
    }

    public static synchronized void deleteEntry(int i) {
        if (i >= 0 && i < list.size()) {
            list.remove(i);
            save();
        }
    }

    public static List<TimetableEntry> getAllEntries() {
        synchronized(list) {
            return new ArrayList<>(list); // Return a copy to avoid ConcurrentModificationException
        }
    }

    public static synchronized void save() {
        try (ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(FILE))) {
            o.writeObject(new ArrayList<>(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
