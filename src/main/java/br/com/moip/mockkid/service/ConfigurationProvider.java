package br.com.moip.mockkid.service;

import br.com.moip.mockkid.configuration.Configurations;
import br.com.moip.mockkid.model.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class ConfigurationProvider {

    @Autowired
    private Configurations configurations;

    public Configuration getConfiguration(HttpServletRequest request) {
        return configurations.get(request.getMethod() + ":" + request.getRequestURI());
    }

    public Configurations getConfigs(){
        return configurations;
    }

}
