package com.paulograbin;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class SharedMemoryVisualizer {
    public static void main(String[] args) throws Exception {
        try (RandomAccessFile file = new RandomAccessFile("shared_memory.dat", "r")) {
            byte[] buffer = new byte[1024];
            file.read(buffer);
            System.out.println("File Content:");
            for (int i = 0; i < buffer.length; i++) {
                byte b = buffer[i];
                System.out.print((char) b); // Assumes ASCII content
            }
        }
    }
}
