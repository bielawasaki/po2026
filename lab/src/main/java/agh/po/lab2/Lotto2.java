package agh.po.lab2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Lotto2 {
    public static void main(String[] args) {
        int size = 6;

        if (args.length < size) {
            System.out.println("Too few arguments!");
            return;
        }

        ArrayList<Integer> arr = new ArrayList<>();

        while (arr.size() < size) {
            int randomNum = getRandomInt(1,49);
            if (!arr.contains(randomNum)) {
                arr.add(randomNum);
            }
        }

        ArrayList<String> argsList = new ArrayList<>(Arrays.asList(args));

        System.out.println("Twoje typy: " + argsList);
        System.out.println("Wylosowane liczby: " + arr);

        ArrayList<Integer> intArgsList = new ArrayList<>(argsList.stream().map(Integer::parseInt).toList());

        intArgsList.retainAll(arr);

        System.out.println("Liczba trafień: " + intArgsList.size());
    }

    public static int getRandomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(min, max + 1);
    }
}
