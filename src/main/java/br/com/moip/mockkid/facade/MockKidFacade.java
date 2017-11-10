package br.com.moip.mockkid.facade;

import br.com.moip.mockkid.model.Configuration;
import br.com.moip.mockkid.model.Response;
import br.com.moip.mockkid.service.ConfigurationProvider;
import br.com.moip.mockkid.service.ResponseEntityFactory;
import br.com.moip.mockkid.service.ResponseMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Responsable to organize calls to others components to discover the response
 * Created by zyon.silva on 11/10/17.
 */
@Component
public class MockKidFacade {

    @Autowired
    private ConfigurationProvider configurationProvider;
    @Autowired
    private ResponseMatcher responseMatcher;
    @Autowired
    private ResponseEntityFactory responseEntityFactory;

    public ResponseEntity discover(HttpServletRequest request){

        Configuration matchedConfig = configurationProvider.getConfiguration(request);

        Response matchedResponse = responseMatcher.getResponse(matchedConfig, request);

        ResponseEntity result = responseEntityFactory.fromResponse(matchedResponse);

        return result;
    }

}
