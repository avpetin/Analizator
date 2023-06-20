import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    static Random random = new Random();
    static String[] texts = new String[10_000];
    static int countA = 0;
    static int countB = 0;
    static int countC = 0;

    public static void main(String[] args) throws InterruptedException {
        final int CAPACITY = 100;

        BlockingQueue<String> queue1 = new ArrayBlockingQueue<>(CAPACITY);
        BlockingQueue<String> queue2 = new ArrayBlockingQueue<>(CAPACITY);
        BlockingQueue<String> queue3 = new ArrayBlockingQueue<>(CAPACITY);

        Thread[] threads = new Thread[4];
        threads[0] = new Thread(() -> {
            for (int i = 0; i < texts.length; i++) {
                texts[i] = generateText("abc", 100_000);
                try {
                    queue1.put(texts[i]);
                    queue2.put(texts[i]);
                    queue3.put(texts[i]);
                } catch (InterruptedException e) {
                    return;
                }
            }
        });

        threads[1] = new Thread(() -> {
            countA = countSymbols(queue1, countA, "a");
        });

        threads[2] = new Thread(() -> {
            countB = countSymbols(queue2, countB, "b");
        });

        threads[3] = new Thread(() -> {
            countC = countSymbols(queue3, countC, "c");
        });

        for(Thread thread : threads){
            thread.start();
        }

        for(Thread thread : threads){
            thread.join();
        }

        System.out.println("Количество символов a " + countA);
        System.out.println("Количество символов b " + countB);
        System.out.println("Количество символов c " + countC);
    }

    public static String generateText(String letters, int length) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int countSymbols(BlockingQueue<String> queue, int count, String symb) {
        while (count < texts.length || queue.size() > 0) {
            for (int i = 0; i < queue.size(); i++) {
                try {
                    String s = queue.take();
                    String[] arr = s.split(symb);
                    count += arr.length;
                } catch (InterruptedException e) {
                    return 0;
                }
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                return 0;
            }
        }
        return count;
    }
}
