package com.paulograbin;

import java.io.RandomAccessFile;
import java.lang.foreign.Arena;
import java.lang.foreign.ValueLayout;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.Random;

public class SharedMemoryWriter {
    public static void main(String[] args) throws Exception {
        // Create or open the memory-mapped file
        try (RandomAccessFile file = new RandomAccessFile("shared_memory.dat", "rw");
             FileChannel channel = file.getChannel()) {

            // Map the file into memory
            var buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 512, Arena.global());
            System.out.println("base address: " + buffer.address());
            System.out.println("endianness " + ByteOrder.nativeOrder());

            // Write data to the buffer
//            String message = "Hello from Writer!";
//            buffer.put(message.getBytes());

            // Update a "shared state" variable

            var random = new Random();

//            int firstInt = random.nextInt();
            int firstInt = Integer.MAX_VALUE;
            buffer.set(ValueLayout.JAVA_INT, 0, firstInt); // Write an integer at offset 0
            buffer.set(ValueLayout.JAVA_INT, 4, 0); // Write an integer at offset 0
            buffer.set(ValueLayout.JAVA_INT, 8, 0); // Write an integer at offset 0

            String binaryString = Integer.toBinaryString(firstInt);
            System.out.println("binary string if " + firstInt + " is " + binaryString);

            int i = Integer.parseInt(binaryString, 2);
            System.out.println("back conversion to int is " + i);

            int value = random.nextInt();
            buffer.set(ValueLayout.JAVA_INT, 200, value); // Write an integer at offset 512

            System.out.println("Writer: Data ( " + value + " ) written to shared memory.");
        }
    }
}
