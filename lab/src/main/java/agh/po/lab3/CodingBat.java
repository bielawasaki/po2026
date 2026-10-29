package agh.po.lab3;

public class CodingBat {
    public boolean nearHundred(int n) {
        if (Math.abs(n - 100) <= 10) {
            return true;
        } else return Math.abs(n - 200) <= 10;
    }

    public int countEvens(int[] nums) {
        int counter = 0;
        for (int num : nums) {
            if (num % 2 == 0) {
                counter++;
            }
        }

        return counter;
    }

    public int sum13(int[] nums) {
        if (nums.length < 1) {
            return 0;
        }

        int sum = 0;
        boolean skip = false;
        for (int num : nums) {
            if (num == 13) {
                skip = true;
                continue;
            }
            if (skip) {
                skip = false;
                continue;
            }
            sum += num;
        }

        return sum;
    }

    public String doubleChar(String str) {
        String out = "";
        for (int i = 0; i < str.length(); i++) {
            char character = str.charAt(i);
            out = out + character + character;
        }

        return out;
    }

    public static void main(String[] args) {
        CodingBat cb = new CodingBat();
        System.out.println(cb.doubleChar("lukasz"));
    }
}
