import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Map<Integer, Integer> sizeToFreq = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                synchronized (sizeToFreq) {
                    String way = generateRoute("RLRFR", 100);
                    int cnt = getCountLetter(way);
                    sizeToFreq.merge(cnt, 1, Integer::sum);
                }
            }).start();
        }

        int maxValue = printMaxValue(sizeToFreq);
        sizeToFreq.forEach((key, value) -> {
            if (!value.equals(maxValue))
                System.out.println("- " + key + " (" + value + " раз)");
        });
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

    public static int printMaxValue(Map<Integer, Integer> sizeToFreq) {
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