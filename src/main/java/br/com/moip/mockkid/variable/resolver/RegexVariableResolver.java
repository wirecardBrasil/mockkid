package br.com.moip.mockkid.variable.resolver;

import br.com.moip.mockkid.model.MockkidRequest;
import br.com.moip.mockkid.model.Regex;
import br.com.moip.mockkid.model.ResponseConfiguration;
import br.com.moip.mockkid.variable.VariableResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RegexVariableResolver implements VariableResolver {

    private static final Logger logger = LoggerFactory.getLogger(RegexVariableResolver.class);

    @Override
    public boolean handles(String variable) {
        return variable.startsWith("regex.");
    }

    @Override
    public String extract(String variable, ResponseConfiguration responseConfiguration, HttpServletRequest request) {
        if (responseConfiguration.getRegex() == null) {
            return "";
        }

        Regex regex = responseConfiguration.getRegex();
        if (regex.getExpression() == null || regex.getExpression().isEmpty()) {
            throw new IllegalArgumentException("Regex expression is empty");
        }

        String body = getBody(request);

        Pattern pattern = Pattern.compile(regex.getExpression());
        Matcher matcher = pattern.matcher(body);

        return matchExpressionToRequestBody(matcher);
    }

    private String matchExpressionToRequestBody(Matcher matcher) {
        try {
            if (matcher.find()) {
                return matcher.group(1);
            }
        } catch (IndexOutOfBoundsException ie) {
            throw new IllegalArgumentException("Missing group in regex expression");
        }

        return "";
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
