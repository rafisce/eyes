package com.eyes.eyes;
import java.math.BigInteger;
import java.util.ArrayList;

public class DesEncryption {
    public DesEncryption() {

    }

    private static int[][] s1 = { { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 },
            { 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 },
            { 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 },
            { 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 } };

    private static int[][] s2 = { { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 },
            { 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 },
            { 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 },
            { 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 } };

    private static int[][] s3 = { { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 },
            { 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 },
            { 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 },
            { 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 } };

    private static int[][] s4 = { { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 },
            { 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 },
            { 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 },
            { 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 } };

    private static int[][] s5 = { { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 },
            { 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 },
            { 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 },
            { 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 } };

    private static int[][] s6 = { { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 },
            { 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 },
            { 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 },
            { 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 } };

    private static int[][] s7 = { { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 },
            { 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 },
            { 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 },
            { 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 } };

    private static int[][] s8 = { { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 },
            { 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 },
            { 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 },
            { 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 } };

    private static final ArrayList<int[][]> s = new ArrayList<int[][]>() {
        {
            add(s1);
            add(s2);
            add(s3);
            add(s4);
            add(s5);
            add(s6);
            add(s7);
            add(s8);
        }
    };

    private String stringTo64(String key) {

        String created = "";

        for (int i = 0; i < key.length(); i++) {
            int temp = (int) key.charAt(i);
            String temp2 = String.format("%08d", Integer.parseInt(Integer.toBinaryString(temp)));
            created += temp2;
        }

        return created;
    }

    // premutation
    // functions/////////////////////////////////////////////////////////////////////////////
    private String pc1(String input) {
        String created = "";
        final int[] pc_1 = { 57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35, 27, 19, 11, 3,
                60, 52, 44, 36, 63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14, 6, 61, 53, 45, 37, 29, 21,
                13, 5, 28, 20, 12, 4 };

        for (int i = 0; i < (pc_1.length); i++) {
            created += input.substring(pc_1[i] - 1, pc_1[i]);
        }
        return created;
    }

    private String pc2(String input) {
        String created = "";
        final int[] pc_2 = { 14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2, 41,
                52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32 };

        for (int i = 0; i < (pc_2.length); i++) {
            created += input.substring(pc_2[i] - 1, pc_2[i]);
        }
        return created;
    }

    private String IP(String input) {
        String created = "";
        final int[] ip = { 58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6,
                64, 56, 48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3, 61, 53, 45,
                37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7 };
        for (int i = 0; i < (ip.length); i++) {
            created += input.substring(ip[i] - 1, ip[i]);
        }
        return created;
    }

    private String P(String input) {
        String created = "";
        final int[] p = { 16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9, 19,
                13, 30, 6, 22, 11, 4, 25 };
        for (int i = 0; i < (p.length); i++) {
            created += input.substring(p[i] - 1, p[i]);
        }
        return created;
    }

    private String IPi(String input) {
        String created = "";
        final int[] ipi = { 40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30,
                37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27, 34, 2, 42,
                10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25 };
        for (int i = 0; i < (ipi.length); i++) {
            created += input.substring(ipi[i] - 1, ipi[i]);
        }
        return created;
    }

    // premuteation
    // functions//////////////////////////////////////////////////////////////////////////////////////

    private ArrayList<String> makeSubKeys(String key) {

        ArrayList<String> subkeys = new ArrayList<>();
        int[] key_shifts = { 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1 };

        String premutation_key = pc1(stringTo64(key));
        String left = premutation_key.substring(0, (premutation_key.length()) / 2);
        String right = premutation_key.substring(((premutation_key.length()) / 2), premutation_key.length());

        for (Integer num : key_shifts) {

            for (int i = 0; i < num; i++) {
                left = left.substring(1) + left.charAt(0);
                right = right.substring(1) + right.charAt(0);
            }
            subkeys.add(pc2(left + right));
        }

        return subkeys;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////
    private String expand(String input) {

        String created = "" + input.charAt(31);
        int j = 0;
        for (int i = 0; i < input.length(); i++) {
            j++;
            created += input.charAt(i);
            if (j == 4 && i != input.length() - 1) {
                created += input.charAt(i + 1);
                created += input.charAt(i);
                j = 0;
            }

        }
        return created += input.charAt(0);
    }
    ////////////XOR///////////////////////////////////////////////////////////////////////////////
    private String xor(String subkey, String exp) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < subkey.length(); i++) {
            sb.append(charOf(bitOf(subkey.charAt(i)) ^ bitOf(exp.charAt(i))));
        }
        String result = sb.toString();

        return result;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////
    private String makeTextBlocks(String text) {
        int a;
        String result = "";
        if (text.length() % 8 == 0) {
            a = text.length() / 8;
        } else {
            a = (text.length() / 8) + 1;
        }
        String temp;
        for (int i = 0; i < a; i++) {

            if (i < a - 1) {
                temp = text.substring(i * 8, i * 8 + 8);
            } else {
                temp = text.substring(i * 8);
            }
            if (temp.length() == 8) {
                result += stringTo64(temp);
            } else if (temp.length() > 0) {
                temp = String.format("%-8s", temp + "$").replace(' ', '0');
                result += stringTo64(temp);
            }

        }
        return result;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////
    private String makeHexBlocks(String text) {
        int a;
        String result = "";
        if (text.length() % 16 == 0) {
            a = text.length() / 16;
        } else {
            a = (text.length() / 16) + 1;
        }
        String temp;
        for (int i = 0; i < a; i++) {

            if (i < a - 1) {
                temp = text.substring(i * 16, i * 16 + 16);
            } else {
                temp = text.substring(i * 16);
            }
            if (temp.length() == 16) {
                result += temp = String.format("%64s", hexToBinary(temp)).replace(' ', '0');
            } else if (temp.length() > 0) {
                temp = String.format("%-16s", temp + "$").replace(' ', '0');
                result += hexToBinary(temp);
            }

        }
        return result;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////
    private static boolean bitOf(char in) {
        return (in == '1');
    }

    private static char charOf(boolean in) {
        return (in) ? '1' : '0';
    }
    ///////////////////////////////////////////////////////////////////////////////////////////
    private String rightFunc(String right, String subkey) {
        String created = "";

        String right48 = xor(subkey, expand(right));

        for (int[][] some : s) {
            String tempSix = right48.substring(0, 6);
            int row = Integer.parseInt(tempSix.charAt(0) + "" + tempSix.charAt(5), 2);
            int column = Integer.parseInt(tempSix.substring(1, 5), 2);
            created += String.format("%04d", Integer.parseInt(Integer.toString(some[row][column], 2)));
            if (right48.length() > 6) {
                right48 = right48.substring(6);
            }
        }
        return P(created);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////
    private String binaryToAscii(String input) {
        String created = "";
        for (int i = 0; i < input.length() / 8; i++) {
            String temp = input.substring(i * 8, i * 8 + 8);
            char temp2 = (char) Integer.parseInt(temp, 2);
            created += temp2;

        }
        return created;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////
    private String binaryToHex(String input) {
        String created = "";
        for (int i = 0; i < input.length() / 8; i++) {
            String temp = input.substring(i * 8, i * 8 + 8);
            String temp2 = Integer.toHexString(Integer.parseInt(temp, 2));
            if (temp2.length() == 2) {
                created += temp2;
            } else {
                created += "0" + temp2;
            }

        }
        return created;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////
    private String hexToBinary(String input) {
        return new BigInteger(input, 16).toString(2);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////
    private String blockEncryption(String block, String key) {

        String premutation_key = IP(block);
        String left = premutation_key.substring(0, (premutation_key.length()) / 2);
        String right = premutation_key.substring(((premutation_key.length()) / 2));
        ArrayList<String> subkeys = makeSubKeys(key);

        for (String subkey : subkeys) {
            String tempRight = right;
            right = xor(left, rightFunc(right, subkey));
            left = tempRight;

        }
        String result = IPi(right + left);

        return binaryToHex(result);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////
    private String blockDecryption(String block, String key) {

        String premutation_key = IP(block);
        String left = premutation_key.substring(0, (premutation_key.length()) / 2);
        String right = premutation_key.substring(((premutation_key.length()) / 2));
        ArrayList<String> subkeys = makeSubKeys(key);

        for (int i = subkeys.size() - 1; i >= 0; i--) {
            String tempRight = right;
            right = xor(left, rightFunc(right, subkeys.get(i)));
            left = tempRight;

        }
        String result = IPi(right + left);

        return binaryToAscii(result);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    public String Encrypt(String plainText, String key) {
        String ciphertext = "";
        ArrayList<String> textBlock = new ArrayList<>();
        plainText = makeTextBlocks(plainText);
        for (int i = 0; i < plainText.length(); i++) {
            if (plainText.length() >= 64) {
                textBlock.add(plainText.substring(0, 64));
                plainText = plainText.substring(64);
                i = 0;
            } else if (plainText.length() != 0) {
                plainText = String.format("%-64s", plainText).replace(' ', '0');
                textBlock.add(plainText);
                break;
            }

        }
        for (String block : textBlock) {
            ciphertext += blockEncryption(block, key);
        }
        return ciphertext;

    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    public String Decrypt(String cipherText, String key) {
        String plainText = "";
        ArrayList<String> cipherBlock = new ArrayList<>();
        cipherText = makeHexBlocks(cipherText);

        for (int i = 0; i < cipherText.length(); i++) {
            if (cipherText.length() >= 64) {
                cipherBlock.add(cipherText.substring(0, 64));
                cipherText = cipherText.substring(64);
                i = 0;
            } else if (cipherText.length() != 0) {
                cipherText = String.format("%-64s", cipherText).replace(' ', '0');
                cipherBlock.add(cipherText);
                break;
            }
        }

        for (String block : cipherBlock) {
            plainText += blockDecryption(block, key);
        }

        if(plainText.contains("$"))
        {
            return plainText.substring(0, plainText.indexOf("$"));
        }
        else
        {
            return plainText;
        }
    }
}
