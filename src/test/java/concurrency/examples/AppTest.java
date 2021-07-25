package concurrency.examples;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import leetcode.examples.App;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AppTest {

    private App testee = new App();

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void cleanUpStreams() {
        System.setOut(null);
        System.setErr(null);
    }

    @Test
    public void test() {
        assertThat(1).isBetween(0, 2);
    }

    @Test
    public void shouldReadFile() {
        testee.readFile("resources.csv");
        assertThat(outContent.toString()).isEqualTo("test one\ntest two\ntest three\n");
    }
}