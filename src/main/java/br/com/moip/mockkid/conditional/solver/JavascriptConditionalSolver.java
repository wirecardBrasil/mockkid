package br.com.moip.mockkid.conditional.solver;

import br.com.moip.mockkid.conditional.ConditionalSolver;
import br.com.moip.mockkid.model.Conditional;
import br.com.moip.mockkid.model.ConditionalType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Map;

public class JavascriptConditionalSolver implements ConditionalSolver {

    private Logger logger = LoggerFactory.getLogger(JavascriptConditionalSolver.class);

    @Override
    public ConditionalType type() {
        return ConditionalType.JAVASCRIPT;
    }

    @Override
    public boolean eval(Conditional conditional, Map<String, String> variables) {
        try {
            return (boolean) buildJSEngine(variables).eval(normalize(conditional, variables));
        } catch (Exception e) {
            logger.error("Error evaluating javascript function: {} -> message: {}", conditional.getEval(), e.getMessage());
            return false;
        }
    }

    /**
     *  Builds a ScriptEngine where the variables can be used within
     * @param variables Map with variables and it's values
     * @return ScriptEngine
     */
    private ScriptEngine buildJSEngine(Map<String, String> variables) {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
        variables.forEach((k,v) -> engine.put(k.replaceAll("[\\.\\-]", "_"), v));
        return engine;
    }

    /**
     *  Normalize variables like ${headers.auth} to only headers_auth
     * @param conditional The Conditional with the eval field to be normalized
     * @param variables Map with variables and it's values
     * @return normalized expression
     */
    private String normalize(Conditional conditional, Map<String, String> variables) {
        String eval = conditional.getEval();
        for (String variable : variables.keySet()) {
            eval = eval.replace("${" + variable + "}", variable.replaceAll("[\\.\\-]", "_"));
        }
        return eval;
    }

}
