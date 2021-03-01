package cn.macswelle.campusecurity.userservice.controller;

import cn.macswelle.campusecurity.common.dto.requestDto.SignUpDto;
import cn.macswelle.campusecurity.common.dto.responseDto.HttpResult;
import cn.macswelle.campusecurity.common.dto.requestDto.LoginDto;
import cn.macswelle.campusecurity.common.dto.responseDto.LoginDto2;
import cn.macswelle.campusecurity.common.dto.responseDto.LogoutDto;
import cn.macswelle.campusecurity.common.dto.responseDto.UserDto;
import cn.macswelle.campusecurity.feignapi.userservice.UserApi;
import cn.macswelle.campusecurity.userservice.service.LoginService;
import cn.macswelle.campusecurity.userservice.service.SignUpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController implements UserApi {
    @Autowired
    private LoginService loginService;

    @Autowired
    private SignUpService signUpService;

    @RequestMapping(path = "/getUser", method = RequestMethod.POST)
    @ResponseBody
    @Override
    public UserDto getUser(String token) {
        return loginService.getUser(token);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public LoginDto2 login(@RequestBody LoginDto loginDto) {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        LoginDto2 result = loginService.login(loginDto);
        logger.info(result.toString());
//        if (result.getStatus().equals("success")) response.addHeader("user", result.getId());
        return result;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public LogoutDto logoutDto() {
        LogoutDto result = loginService.logout();
//        if (result.getStatus().equals("success")) response.addHeader("user", result.getId());
        return result;
    }

    @Override
    public LoginDto2 changeDescription(String d) {
        return null;
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult changePassword(String original, String newPassword) {
        return signUpService.changePassword(original, newPassword);
    }

    @RequestMapping(value = "/admin/signUp", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult signUp(@RequestBody SignUpDto signUpDto) {
        return signUpService.signUp(signUpDto);
    }

    @RequestMapping(value = "/admin/remove", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult remove(String id) {
        return signUpService.remove(id);
    }

    @RequestMapping(value = "/admin/getUsers", method = RequestMethod.POST)
    @ResponseBody
    public List<UserDto> getUsers() {
        return loginService.getUsers();
    }
}
