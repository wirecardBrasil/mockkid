package br.com.moip.mockkid.facade;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Responsable to organize calls to others components to discover the response
 * Created by zyon.silva on 11/10/17.
 */
@Component
public class MockKidFacade {

    public ResponseEntity discover(HttpServletRequest request){
        // TODO: implement method
        return null;
    }
}
