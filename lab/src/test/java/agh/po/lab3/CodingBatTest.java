package agh.po.lab3;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

class CodingBatTest {
    CodingBat cb = new CodingBat();

    @org.junit.jupiter.api.Test
    void nearHundred() {
        assertTrue(cb.nearHundred(91));
        assertTrue(cb.nearHundred(192));
        assertTrue(cb.nearHundred(199));
        assertTrue(cb.nearHundred(97));
        assertFalse(cb.nearHundred(89));
        assertFalse(cb.nearHundred(176));
        assertFalse(cb.nearHundred(111));
        assertFalse(cb.nearHundred(189));
    }

    @org.junit.jupiter.api.Test
    void countEvens() {
        assertEquals(5, cb.countEvens(new int[]{2,3,4,5,6,7,8,9,10}));
        assertEquals(0, cb.countEvens(new int[]{35,5,77,95,11,23,17}));
        assertEquals(2, cb.countEvens(new int[]{18,31,14,59,71,93}));
    }

    @org.junit.jupiter.api.Test
    void sum13() {
        assertEquals(8, cb.sum13(new int[]{2,1,4,13,6,1}));
        assertEquals(5, cb.sum13(new int[]{13,50,4,13,6,1}));
        assertEquals(13, cb.sum13(new int[]{4,3,13,80,13,20,6}));
    }

    @org.junit.jupiter.api.Test
    void doubleChar() {
        assertEquals("aabbccdd", cb.doubleChar("abcd"));
        assertEquals("tteesscciikk", cb.doubleChar("tescik"));
        assertEquals("pp--aa--ww--ee--ll!!", cb.doubleChar("p-a-w-e-l!"));
    }

    @org.junit.jupiter.api.Test
    void maxSpan() {
        assertEquals(4, cb.maxSpan(new int[] {1,1,3,1,6,7}));
        assertEquals(2, cb.maxSpan(new int[] {9,9,8,5,6,12}));
        assertEquals(6, cb.maxSpan(new int[] {15,21,35,16,61,15}));
        assertEquals(1, cb.maxSpan(new int[] {15,21,35,16,61,81}));
    }

    @org.junit.jupiter.api.Test
    void mapAB() {
        Map<String, String> map1 = new HashMap<>();
        map1.put("a", "Hi");
        map1.put("b", "There");
        assertEquals("HiThere", cb.mapAB(map1).get("ab"));

        Map<String, String> map2 = new HashMap<>();
        map2.put("a", "Hi");
        assertEquals("Hi", cb.mapAB(map2).get("a"));

        Map<String, String> map3 = new HashMap<>();
        map3.put("b", "There");
        assertEquals("There", cb.mapAB(map3).get("b"));
    }

    @org.junit.jupiter.api.Test
    void wordLen() {
        String[] strings = {"apple", "banana", "kiwi", "apple", "kiwi"};
        Map<String, Integer> expectedMap = new HashMap<>();
        expectedMap.put("apple", 5);
        expectedMap.put("banana", 6);
        expectedMap.put("kiwi", 4);

        assertEquals(expectedMap, cb.wordLen(strings));
    }
}