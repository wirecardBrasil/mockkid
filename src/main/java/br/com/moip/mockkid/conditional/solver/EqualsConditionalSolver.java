package br.com.moip.mockkid.conditional.solver;

import br.com.moip.mockkid.conditional.ConditionalSolver;
import br.com.moip.mockkid.model.Conditional;
import br.com.moip.mockkid.model.ConditionalType;

import java.util.Map;

public class EqualsConditionalSolver implements ConditionalSolver {

    @Override
    public ConditionalType type() {
        return ConditionalType.EQUALS;
    }

    @Override
    public boolean eval(Conditional conditional, Map<String, String> variables) {
        return variables.getOrDefault(conditional.getElement(), "").
                equalsIgnoreCase(conditional.getValue());
    }

}
