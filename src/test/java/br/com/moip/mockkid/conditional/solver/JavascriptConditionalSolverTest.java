package br.com.moip.mockkid.conditional.solver;

import br.com.moip.mockkid.model.Conditional;
import br.com.moip.mockkid.model.ConditionalType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JavascriptConditionalSolverTest {

    private JavascriptConditionalSolver jsConditionalSolver;

    @Before
    public void setUp() {
        jsConditionalSolver = new JavascriptConditionalSolver();
    }

    @Test
    public void shouldHandleOnlyTheContainsType() {
        Assert.assertEquals(jsConditionalSolver.type(), ConditionalType.JAVASCRIPT);
    }

    @Test
    public void evalShouldReturnTrue() {
        Map<String, String> variables = of("myvar", "value");
        Conditional conditional = new Conditional(ConditionalType.JAVASCRIPT, "${myvar} == \"value\"");

        assertTrue(jsConditionalSolver.eval(conditional, variables));
    }

    @Test
    public void evalShouldReturnFalse() {
        Map<String, String> variables = of("myvar", "othervalue");
        Conditional conditional = new Conditional(ConditionalType.JAVASCRIPT, "${myvar} == \"value\"");

        assertFalse(jsConditionalSolver.eval(conditional, variables));
    }

    @Test
    public void shouldFailOnUnknownVariable() {
        Map<String, String> variables = of("myvar", "othervalue");
        Conditional conditional = new Conditional(ConditionalType.JAVASCRIPT, "${myvar} == \"value\" && " +
                " ${othervar} == \"missing\" ");

        assertFalse(jsConditionalSolver.eval(conditional, variables));
    }

}
