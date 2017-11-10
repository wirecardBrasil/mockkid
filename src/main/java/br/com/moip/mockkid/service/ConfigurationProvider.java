package br.com.moip.mockkid.service;

import br.com.moip.mockkid.model.Conditional;
import br.com.moip.mockkid.model.Configuration;
import br.com.moip.mockkid.model.Endpoint;
import br.com.moip.mockkid.model.Method;
import br.com.moip.mockkid.model.Response;
import br.com.moip.mockkid.model.ResponseConfiguration;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class ConfigurationProvider {

    private Map<String, Configuration> configs;

    public ConfigurationProvider(){
        // TODO: change to called method that read config files and genarete map
        configs = mockConfiguration();
    }

    public Configuration getConfiguration(HttpServletRequest request) {
        return configs.get(request.getRequestURI());
    }

    public Map<String, Configuration> getConfigs(){
        return configs;
    }

    private Map<String, Configuration> mockConfiguration() {
        Map<String, Configuration> configs = new HashMap<>();

        Configuration configuration = new Configuration();

        Endpoint endpoint = new Endpoint();
        endpoint.setMethod(Method.GET);
        endpoint.setUrl("/zyon");
        configuration.setEndpoint(endpoint);

        ResponseConfiguration responseConfiguration = new ResponseConfiguration();
        responseConfiguration.setResponse(new Response());
        responseConfiguration.setConditional(new Conditional());
        configuration.setResponseConfigurations(Arrays.asList(responseConfiguration));

        configs.put("/zyon", configuration);

        return configs;
    }
}
