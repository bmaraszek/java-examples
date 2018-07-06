package pl.bmaraszek;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {

    private App testee = new App();

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
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