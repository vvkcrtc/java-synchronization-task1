import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<Integer, Integer>();

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                String route = generateRoute("RLRFR", 100);
                int occurrencesCount = (int) route.chars().filter(ch -> ch == 'R').count();
                synchronized (sizeToFreq) {
                    if (occurrencesCount >= 60) {
                        if (sizeToFreq.containsKey(occurrencesCount)) {
                            int tmp = sizeToFreq.get(occurrencesCount);
                            sizeToFreq.put((Integer) occurrencesCount, tmp + 1);
                        } else {
                            sizeToFreq.put(occurrencesCount, 1);
                        }
                    }

                }
            }).start();
        }

        new Thread(() -> {
            synchronized (sizeToFreq) {
                int maxCount = 0;
                int maxValue = 0;
                for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
                    if (maxCount <= entry.getValue()) {
                        maxCount = entry.getValue();
                        maxValue = entry.getKey();
                    }
                }
                System.out.println("Самое частое количество повторений " + maxValue + " (встретилось " + maxCount + " раз)");
                System.out.println("Другие размеры:");
                for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
                    System.out.println(" - " + entry.getKey() + " ( " + entry.getValue() + " раз )");
                }
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                return;
            }

        }).start();
    }
}
