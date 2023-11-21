package ru.melnik;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static final AtomicInteger countChar3 = new AtomicInteger(0);
    private static final AtomicInteger countChar4 = new AtomicInteger(0);
    private static final AtomicInteger countChar5 = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        List<Thread> threads = new ArrayList<>();

        Runnable runnableChar3 = () -> Arrays.stream(texts)
            .filter(t -> t.length() == 3)
            .filter(t -> t.equals("aaa") || t.equals("bbb") || t.equals("ccc"))
            .forEach(i -> countChar3.getAndIncrement());
        threads.add(new Thread(runnableChar3));

        Runnable runnableChar4 = () -> Arrays.stream(texts)
                    .filter(t -> t.length() == 4)
                    .filter(t -> t.equals("abba") || t.equals("baab") || t.equals("caac") || t.equals("acca") || t.equals("cbbc") || t.equals("bccb"))
                    .forEach(i -> countChar4.getAndIncrement());
        threads.add(new Thread(runnableChar4));

        Runnable runnableChar5 = () -> { for (String s :texts) {
                if (s.length() == 5) {
                    int count = getCharCount(s);
                    if (count == 5) countChar5.getAndIncrement();
                }
            }
        };
        threads.add(new Thread(runnableChar5));

        for (Thread thread : threads) {
            thread.start();
            thread.join();
        }

        System.out.println();
        System.out.println("Красивых слов с длиной 3: " + countChar4 + " шт");
        System.out.println("Красивых слов с длиной 4: " + countChar3 + " шт");
        System.out.println("Красивых слов с длиной 5: " + countChar5 + " шт");

        }

    private static int getCharCount(String s) {
        int count = 0;
        for (int j = 0; j < s.length(); j++) {
            if (j == 4) {
                if ((int) s.charAt(j) >= (int) s.charAt(j-1)) {
                    count++;
                }
            } else if (j == 0) {
                if ((int) s.charAt(j) <= (int) s.charAt(j+1)) {
                    count++;
                }
            } else {
                if ((int) s.charAt(j) <= (int) s.charAt(j+1) && (int) s.charAt(j) >= (int) s.charAt(j-1)) {
                    count++;
                }
            }
        }
        return count;
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

}
