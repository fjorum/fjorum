package org.fjorum.util;

import java.util.function.Predicate;

public class Predicates {

    @SafeVarargs
    public static <A> Predicate<A> or(Predicate<A>... predicates) {
        Predicate<A> result = a -> false;
        for (Predicate<A> predicate : predicates) {
            result = result.or(predicate);
        }
        return result;
    }

    @SafeVarargs
    public static <A> Predicate<A> and(Predicate<A>... predicates) {
        Predicate<A> result = a -> true;
        for (Predicate<A> predicate : predicates) {
            result = result.and(predicate);
        }
        return result;
    }
}
