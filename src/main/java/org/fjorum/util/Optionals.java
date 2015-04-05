package org.fjorum.util;

import java.util.Optional;
import java.util.function.BiFunction;

public final class Optionals {

    public static <A,B,C> BiFunction<Optional<A>, Optional<B>, Optional<C>> lift2(BiFunction<A,B,C> fn) {
        return (optA, optB) -> optA.flatMap(a -> optB.map(b -> fn.apply(a,b)));
    }
}