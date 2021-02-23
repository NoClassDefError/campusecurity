package cn.macswelle.campusecurity.nvrlistener.controller;

import cn.macswelle.campusecurity.nvrlistener.Info;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NvrListenerController {

    @RequestMapping("/getInfo")
    public Info getInfo() {
        return new Info();
    }


}
