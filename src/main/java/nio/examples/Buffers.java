package nio.examples;

import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.*;

public class Buffers {

    private final static Path PATH = Paths.get("src/main/resources/nio/ints.bin");

    public static void main(String[] args) throws IOException {
        writeToFile();
        readFromFile();
    }

    @SneakyThrows
    private static void writeToFile() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.putInt(10).putInt(20).putInt(30);

        System.out.printf("Before flip: Position = %s; Limit = %s\n", byteBuffer.position(), byteBuffer.limit());
        byteBuffer.flip();
        System.out.printf("After flip: Position = %s; Limit = %s\n", byteBuffer.position(), byteBuffer.limit());

        try (FileChannel fileChannel = FileChannel.open(PATH, CREATE, WRITE);) {
            fileChannel.write(byteBuffer); // writes the content between current position and the limit
        }
        System.out.printf("File size is: %d\n\n", Files.size(PATH));
    }

    @SneakyThrows
    private static void readFromFile() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        try (FileChannel fileChannel = FileChannel.open(PATH, READ);) {
            fileChannel.read(byteBuffer);
            byteBuffer.flip();
            for(int i = 0; i < 3; i++) {
                System.out.printf("Read: %d\n",  byteBuffer.getInt());
            }
        }
    }
}
