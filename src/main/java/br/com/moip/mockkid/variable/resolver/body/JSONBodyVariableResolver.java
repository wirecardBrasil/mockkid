package br.com.moip.mockkid.variable.resolver.body;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class JSONBodyVariableResolver {

    private static final Logger logger = LoggerFactory.getLogger(JSONBodyVariableResolver.class);

    public static String extractValueFromJson(String name, HttpServletRequest request) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            JsonObject parse = (JsonObject) new JsonParser().parse(reader);
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
