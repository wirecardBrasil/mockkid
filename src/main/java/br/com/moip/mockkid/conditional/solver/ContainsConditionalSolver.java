package br.com.moip.mockkid.conditional.solver;

import br.com.moip.mockkid.conditional.ConditionalSolver;
import br.com.moip.mockkid.model.Conditional;
import br.com.moip.mockkid.model.ConditionalType;

import java.util.Map;

public class ContainsConditionalSolver implements ConditionalSolver {

    @Override
    public ConditionalType type() {
        return ConditionalType.CONTAINS;
    }

    @Override
    public boolean eval(Conditional conditional, Map<String, String> variables) {
        String value = variables.get(conditional.getElement()) != null ? variables.get(conditional.getElement()) : "";
        return value.toLowerCase().contains(conditional.getValue().toLowerCase());
    }

}
