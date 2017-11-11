package br.com.moip.mockkid.service;

import br.com.moip.mockkid.model.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseEntityFactory {

    public ResponseEntity fromResponse(Response matchedResponse) {
        return ResponseEntity.status(matchedResponse.getStatus()).headers(headersFrom(matchedResponse))
                .body(matchedResponse.getBody());
    }

    private HttpHeaders headersFrom(Response matchedResponse) {
        HttpHeaders headers = new HttpHeaders();
        matchedResponse.getHeaders().keySet().forEach(
                key -> headers.add(key, matchedResponse.getHeaders().get(key))
        );
        return headers;
    }

}
