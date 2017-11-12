package br.com.moip.mockkid.service;

import br.com.moip.mockkid.model.Configuration;
import br.com.moip.mockkid.model.ResponseConfiguration;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class VariableResolver {

    public Map<String, String> resolveElementVariables(Configuration config, HttpServletRequest request) {
        List<String> elements =
                config.getResponseConfigurations().stream()
                        .filter(m -> m.getConditional() != null)
                        .map(m -> m.getConditional().getElement()).collect(Collectors.toList());
        return resolveVariables(elements, request);
    }

    public Map<String, String> resolveResponseBodyVariables(ResponseConfiguration config, HttpServletRequest request) {
        List<String> names = new ArrayList<>();

        Matcher matcher = Pattern.compile("\\$\\{([a-zA-Z\\.]*)\\}").matcher(config.getResponse().getBody());
        while (matcher.find()) {
            names.add(matcher.group().replace("${", "").replace("}", ""));
        }

        return resolveVariables(names, request);
    }

    private Map<String, String> resolveVariables(List<String> names, HttpServletRequest request) {
        Map<String, String> variables = new HashMap<>();

        for (String name : names) {
            if (name.startsWith("body.")) {
                try {
                    JsonObject parse = (JsonObject) new JsonParser().parse(request.getReader());
                    String[] nodes = name.replace("body.", "").split("\\.");

                    JsonElement jsonElement = parse.get(nodes[0]);
                    if (jsonElement instanceof JsonObject && nodes.length>1) {
                        JsonObject jsonObject = (JsonObject) jsonElement;
                        for (int i=1; i<nodes.length-1; i++) {
                            jsonObject = (JsonObject) jsonObject.get(nodes[i]);
                        }
                        jsonElement = jsonObject.get(nodes[nodes.length - 1]);
                    }

                    if (jsonElement != null) {
                        String jsonVar = jsonElement.getAsString();
                        if (jsonVar != null) variables.put(name, jsonVar);
                    }
                } catch (IOException e) {
                    throw new IllegalArgumentException("Invalid JSON body :/");
                }
            } else if (name.startsWith("headers.")) {
                String header = request.getHeader(name.replace("headers.", ""));
                if (header != null) variables.put(name, header);
            } else if (name.startsWith("url.")) {
                String queryParam = request.getParameter(name.replace("url.", ""));
                if (queryParam != null) variables.put(name, queryParam);
            }
        }

        return variables;
    }

}
