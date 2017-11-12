package br.com.moip.mockkid.conditional;

import br.com.moip.mockkid.model.Conditional;
import br.com.moip.mockkid.model.ConditionalType;

import java.util.Map;

public interface ConditionalExpression {

    ConditionalType type();

    boolean eval(Conditional conditional, Map<String, String> variables);

}
