package org.fjorum.util;

import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.fjorum.util.Optionals.*;
import static org.junit.Assert.*;

public class OptionalsTest {

    @Test
    public void testLift2() throws Exception {
        BiFunction<Optional<Integer>, Optional<Integer>, Optional<Integer>> lifted =
                lift2((x,y) -> x + y);
        assertFalse(lifted.apply(Optional.empty(), Optional.empty()).isPresent());
        assertFalse(lifted.apply(Optional.empty(), Optional.of(13)).isPresent());
        assertFalse(lifted.apply(Optional.of(17), Optional.empty()).isPresent());
        assertEquals(Optional.of(42), lifted.apply(Optional.of(21), Optional.of(21)));
    }

    @Test
    public void testTryIf() throws Exception {
        Function<Integer, Integer> divide10 = x -> 10 / x;
        assertEquals(Optional.of(1), tryIf(divide10).apply(10));
        assertEquals(Optional.empty(), tryIf(divide10).apply(0)); //division by 0
    }

    @Test
    public void testIterable() throws Exception {
        assertFalse(iterable(Optional.empty()).iterator().hasNext());
        assertTrue(iterable(Optional.of(42)).iterator().hasNext());


        Iterator<String> fooIt = iterable(Optional.of("foo")).iterator();
        assertTrue(fooIt.hasNext());
        assertEquals("foo", fooIt.next());
        assertFalse(fooIt.hasNext());

        String p = null;
        for(String s : iterable(Optional.of("foo"))) {
           p = s;
        }
        assertEquals("foo", p);

        String q = null;
        for(String s : iterable(Optional.<String>empty())) {
            q = s;
        }
        assertNull(q);
    }

    @Test(expected = NoSuchElementException.class)
    public void testIterableException() throws Exception {
        iterable(Optional.empty()).iterator().next();
    }

}