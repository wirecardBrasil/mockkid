package br.com.moip.mockkid.conditional;

import br.com.moip.mockkid.model.Conditional;
import br.com.moip.mockkid.model.ConditionalType;

import java.util.Map;

public class EqualsCondition implements ConditionalExpression {

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
