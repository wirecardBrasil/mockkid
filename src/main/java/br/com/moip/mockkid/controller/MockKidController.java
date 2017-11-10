package br.com.moip.mockkid.controller;

import br.com.moip.mockkid.facade.MockKidFacade;
import br.com.moip.mockkid.service.ConfigurationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Mapping Endpoints
 * Created by zyon.silva on 11/10/17.
 */
@Controller
public class MockKidController {

    @Autowired
    private MockKidFacade facade;

    @Autowired
    private ConfigurationProvider configurationProvider;
    @RequestMapping(path = "/mockkid", method= RequestMethod.GET)
    public String admin(Model model) {

        model.addAttribute("configs", configurationProvider.getConfigs().values());

        return "admin";
    }

    @RequestMapping("/*")
    public ResponseEntity mocks(HttpServletRequest request){
        return facade.discover(request);
    }
}
