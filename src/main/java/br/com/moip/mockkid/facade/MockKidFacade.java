package br.com.moip.mockkid.facade;

import br.com.moip.mockkid.model.Configuration;
import br.com.moip.mockkid.model.MockkidRequest;
import br.com.moip.mockkid.model.Response;
import br.com.moip.mockkid.provider.ConfigurationProvider;
import br.com.moip.mockkid.service.ResponseEntityFactory;
import br.com.moip.mockkid.service.ResponseMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class MockKidFacade {

    private static final Logger logger = LoggerFactory.getLogger(MockKidFacade.class);

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private ResponseMatcher responseMatcher;

    @Autowired
    private ResponseEntityFactory responseEntityFactory;

    public ResponseEntity discover(MockkidRequest request) {
        logger.info("Processing new request for URI: {}", request.getRequestURI());

        Configuration matchedConfig = configurationProvider.getConfiguration(request);

        logger.info("Matched Configuration: {}", matchedConfig);

        Response matchedResponse = responseMatcher.getResponse(matchedConfig, request);

        return responseEntityFactory.fromResponse(matchedResponse);
    }

}
