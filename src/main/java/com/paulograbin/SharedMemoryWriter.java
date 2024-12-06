package com.paulograbin;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;

public class SharedMemoryWriter {
    public static void main(String[] args) throws Exception {
        // Create or open the memory-mapped file
        try (RandomAccessFile file = new RandomAccessFile("shared_memory.dat", "rw");
             FileChannel channel = file.getChannel()) {


            // Map the file into memory
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 1024);


            // Write data to the buffer
//            String message = "Hello from Writer!";
//            buffer.put(message.getBytes());

            // Update a "shared state" variable
            int value = new Random().nextInt(0, 10);
            buffer.putChar('c'); // Write an integer at offset 512
            buffer.putChar('b'); // Write an integer at offset 512
            buffer.putChar('h'); // Write an integer at offset 512
            buffer.putChar('j'); // Write an integer at offset 512
            buffer.putChar('z'); // Write an integer at offset 512
            buffer.putChar('@'); // Write an integer at offset 512
            buffer.putChar(' '); // Write an integer at offset 512

            System.out.println("Writer: Data ( " + value + " ) written to shared memory.");
        }
    }
}
