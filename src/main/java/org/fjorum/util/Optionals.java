package org.fjorum.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class Optionals {

    public static <A, B, C> BiFunction<Optional<A>, Optional<B>, Optional<C>> lift2(BiFunction<A, B, C> fn) {
        return (optA, optB) -> optA.flatMap(a -> optB.map(b -> fn.apply(a, b)));
    }

    public static <A,B> Function<A,Optional<B>> tryIf(Function<A,B> fn) {
        return a -> {
            try {
                return Optional.ofNullable(fn.apply(a));
            } catch (RuntimeException ex) {
                return Optional.empty();
            }
        };
    }

    public static <A> Iterable<A> iterable(Optional<A> optional) {
        return () -> new Iterator<A>() {

            private Optional<A> opt = optional;

            @Override
            public boolean hasNext() {
                return opt.isPresent();
            }

            @Override
            public A next() {
                A a = opt.orElseThrow(NoSuchElementException::new);
                opt = Optional.empty();
                return a;
            }
        };
    }
}