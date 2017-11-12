package br.com.moip.mockkid.conditional;

import br.com.moip.mockkid.conditional.solver.ContainsConditionalSolver;
import br.com.moip.mockkid.model.Conditional;
import br.com.moip.mockkid.model.ConditionalType;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ContainsConditionalSolverTest {

    private ContainsConditionalSolver containsConditionalExpression;

    @Before
    public void setUp() {
        containsConditionalExpression = new ContainsConditionalSolver();
    }

    @Test
    public void shouldHandleOnlyTheContainsType() {
        assertEquals(containsConditionalExpression.type(), ConditionalType.CONTAINS);
    }

    @Test
    public void evalShouldReturnTrue() {
        Map<String, String> variables = of("myvar", "avalueb");
        Conditional conditional = new Conditional(ConditionalType.CONTAINS, "myvar", "value");

        assertTrue(containsConditionalExpression.eval(conditional, variables));
    }

    @Test
    public void evalShouldReturnFalse() {
        Map<String, String> variables = of("myvar", "nope");
        Conditional conditional = new Conditional(ConditionalType.CONTAINS, "myvar", "value");

        assertFalse(containsConditionalExpression.eval(conditional, variables));
    }

    @Test
    public void evalShouldReturnFalseOnUnknownVariable() {
        Map<String, String> variables = of("anothervar", "nope");
        Conditional conditional = new Conditional(ConditionalType.CONTAINS, "myvar", "value");

        assertFalse(containsConditionalExpression.eval(conditional, variables));
    }

}
