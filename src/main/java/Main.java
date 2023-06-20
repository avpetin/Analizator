import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static Random random = new Random();

    public static void main(String[] args) throws InterruptedException {
        final int CAPACITY = 100;
        BlockingQueue<String> queue1 = new ArrayBlockingQueue<>(CAPACITY);
        BlockingQueue<String> queue2 = new ArrayBlockingQueue<>(CAPACITY);
        BlockingQueue<String> queue3 = new ArrayBlockingQueue<>(CAPACITY);
        AtomicInteger count_a = new AtomicInteger(0);
        AtomicInteger count_b = new AtomicInteger(0);
        AtomicInteger count_c = new AtomicInteger(0);
        AtomicInteger count = new AtomicInteger(0);
        String[] texts = new String[10_000];

        Thread[] threads = new Thread[4];
        threads[0] = new Thread(() -> {
            for (int i = 0; i < texts.length; i++) {
                texts[i] = generateText("abc", 100_000);
                try {
                    queue1.put(texts[i]);
                    queue2.put(texts[i]);
                    queue3.put(texts[i]);
                    count.incrementAndGet();  // для наглядности, что программа не висит
                } catch (InterruptedException e) {
                    return;
                }
            }
        });

        threads[1] = new Thread(() -> {
            while (count.get() < texts.length || queue1.size() > 0){
                for (int i = 0; i < queue1.size(); i++) {
                    try {
                        String s = queue1.take();
                        String[] a_Arr = s.split("a");
                        count_a.addAndGet(a_Arr.length);
                        System.out.println(count.get());
                    } catch (InterruptedException e) {
                        return;
                    }
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        threads[2] = new Thread(() -> {
            while (count.get() < texts.length || queue2.size() > 0) {
                for (int i = 0; i < queue2.size(); i++) {
                    try {
                        String s = queue2.take();
                        String[] b_Arr = s.split("b");
                        count_b.addAndGet(b_Arr.length);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        threads[3] = new Thread(() -> {
            while (count.get() < texts.length || queue3.size() > 0) {
                for (int i = 0; i < queue3.size(); i++) {
                    try {
                        String s = queue3.take();
                        String[] c_Arr = s.split("c");
                        count_c.addAndGet(c_Arr.length);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
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

        System.out.println("Количество символов a " + count_a.get());
        System.out.println("Количество символов b " + count_b.get());
        System.out.println("Количество символов c " + count_c.get());
    }

    public static String generateText(String letters, int length) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
