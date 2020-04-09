package br.com.moip.mockkid.variable.resolver;

import br.com.moip.mockkid.model.MockkidRequest;
import br.com.moip.mockkid.model.ResponseConfiguration;
import br.com.moip.mockkid.variable.VariableResolver;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class JSONBodyVariableResolver implements VariableResolver {

    private static final Logger logger = LoggerFactory.getLogger(JSONBodyVariableResolver.class);

    @Override
    public boolean canHandle(String variable, HttpServletRequest request) {
        return variable.startsWith("body.") && isJson(request);
    }

    @Override
    public String extract(String variable, ResponseConfiguration responseConfiguration, HttpServletRequest request) {
        return extractValueFromJson(variable, request);
    }

    private boolean isJson(HttpServletRequest request) {
        String header = request.getHeader("content-type");
        return header != null && (header.contains("application/json") || header.contains("text/json"));
    }

    private String extractValueFromJson(String name, HttpServletRequest request) {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(((MockkidRequest) request).getSafeInputStream()));
            JsonObject parse = (JsonObject) new JsonParser().parse(reader);
            String[] nodes = name.replace("body.", "").split("\\.");

            JsonElement jsonElement = parse.get(nodes[0]);
            if (jsonElement instanceof JsonObject && nodes.length>1) {
                JsonObject jsonObject = (JsonObject) jsonElement;
                for (int i=1; i<nodes.length-1; i++) {
                    if (jsonObject == null) return null; //couldn't tread the full path
                    jsonObject = (JsonObject) jsonObject.get(nodes[i]);
                }
                jsonElement = jsonObject.get(nodes[nodes.length - 1]);
            }

            if (jsonElement != null) {
                String jsonVar = jsonElement.getAsString();
                if (jsonVar != null) return jsonVar;
            }
        } catch (Exception e) {
            logger.warn("Couldn't extract variable", e);
        }

        return null;
    }
}
