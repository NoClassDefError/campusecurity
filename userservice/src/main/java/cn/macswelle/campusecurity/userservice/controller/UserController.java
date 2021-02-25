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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(name = "/user")
public class UserController implements UserApi {
    @Autowired
    private LoginService loginService;

    @Autowired
    private SignUpService signUpService;

    @Override
    public Integer getAuth(String userId) {
        return loginService.getAuth(userId);
    }

    @RequestMapping(name = "/login", method = RequestMethod.POST)
    public LoginDto2 login(LoginDto loginDto, HttpServletResponse response) {
        LoginDto2 result = loginService.login(loginDto);
        if (result.getStatus().equals("success")) response.addHeader("user", result.getId());
        return result;
    }

    @RequestMapping(name = "/logout", method = RequestMethod.POST)
    public LogoutDto logoutDto(HttpServletResponse response) {
        LogoutDto result = loginService.logout();
        if (result.getStatus().equals("success")) response.addHeader("user", result.getId());
        return result;
    }

    @RequestMapping(name = "/changePassword", method = RequestMethod.POST)
    public HttpResult changePassword(String original, String newPassword) {
        return signUpService.changePassword(original, newPassword);
    }

    @RequestMapping(name = "/admin/signUp", method = RequestMethod.POST)
    public HttpResult signUp(SignUpDto signUpDto) {
        return signUpService.signUp(signUpDto);
    }

    @RequestMapping(name = "/admin/remove", method = RequestMethod.POST)
    public HttpResult remove(String id) {
        return signUpService.remove(id);
    }

    @RequestMapping(name = "/admin/getUsers", method = RequestMethod.POST)
    public List<UserDto> getUsers() {
        return loginService.getUsers();
    }
}
