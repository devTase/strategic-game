package org.hsh.games.aoe.shared.utils;

public class ThreadUtils {

    public static int toMilliseconds(int minutes) {
        return minutes * 60 * 1000;
    }

    public static int toMinutes(int milliseconds) {
        return milliseconds / (60 * 1000);
    }
}
