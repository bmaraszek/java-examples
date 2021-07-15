package project.reactor.examples;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Both a Flux and a Mono is a Publisher
 */
@Slf4j
public class FluxAndMonoExample {

    public Flux<String> namesFlux() {
        log.info("Returning a new Flux");
        return Flux.fromIterable(List.of("luke", "leia", "han"))
            .log(); // log all events
    }

    public Mono<String> nameMono() {
        return Mono.just("alice")
            .log();
    }

    public Flux<String> namesFluxMap() {
        return Flux.fromIterable(List.of("luke", "leia", "han"))
            .map(String::toUpperCase)
            .log();
    }

    public Flux<String>  namesFluxImmutable() {
        Flux<String> namesFlux = Flux.fromIterable(List.of("luke", "leia", "han"));
        namesFlux.map(String::toUpperCase);
        return namesFlux; // immutable!
    }

    public static void main(String[] args) {
        FluxAndMonoExample f = new FluxAndMonoExample();
        f.namesFlux().subscribe(
            (String s) -> log.info(s),
            (Throwable t) -> log.info(t.getMessage())
        );

        f.nameMono().subscribe(
            (String s) -> log.info("Mono name is: {}", s),
            (Throwable t) -> log.info(t.getMessage())
        );
    }
}
