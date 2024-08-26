package x_hp_vierte_code_stuff;

import java.util.Arrays;

public class sort {
    public static void main(String[] args) {
//        System.out.println(Arrays.toString(getMostCommonChar("HalloWalter")));
//        System.out.println(Arrays.toString(getMostCommonChar("ABCDE")));
        System.out.println(Integer.compare(10,0));
    }

    private static char[] getMostCommonChar(String s) {
        if (s == null || s.isBlank()) return new char[0];
        s = s.toLowerCase();
        int[] amount = new int[26];
        for (char c : s.toCharArray()) {
            if (Character.isLetter(c)) amount[c - 'a']++;
        }
        int length = 0;
        for (int i = 0; i < amount.length; i++) {
            if (amount[i] != 0) length++;
        }
        char[] res = new char[length];
        int maxChar = -1;
        for (int i = 0; i < length; i++) {
            int nextChar = -1;
            for (int j = 0; j < amount.length; j++) {
                if ((nextChar == -1) || amount[j] > amount[nextChar]) {
                    if (amount[j] <= maxChar || maxChar == -1)
                        nextChar = j;
                }
            }
            maxChar = amount[nextChar];
            res[i] = (char) (nextChar + 'a');
            amount[nextChar]=0;
        }
        return res;
    }
}
