package br.com.moip.mockkid.service;

import br.com.moip.mockkid.model.MockkidRequest;
import br.com.moip.mockkid.model.Regex;
import br.com.moip.mockkid.model.ResponseConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class RegexResolver {

    private static final Logger logger = LoggerFactory.getLogger(RegexResolver.class);

    public Map<String, String> resolve(ResponseConfiguration config, HttpServletRequest request) {
        if (config.getRegex() == null) {
            return Collections.emptyMap();
        }

        Regex regex = config.getRegex();
        if (regex.getExpression() == null || regex.getExpression().isEmpty()) {
            throw new IllegalArgumentException("Regex expression is empty");
        }

        String body = getBody(request);

        Pattern pattern = Pattern.compile(regex.getExpression());
        Matcher matcher = pattern.matcher(body);

        return matchExpressionToRequestBody(regex, matcher);
    }

    private Map<String, String> matchExpressionToRequestBody(Regex regex, Matcher matcher) {
        try {
            if (matcher.find()) {
                Map<String, String> resolvedMap = new HashMap<>();

                String group = matcher.group(1);
                resolvedMap.put(regex.getPlaceholder(), group);

                return resolvedMap;
            }
        } catch (IndexOutOfBoundsException ie) {
            throw new IllegalArgumentException("Missing group in regex expression");
        }

        return Collections.emptyMap();
    }

    private String getBody(HttpServletRequest request) {
        try {
            InputStream inputStream = ((MockkidRequest) request).getSafeInputStream();
            return readFromInputStream(inputStream);
        } catch (IOException e) {
            logger.error("Cannot extract body", e);
        }

        return null;
    }

    private String readFromInputStream(InputStream input) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }

}
