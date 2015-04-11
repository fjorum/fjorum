package org.fjorum.util;

import java.util.Optional;
import java.util.function.Predicate;

public class Predicates {

    @SafeVarargs
    public static <A> Predicate<A> or(Predicate<A> ... predicates) {
        Optional<Predicate<A>> result = Optional.empty();
        for(Predicate<A> predicate : predicates) {
            result = result.map(r -> r.or(predicate));
        }
        return result.orElseGet(() -> a -> false);
    }

    @SafeVarargs
    public static <A> Predicate<A> and(Predicate<A> ... predicates) {
        Optional<Predicate<A>> result = Optional.empty();
        for(Predicate<A> predicate : predicates) {
            result = result.map(r -> r.and(predicate));
        }
        return result.orElseGet(() -> a -> true);
    }
}
