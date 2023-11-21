package ru.melnik;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static final AtomicInteger nickname1 = new AtomicInteger(0);
    private static final AtomicInteger nickname2 = new AtomicInteger(0);
    private static final AtomicInteger nickname3 = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        List<Thread> threads = new ArrayList<>();

        Runnable runnable1 = () -> Arrays.stream(texts).forEach(i -> {
            if (i.length() == 3 && isPalindrome(i)) {
                nickname1.getAndIncrement();
            } else if (i.length() == 4 && isPalindrome(i)) {
                nickname2.getAndIncrement();
            } else if (i.length() == 5 && isPalindrome(i)) {
                nickname3.getAndIncrement();
            }
        });
        threads.add(new Thread(runnable1));

        Runnable runnable2 = () -> Arrays.stream(texts).forEach(i -> {
            if (i.length() == 3 && isEquals(i)) {
                nickname1.getAndIncrement();
            } else if (i.length() == 4 && isEquals(i)) {
                nickname2.getAndIncrement();
            } else if (i.length() == 5 && isEquals(i)) {
                nickname3.getAndIncrement();
            }
        });
        threads.add(new Thread(runnable2));

        Runnable runnable3 = () -> Arrays.stream(texts).forEach(i -> {
            if (i.length() == 3 && isSort(i)) {
                nickname1.getAndIncrement();
            } else if (i.length() == 4 && isSort(i)) {
                nickname2.getAndIncrement();
            } else if (i.length() == 5 && isSort(i)) {
                nickname3.getAndIncrement();
            }
        });
        threads.add(new Thread(runnable3));

        for (Thread thread : threads) {
            thread.start();
            thread.join();
        }

        System.out.println();
        System.out.println("Красивых слов с длиной 3: " + nickname1 + " шт");
        System.out.println("Красивых слов с длиной 4: " + nickname2 + " шт");
        System.out.println("Красивых слов с длиной 5: " + nickname3 + " шт");

        }

    private static boolean isPalindrome(String string) {
        return !isEquals(string) && string.contentEquals(new StringBuilder(string).reverse());
        }

    private static boolean isEquals(String string) {
        return string.matches("(.)\\1*");
    }

    private static boolean isSort(String string) {
        String stringSort = string.chars()
                .sorted()
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return !isEquals(string) && stringSort.equals(string);
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
