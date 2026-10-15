package agh.po.lab2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Lotto3 {
    public static void main(String[] args) {
        int size = 6;

        ArrayList<Integer> arr = new ArrayList<>();
        ArrayList<Integer> arr2 = new ArrayList<>();

        while (arr.size() < size) {
            int randomNum = getRandomInt(1,49);
            if (!arr.contains(randomNum)) {
                arr.add(randomNum);
            }
        }

        while (arr2.size() < size) {
            int randomNum2 = getRandomInt(1,49);
            if (!arr2.contains(randomNum2)) {
                arr2.add(randomNum2);
            }
        }

        arr2.retainAll(arr);
        int hits = arr2.size();
    }

    public static int getRandomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(min, max + 1);
    }
}
