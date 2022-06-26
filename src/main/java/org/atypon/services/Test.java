package org.atypon.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Test {
    public static boolean containsDuplicate(int[] nums) {
        Set<Integer> sets = new HashSet<>();
        for (int i: nums){
            if(!sets.add(i))return true;
        }
        return sets.size() <nums.length;

    }
        public static void main(String[] args) {
            System.out.println(containsDuplicate(new int[]{1, 2, 3, 4}));
        ;

    }
}
