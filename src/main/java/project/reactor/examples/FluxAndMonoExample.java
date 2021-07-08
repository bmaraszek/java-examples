package project.reactor.examples;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FluxAndMonoExample {

    private static final Logger log = LoggerFactory.getLogger(FluxAndMonoExample.class);

    public Flux<String> namesFlux() {
        log.info("Returning a new Flux");
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
            .log(); // log all events
    }

    public Mono<String> nameMono() {
        return Mono.just("alice")
            .log();
    }

    public Flux<String> namesFluxMap() {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
            .map(String::toUpperCase)
            .log();
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
