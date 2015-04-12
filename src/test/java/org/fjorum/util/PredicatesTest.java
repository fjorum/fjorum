package org.fjorum.util;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.fjorum.util.Predicates.and;
import static org.fjorum.util.Predicates.or;


/**
 * @author MrEuler
 */
public class PredicatesTest {

    @Test
    public void testAnd() {
        assertThat("an empty and", and().test(new Object()), is(true));

        assertThat("and true", and((x -> true)).test(new Object()), is(true));
        assertThat("and false", and((x -> false)).test(new Object()), is(false));

        assertThat("true and true", and((x -> true), (x -> true)).test(new Object()), is(true));
        assertThat("true and false", and((x -> true), (x -> false)).test(new Object()), is(false));
        assertThat("false and true", and((x -> false), (x -> true)).test(new Object()), is(false));
        assertThat("false and false", and((x -> false), (x -> false)).test(new Object()), is(false));
    }

    @Test
    public void testOr() {
        assertThat("empty or", or().test(new Object()), is(false));

        assertThat("or true", or((x -> true)).test(new Object()), is(true));
        assertThat("or false", or((x -> false)).test(new Object()), is(false));

        assertThat("true or true", or((x -> true), (x -> true)).test(new Object()), is(true));
        assertThat("true or false", or((x -> true), (x -> false)).test(new Object()), is(true));
        assertThat("false or true", or((x -> false), (x -> true)).test(new Object()), is(true));
        assertThat("false or false", or((x -> false), (x -> false)).test(new Object()), is(false));
    }

}