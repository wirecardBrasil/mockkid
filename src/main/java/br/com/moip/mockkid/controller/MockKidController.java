package br.com.moip.mockkid.controller;

import br.com.moip.mockkid.facade.MockKidFacade;
import br.com.moip.mockkid.model.Configuration;
import br.com.moip.mockkid.model.MockkidRequest;
import br.com.moip.mockkid.provider.ConfigurationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MockKidController {

    @Autowired
    private MockKidFacade facade;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @RequestMapping(path = "/mockkid", method= RequestMethod.GET)
    public String admin(Model model) {
        model.addAttribute("configs", configurationProvider.getConfigs().values());
        return "index";
    }

    @RequestMapping(path = "/mockkid", method = RequestMethod.PUT)
    public String editConfig(@RequestBody  Configuration configuration){
        //TODO: implement methods
        return "index";
    }

    @RequestMapping("/**")
    public ResponseEntity mocks(HttpServletRequest request){
        return facade.discover(new MockkidRequest(request));
    }

}
