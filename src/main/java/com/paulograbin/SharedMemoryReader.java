package com.paulograbin;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class SharedMemoryReader {
    public static void main(String[] args) throws Exception {
        try (RandomAccessFile file = new RandomAccessFile("shared_memory.dat", "rw");
             FileChannel channel = file.getChannel()) {

            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 1024);

            int lastValue = -1;
            while (true) {
                int currentValue = buffer.getInt(512); // Check shared state
                if (currentValue != lastValue) {
                    System.out.println("Detected change: " + currentValue);
                    lastValue = currentValue;
                }

                Thread.sleep(100); // Adjust polling interval as needed
            }
        }
    }
}
