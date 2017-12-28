package br.com.moip.mockkid.util;

import br.com.moip.mockkid.model.MockkidRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class MockkidRequestUtil {

    private static final Logger logger = LoggerFactory.getLogger(MockkidRequestUtil.class);

    public static String getBody(HttpServletRequest request) {
        try {
            InputStream inputStream = ((MockkidRequest) request).getSafeInputStream();
            return readFromInputStream(inputStream);
        } catch (IOException e) {
            logger.error("Cannot extract body", e);
        }

        return null;
    }

    private static String readFromInputStream(InputStream input) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }

}
