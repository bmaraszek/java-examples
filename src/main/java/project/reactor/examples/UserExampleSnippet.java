package project.reactor.examples;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class UserExampleSnippet {

  public static void main(String[] args) {
    getUserId()
        .flatMap(userId -> getUser(userId))
        .filter(user -> isUserValid(user))
        .log()
        .subscribe(user -> log.info("User: {}", user));

    log.info("========");

    getUserIds()
        .flatMap(userId -> getUsers(userId))
        .filter(user -> isUserValid(user))
        .log()
        .subscribe(user -> log.info("User: {}", user));
  }

  public static Mono<String> getUserId() {
    log.info("getUserId()");
    return Mono.just("abc123");
  }

  public static Flux<String> getUserIds() {
    log.info("getUserIds()");
    return Flux.fromIterable(Lists.list("abc123", "xyz123", "cde123"));
  }

  public static Mono<User> getUser(String userId) {
    log.info("getUser({})", userId);
    return Mono.just(new User("abc123", "Frodo"));
  }

  public static Flux<User> getUsers(String userId) {
    log.info("getUsers({})", userId);
    return Flux.fromIterable(Lists.list(
        new User("abc123", "Frodo"),
        new User("xyz123", null),
        new User("cde123", "Sam")
    ));
  }

  public static boolean isUserValid(User user) {
    log.info("isUserValid({})", user);
    return user.getId() != null && user.getName() != null;
  }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
class User {
  private String id;
  private String name;
}
