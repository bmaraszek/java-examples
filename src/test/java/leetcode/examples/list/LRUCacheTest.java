package leetcode.examples.list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class LRUCacheTest {


    private LRUCache<String, String> subject = new LinkedListLRUCache<>(3);

    @BeforeEach
    public void setup() {
        subject = new LinkedListLRUCache<>(3);
    }

    @Test
    @DisplayName("Should be empty when created")
    public void shouldBeEmptyWhenCreated() {
        assertThat(subject.isEmpty()).isTrue();
        assertThat(subject.size()).isZero();
    }

    @Test
    @DisplayName("Should not be empty when entry added")
    public void shouldNotBeEmptyWhenEntryAdded() {
        subject.put("key1", "value1");
        assertThat(subject.isEmpty()).isFalse();
        assertThat(subject.size()).isOne();
    }

    @Test
    @DisplayName("Should return empty optional when cache is empty")
    public void shouldReturnEmptyOptionalWhenCacheIsEmpty() {
        Optional<String> result = subject.get("iDontExist");

        assertThat(subject.isEmpty()).isEqualTo(true);
        assertThat(subject.size()).isZero();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return empty optional when cache miss")
    public void shouldReturnEmptyOptionalWhenEntryNotPresent() {
        subject.put("key1", "value1")
                .put("key2", "value2")
                .put("key3", "value3");
        Optional<String> result = subject.get("iDontExist");
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return value when cache hit")
    public void shouldReturnValueWhenCacheHit() {
        subject.put("key1", "value1")
                .put("key2", "value2")
                .put("key3", "value3");
        Optional<String> result = subject.get("key2");
        assertThat(result.get()).isEqualTo("value2");
    }

    @Test
    @DisplayName("Should retain max number of entries")
    public void shouldRetainMaxNumberOfEntries() {
        subject.put("key1", "value1")
                .put("key2", "value2")
                .put("key3", "value3")
                .put("key4", "value4");

        assertThat(subject.get("key4")).isPresent();
        assertThat(subject.get("key3")).isPresent();
        assertThat(subject.get("key2")).isPresent();
        assertThat(subject.get("key1")).isNotPresent();
    }

    @Test
    @DisplayName("Should mark accessed value as last recently used")
    public void shouldMarkAccessedValueAsLastRecentlyUsed() {
        subject.put("key1", "value1")
                .put("key2", "value2")
                .put("key3", "value3");

        subject.get("key1");
        subject.put("key4", "value4");

        assertThat(subject.get("key4")).isPresent();
        assertThat(subject.get("key3")).isPresent();
        assertThat(subject.get("key2")).isNotPresent();
        assertThat(subject.get("key1")).isPresent();

    }
}
