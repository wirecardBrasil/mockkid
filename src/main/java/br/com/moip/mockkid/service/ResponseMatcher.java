package br.com.moip.mockkid.service;

import br.com.moip.mockkid.model.Configuration;
import br.com.moip.mockkid.model.Response;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class ResponseMatcher {
    public Response getResponse(Configuration matchedConfig, HttpServletRequest request) {
        //TODO: implement
        return null;
    }
}
