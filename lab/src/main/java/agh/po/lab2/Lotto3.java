package agh.po.lab2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Lotto3 {
    static Random random = new Random();
    
    public static void main(String[] args) {
        long startTime = System.nanoTime();
      
        int size = 6;

        if (args.length < size) {
            System.out.println("Too few arguments!");
            return;
        }

        int counter = 0;
        int hits = 0;

        ArrayList<String> argsList = new ArrayList<>(Arrays.asList(args));
        ArrayList<Integer> intArgsList = new ArrayList<>(argsList.stream().map(Integer::parseInt).toList());
        ArrayList<Integer> arr = new ArrayList<>();
        
        while (hits < 6) {
          counter++;
          arr.clear();
          
          while (arr.size() < size) {
              int randomNum = getRandomInt(1,49);
              if (!arr.contains(randomNum)) {
                  arr.add(randomNum);
              }
          }

          arr.retainAll(intArgsList);
          hits = arr.size();
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        float durationInSeconds = (float)duration / 1000f;
        System.out.println("Arr1: " + arr);
        System.out.println("Liczba losowan: " + counter);
        System.out.println("Czas trwania: " + duration + " ms" + " (" + durationInSeconds + " s)");
    }

    public static int getRandomInt(int min, int max) {
        return random.nextInt(min, max + 1);
    }
}
