package one.opensource.util;

public class ExecuteUtil {

    public static void blockMills(long mills) throws InterruptedException {
        Thread.sleep(mills);
    }

    public static void blockRandomTeenMills() throws InterruptedException {
        blockMills((int) Math.random() * 100 + 2);
    }

    public static void blockRandomHundredMills() throws InterruptedException {
        blockMills((int) Math.random() * 1000 + 20);
    }

    public static void throwRandomException(float prob) {
        if (Math.random() > 1 - prob) {
            throw new RuntimeException("random failure");
        }
    }

    public static void randomBlockTeenMills(float prob) throws InterruptedException {
        if (Math.random() > 1 - prob)
            blockRandomTeenMills();
    }

    public static void randomBlockHundredMills(float prob) throws InterruptedException {
        if (Math.random() > 1 - prob)
            blockRandomHundredMills();
    }
}
