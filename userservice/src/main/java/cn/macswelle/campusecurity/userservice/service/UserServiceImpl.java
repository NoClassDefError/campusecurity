package cn.macswelle.campusecurity.userservice.service;

import cn.macswelle.campusecurity.common.dto.responseDto.HttpResult;
import cn.macswelle.campusecurity.common.dto.responseDto.UserDto;
import cn.macswelle.campusecurity.common.entities.User;

import cn.macswelle.campusecurity.common.dto.requestDto.LoginDto;
import cn.macswelle.campusecurity.common.dto.requestDto.SignUpDto;
import cn.macswelle.campusecurity.common.dto.responseDto.LoginDto2;
import cn.macswelle.campusecurity.common.dto.responseDto.LogoutDto;
import cn.macswelle.campusecurity.common.utils.JwtUtil;
import cn.macswelle.campusecurity.common.utils.KeyUtil;
import cn.macswelle.campusecurity.userservice.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class UserServiceImpl implements LoginService, SignUpService {


    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected HttpSession session;

    @Override
    public LoginDto2 login(LoginDto dto) {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        //对象转换
        LoginDto2 loginDto2 = new LoginDto2();
        User result = userRepository.findByNameAndPassword(dto.getUsername(), dto.getPassword()).get(0);
        if (result != null) {
            loginDto2.setStatus("success");
            loginDto2.setId(result.getId());
            loginDto2.setAuth(result.getAuth());
            loginDto2.setDescription(result.getDescription());
            loginDto2.setName(result.getName());
            //记录登录状态，不能像单体应用那样，微服务架构存在session同步问题，要将session存在redis中
            logger.info("access-token: " + JwtUtil.generateToken(result.getId(), "gehanchen"));
            logger.info("登录: " + result.toString());
            session.setAttribute("User", result);
        } else {
            loginDto2.setStatus("failed");
            logger.info("session id: " + session.getId());
            logger.info("登录失败" + dto.toString());
        }
        return loginDto2;
    }

    @Override
    public LogoutDto logout() {
        LogoutDto result = new LogoutDto();
        User user = (User) session.getAttribute("User");
        if (user != null) {
            result.setId(user.getId());
            result.setName(user.getName());
            result.setStatus("success");
            session.removeAttribute("User");
        } else result.setStatus("already has done that");
        return result;
    }

    @Override
    public List<UserDto> getUsers() {
        return null;
    }

    @Override
    public Integer getAuth(String userId) {
        return userRepository.findUserAuthById(userId);
    }

    @Override
    public HttpResult signUp(SignUpDto signUpDto) {

        return null;
    }

    @Override
    public HttpResult remove(String id) {
        return null;
    }

    @Override
    public HttpResult changePassword(String original, String newPassword) {
        return null;
    }
}
