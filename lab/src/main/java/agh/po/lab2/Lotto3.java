package agh.po.lab2;

import java.util.ArrayList;
import java.util.Random;

public class Lotto3 {
    public static void main(String[] args) {
        long startTime = System.nanoTime();
      
        int size = 6;

        int counter = 0;
        int hits = 0;

        ArrayList<Integer> arr = new ArrayList<>();
        ArrayList<Integer> arr2 = new ArrayList<>();
        while (hits < 6) {
          counter++;
          arr.clear();
          arr2.clear();
          
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
          hits = arr2.size();
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        System.out.println("Arr1: " + arr);
        System.out.println("Arr2: " + arr2);
        System.out.println("Liczba losowan: " + counter);
        System.out.println("Czas trwania: " + duration + " ms");
    }

    public static int getRandomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(min, max + 1);
    }
}
