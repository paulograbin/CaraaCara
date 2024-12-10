package com.paulograbin;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

public class SharedMemoryReader {
    public static void main(String[] args) throws Exception {
        File f = new File("shared_memory.dat");
        f.createNewFile();
        f.deleteOnExit();

        try (FileChannel channel =
                     FileChannel.open(f.toPath(), StandardOpenOption.WRITE, StandardOpenOption.READ)) {
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 512);

            int lastValue = -1;
            while (true) {
                int currentValue = buffer.getInt(200); // Check shared state
                if (currentValue != lastValue) {
                    System.out.println("Detected change: " + currentValue);
                    lastValue = currentValue;


                    for (int i = 0; i < 512; i++) {
                        System.out.print(buffer.get(i));
                    }

                    System.out.println("");
                }

                Thread.sleep(100); // Adjust polling interval as needed
            }
        }
    }
}
