package io.bidmachine;

public class Utils {

    public static int getOrDefault(int target, int targetDefault, int def) {
        return target == targetDefault ? def : target;
    }

    public static long getOrDefault(long target, long targetDefault, long def) {
        return target == targetDefault ? def : target;
    }

    public static float getOrDefault(float target, float targetDefault, float def) {
        return target == targetDefault ? def : target;
    }

}
