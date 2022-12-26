import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();

        Thread threadPrintMax = new Thread(() -> {
            while (!Thread.interrupted()) {
                synchronized (sizeToFreq) {
                    try {
                        sizeToFreq.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                    printMaxValue();
                }
            }
        });

        threadPrintMax.start();

        for (int i = 0; i < 1000; i++) {
            threads.add(new Thread(() -> {
                synchronized (sizeToFreq) {
                    String way = generateRoute("RLRFR", 100);
                    int cnt = getCountLetter(way);
                    sizeToFreq.merge(cnt, 1, Integer::sum);
                    sizeToFreq.notify();
                }
            }));
        }

        for (Thread thread : threads) {
            thread.start();
            Thread.sleep(1);
        }

        for (Thread thread : threads) {
            thread.join();
        }

        threadPrintMax.interrupt();

        sizeToFreq.forEach((key, value) -> System.out.println("- " + key + " (" + value + " раз)"));
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static int getCountLetter(String way) {
        int cnt = 0;
        for (char symbol : way.toCharArray()) {
            if (symbol == 'R')
                cnt++;
        }
        return cnt;
    }

    public static int printMaxValue() {
        Map.Entry<Integer, Integer> maxValue = null;
        for (Map.Entry<Integer, Integer> price : sizeToFreq.entrySet()) {
            if (maxValue == null || price.getValue().compareTo(maxValue.getValue()) > 0) {
                maxValue = price;
            }
        }
        System.out.println("Самое частое количество повторений " + Objects.requireNonNull(maxValue).getKey() +
                " (встретилось " + maxValue.getValue() + " раз) ");
        return maxValue.getValue();
    }
}