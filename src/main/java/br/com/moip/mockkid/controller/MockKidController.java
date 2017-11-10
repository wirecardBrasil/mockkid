package br.com.moip.mockkid.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by zyon.silva on 11/10/17.
 */
@Controller
public class MockKidController {

    @RequestMapping(path = "/mockkid", method= RequestMethod.GET)
    public String admin(Model model) {

        model.addAttribute("message", "kkk eai man");

        return "admin";
    }
}
