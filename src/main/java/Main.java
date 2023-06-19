import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static Random random = new Random();

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> queue1 = new ArrayBlockingQueue<>(100);
        BlockingQueue<String> queue2 = new ArrayBlockingQueue<>(100);
        BlockingQueue<String> queue3 = new ArrayBlockingQueue<>(100);
        String[] texts = new String[10_000];

        Thread[] threads = new Thread[4];
        threads[0] = new Thread(() -> {
            for (int i = 0; i < texts.length; i++) {
                texts[i] = generateText("abc", 100_000);
            }
        });

        threads[1] = new Thread(() -> {

        });

        threads[2] = new Thread(() -> {

        });

        threads[3] = new Thread(() -> {
            for (String text : texts) {
                char ch = text.charAt(0);
                int j;
                for (j = 1; j < text.length(); j++) {
                    if (ch < text.charAt(j)) {
                        ch = text.charAt(j);
                    } else if (ch > text.charAt(j)) {
                        break;
                    } else if (j == text.length() - 1) {
                        break;
                    }
                }
                if (j == text.length()) {
                    incrementCounts(text.length());
                }
            }
        });

        threads[0].start();
        threads[1].start();
        threads[2].start();
        threads[3].start();

        threads[0].join();
        threads[1].join();
        threads[2].join();
        threads[3].join();
    }

    public static String generateText(String letters, int length) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
