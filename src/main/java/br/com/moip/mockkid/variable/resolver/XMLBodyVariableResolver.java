package br.com.moip.mockkid.variable.resolver;

import br.com.moip.mockkid.model.MockkidRequest;
import br.com.moip.mockkid.model.ResponseConfiguration;
import br.com.moip.mockkid.variable.VariableResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

public class XMLBodyVariableResolver implements VariableResolver {

    private static final Logger logger = LoggerFactory.getLogger(XMLBodyVariableResolver.class);

    @Override
    public boolean handles(String variable) {
        return variable.startsWith("body.");
    }

    @Override
    public String extract(String variable, ResponseConfiguration responseConfiguration, HttpServletRequest request) {
        return isXml(request) ? extractValueFromXml(variable, request) : null;
    }

    private boolean isXml(HttpServletRequest request) {
        String header = request.getHeader("content-type");
        return header != null && (header.contains("application/xml") || header.contains("text/xml"));
    }

    private String extractValueFromXml(String name, HttpServletRequest request) {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(((MockkidRequest) request).getSafeInputStream());

            XPath xPath =  XPathFactory.newInstance().newXPath();
            String expression = name.replace("body", "").replaceAll("\\.", "/");
            String result = xPath.compile(expression).evaluate(document);
            if (result.isEmpty()) return null; //empty string can mean node doesn't exist, or value is empty
            return result;
        } catch (Exception e) {
            logger.warn("Couldn't extract variable", e);
        }

        return null;
    }
}
