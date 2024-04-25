package com.mycompany.ex3.dcc075;
 import java.math.BigInteger;

/**
 *
 * @author Samuel
 */


public class Ex3Dcc075 {
    
    // S-Boxes do DES
    private static final int[][] S_BOXES = {
        // S1
        {
            14,  4, 13,  1,  2, 15, 11,  8,  3, 10,  6, 12,  5,  9,  0,  7,
             0, 15,  7,  4, 14,  2, 13,  1, 10,  6, 12, 11,  9,  5,  3,  8,
             4,  1, 14,  8, 13,  6,  2, 11, 15, 12,  9,  7,  3, 10,  5,  0,
            15, 12,  8,  2,  4,  9,  1,  7,  5, 11,  3, 14, 10,  0,  6, 13
        },
        // S2
        {
            15,  1,  8, 14,  6, 11,  3,  4,  9,  7,  2, 13, 12,  0,  5, 10,
             3, 13,  4,  7, 15,  2,  8, 14, 12,  0,  1, 10,  6,  9, 11,  5,
             0, 14,  7, 11, 10,  4, 13,  1,  5,  8, 12,  6,  9,  3,  2, 15,
            13,  8, 10,  1,  3, 15,  4,  2, 11,  6,  7, 12,  0,  5, 14,  9
        },
        // S3
        {
            10,  0,  9, 14,  6,  3, 15,  5,  1, 13, 12,  7, 11,  4,  2,  8,
            13,  7,  0,  9,  3,  4,  6, 10,  2,  8,  5, 14, 12, 11, 15,  1,
            13,  6,  4,  
            5, 11,  3, 14, 10,  5, 15, 12,  7,  8,  4,  9,  1, 14, 11,  2
        },
        // S4
        {
            7, 13, 14,  3,  0,  6,  9, 10,  1,  2,  8,  5, 11, 12,  4, 15,
            13,  8, 11,  5,  6, 15,  0,  3,  4,  7,  2, 12,  1, 10, 14,  9,
            10,  6,  9,  0, 12, 11,  7, 13, 15,  1,  3, 14,  5,  2,  8,  4
        },
        // S5
        {
            2, 14, 12, 11,  4,  2,  1, 12,  7,  4, 10,  7, 11, 13,  6,  1,
            8,  5,  5,  0,  3, 15, 15, 10, 13,  3,  0,  9, 14,  8,  9,  6,
            4, 11,  2,  8,  1, 12, 11,  7, 10,  1, 13, 14,  7,  2,  8, 13
        },
        // S6
        {
            4, 13, 11,  0,  2, 11, 14,  7, 15,  4,  1, 12, 10,  0,  6,  9,
            8,  5,  3,  2, 12,  5,  9,  6, 10, 15,  9,  8,  6,  8,  1,  3,
            13,  3,  4, 15,  1, 14, 10,  2,  7, 12,  6,  0,  9,  5, 13, 11
        },
        // S7
        {
            13,  1,  2, 15, 14,  7,  8, 13, 15,  6,  9, 10,  
1,  4,  5, 11,  0,  8,  3, 14, 12,  9,  7,  2, 10,  6,  1, 12,
            15, 10,  4,  1, 14,  8,  3,  9,  7,  6, 11, 12,  2,  0,  5, 15,
             1, 14,  7,  9,  4,  2,  0,  6, 10, 13, 15,  3,  5,  8, 11, 10
        },
        // S8
        {
            12,  9,  0,  7,  2, 14, 11,  4,  2,  1, 10, 11,  7, 13, 15,  3,
            11, 13,  3, 14,  6,  4,  9,  0,  8,  6, 15,  9,  3,  8,  5,  2,
             1, 14, 12,  7,  4, 10, 15,  2,  8,  5, 13,  0, 14, 13,  6,  1
        }
    };

    private static BigInteger feistel(BigInteger right, BigInteger key) {
        right = expandAndPermute(right);
        right = right.xor(key);
        BigInteger[] parts = divideIntoSixBitParts(right);
        right = applySBoxes(parts);
        right = permute(right);
        return right;
    }

    private static BigInteger expandAndPermute(BigInteger right) {
    int[] expansionTable = {
        32,  1,  2,  3,  4,  5,
         4,  5,  6,  7,  8,  9,
         8,  9, 10, 11, 12, 13,
        12, 13, 14, 15, 16, 17,
        16, 17, 18, 19, 20, 21,
        20, 21, 22, 23, 24, 25,
        24, 25, 26, 27, 28, 29,
        28, 29, 30, 31, 32,  1
    };
    BigInteger expanded = BigInteger.ZERO;
    for (int i = 0; i < expansionTable.length; i++) {
        if (right.testBit(32 - expansionTable[i])) {
            expanded = expanded.setBit(47 - i);
        }
    }
    return expanded;
}

    private static BigInteger[] divideIntoSixBitParts(BigInteger right) {
    BigInteger[] parts = new BigInteger[8];
    for (int i = 0; i < 8; i++) {
        parts[i] = right.shiftRight(6 * (7 - i)).and(new BigInteger("3F", 16));
    }
    return parts;
}


    private static BigInteger applySBoxes(BigInteger[] parts) {
    BigInteger result = BigInteger.ZERO;
    for (int i = 0; i < 8; i++) {
        int row = parts[i].shiftRight(5).intValue();
        int col = parts[i].and(new BigInteger("1F", 16)).intValue();
        int value = S_BOXES[i][row * 16 + col];
        result = result.or(BigInteger.valueOf(value).shiftLeft(4 * (7 - i)));
    }
    return result;
}


    private static BigInteger permute(BigInteger right) {
    int[] permutationTable = {
        16,  7, 20, 21,
        29, 12, 28, 17,
         1, 15, 23, 26,
         5, 18, 31, 10,
         2,  8, 24, 14,
        32, 27,  3,  9,
        19, 13, 30,  6,
        22, 11,  4, 25
    };
    BigInteger permuted = BigInteger.ZERO;
    for (int i = 0; i < permutationTable.length; i++) {
        if (right.testBit(32 - permutationTable[i])) {
            permuted = permuted.setBit(31 - i);
        }
    }
    return permuted;
}


    public static BigInteger encrypt(BigInteger plainText, BigInteger key) {
        BigInteger left = plainText.shiftRight(32);
        BigInteger right = plainText.and(new BigInteger("FFFFFFFF", 16));

        for (int i = 0; i < 16; i++) {
            BigInteger temp = left;
            left = right;
            right = temp.xor(feistel(right, key));
        }
        return left.shiftLeft(32).or(right);
    }

    public static void main(String[] args) {

        BigInteger key = new BigInteger("133457799BBCDFF1", 16);
        BigInteger plainText = new BigInteger("0123456789ABCDEF", 16);

        BigInteger cipherText = encrypt(plainText, key);
        System.out.println("Texto cifrado: " + cipherText.toString(16));
    }
}


/* INSTRUÇÕES PARA RODAR :

PARA COMPILAR :
    mvn package

PARA EXECUTAR O PROGRAMA:
    java -cp target/ex3-dcc075-1.0-SNAPSHOT.jar com.mycompany.ex3.dcc075.Ex3Dcc075

*/