package project.reactor.examples;

import reactor.core.publisher.Flux;

public class FluxAndMono {
    public static void main(String[] args) {
        System.out.println("demo");
        Flux<String> f = Flux.fromArray(new String[] {"one", "two", "three"});
        f.map(x -> x + " - processed").subscribe(
            (String s) -> System.out.printf("%s\n", s),
            (Throwable t) -> {}
        );
    }
}
