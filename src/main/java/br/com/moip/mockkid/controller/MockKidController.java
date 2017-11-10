package br.com.moip.mockkid.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by zyon.silva on 11/10/17.
 */
@Controller
public class MockKidController {

    @RequestMapping("/mockkid")
    public String pageAdmin(){

        return "index";
    }
}
