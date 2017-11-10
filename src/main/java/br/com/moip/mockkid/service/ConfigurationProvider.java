package br.com.moip.mockkid.service;

import br.com.moip.mockkid.model.Configuration;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class ConfigurationProvider {

    public Configuration getConfiguration(HttpServletRequest request) {
        //TODO: implementar
        return null;
    }
}
