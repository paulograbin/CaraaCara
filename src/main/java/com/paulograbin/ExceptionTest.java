package com.paulograbin;

public class ExceptionTest {
    public static void main(String[] args) {
        byte array[] = new byte[0];

        for (int i = 0; i < 100_000_000; ++i) {
            try {
                array[i] = (byte) i;
                System.out.println("bad");
            } catch (ArrayIndexOutOfBoundsException err) {
                if (err.getStackTrace().length == 0) {
                    System.out.println("Empty stack at: " + i);
                    break;
                }
            }
        }
    }
}