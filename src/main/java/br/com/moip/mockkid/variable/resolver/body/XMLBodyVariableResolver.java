package br.com.moip.mockkid.variable.resolver.body;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

public class XMLBodyVariableResolver {

    private static final Logger logger = LoggerFactory.getLogger(XMLBodyVariableResolver.class);

    public static String extractValueFromXml(String name, HttpServletRequest request) {
        try {
            DocumentBuilderFactory builderFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(request.getInputStream());

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
