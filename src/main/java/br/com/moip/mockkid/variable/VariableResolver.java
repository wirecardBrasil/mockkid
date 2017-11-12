package br.com.moip.mockkid.variable;

import javax.servlet.http.HttpServletRequest;

public interface VariableResolver {

    boolean handles(String variable);

    String extract(String variable, HttpServletRequest request);

}
