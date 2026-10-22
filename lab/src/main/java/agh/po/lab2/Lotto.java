package agh.po.lab2;

import java.util.ArrayList;
import java.util.Random;

public class Lotto {
    public static void main(String[] args) {
        int size = 6;

        ArrayList<Integer> arr = new ArrayList<>();

        while (arr.size() < size) {
            int randomNum = getRandomInt(1,49);
            if (!arr.contains(randomNum)) {
                arr.add(randomNum);
            }
        }

        System.out.println("Wylosowane liczby: " + arr);
    }

    public static int getRandomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(min, max + 1);
    }
}