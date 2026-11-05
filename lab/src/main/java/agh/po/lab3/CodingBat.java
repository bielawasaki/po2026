package agh.po.lab3;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

    public Map<String, String> mapAB(Map<String, String> map) {
        if (map.containsKey("a") && map.containsKey("b")) {
            map.put("ab", map.get("a") + map.get("b"));
        }

        return map;
    }

    public Map<String, Integer> wordLen(String[] strings) {
      Map<String, Integer> map = new HashMap<>();
      for (String str: strings) {
          if (!map.containsKey(str)) {
              map.put(str, str.length());
          }
      }
      return map;
    }

    public int maxSpan(int[] nums) {
        if (nums.length < 1) return 0;

        HashMap<Integer, Integer> numsSourceMap = new HashMap<Integer, Integer>();
        HashMap<Integer, Integer> numsSpanMap = new HashMap<Integer, Integer>();
        for (int i = 0; i < nums.length; i++) {
            if (!numsSourceMap.containsKey(nums[i])) {
                numsSourceMap.put(nums[i], i);
                numsSpanMap.put(nums[i], 1);
            } else {
                numsSpanMap.put(nums[i], i - numsSourceMap.get(nums[i]) + 1);
            }
        }

        return Collections.max(numsSpanMap.values());
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
