package br.com.moip.mockkid.variable.resolver;

import br.com.moip.mockkid.variable.VariableResolver;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class JsonBodyVariableResolver implements VariableResolver {

    private static final Logger logger = LoggerFactory.getLogger(JsonBodyVariableResolver.class);

    @Override
    public boolean handles(String variable) {
        return variable.startsWith("body.");
    }

    @Override
    public String extract(String name, HttpServletRequest request) {
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
                if (jsonVar != null) return jsonVar;
            }
        } catch (Exception e) {
            logger.warn("Couldn't extract variable", e);
        }

        return null;
    }
}
