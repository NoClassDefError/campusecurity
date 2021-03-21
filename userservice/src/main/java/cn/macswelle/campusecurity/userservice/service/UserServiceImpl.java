package cn.macswelle.campusecurity.userservice.service;

import cn.macswelle.campusecurity.common.dto.responseDto.HttpResult;
import cn.macswelle.campusecurity.common.dto.responseDto.UserDto;
import cn.macswelle.campusecurity.database.entities.User;

import cn.macswelle.campusecurity.common.dto.requestDto.LoginDto;
import cn.macswelle.campusecurity.common.dto.requestDto.SignUpDto;
import cn.macswelle.campusecurity.common.dto.responseDto.LoginDto2;
import cn.macswelle.campusecurity.common.dto.responseDto.LogoutDto;
import cn.macswelle.campusecurity.common.utils.JwtUtil;

import cn.macswelle.campusecurity.database.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
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
    List<User> result = userRepository.findByIdAndPassword(dto.getUserId(), dto.getPassword());
    if (result != null && result.size() == 1) {
      loginDto2.setStatus("success");
//            String token = JwtUtil.generateToken(result.get(0).getId(), "gehanchen");
//            loginDto2.setAuth(result.get(0).getAuth());
      switch (result.get(0).getAuth()) {
        case 0:
          loginDto2.setAuth("超级管理员");
          break;
        case 1:
          loginDto2.setAuth("管理员");
          break;
        case 2:
          loginDto2.setAuth("只读");
          break;
      }
      loginDto2.setDescription(result.get(0).getDescription());
      loginDto2.setId(result.get(0).getId());
      loginDto2.setName(result.get(0).getName());
      //记录登录状态，不能像单体应用那样，微服务架构存在session同步问题，要将session存在redis中
//            logger.info("access-token: " + token);
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
    List<UserDto> result = new ArrayList<>();
    userRepository.findAll().forEach(user -> result.add(convertUser(user)));
    return result;
  }

  private UserDto convertUser(User user) {
    UserDto userDto = new UserDto();
    switch (user.getAuth()) {
      case 0:
        userDto.setAuth("超级管理员");
        break;
      case 1:
        userDto.setAuth("管理员");
        break;
      case 2:
        userDto.setAuth("只读");
        break;
    }
    userDto.setDescription(user.getDescription());
    userDto.setId(user.getId());
    userDto.setName(user.getName());
    return userDto;
  }

  @Override
  public UserDto getUser(String token) {
    String uerId = JwtUtil.getUserInfo(token);
    User user = userRepository.findById(uerId).orElse(null);
    return convertUser(user);
  }

  @Override
  public HttpResult signUp(SignUpDto signUpDto) {
    if (signUpDto.getId() == null) {
      User user = new User();
      user.setAuth(signUpDto.getAuth());
      user.setDescription(signUpDto.getDescription());
      user.setName(signUpDto.getName());
      String id = userRepository.save(user).getId();
      return new HttpResult("status:add", "id:" + id);
    } else {
      userRepository.updateUser(signUpDto.getId(), signUpDto.getName(),
        signUpDto.getDescription(), signUpDto.getAuth());
      return new HttpResult("status:modify", "id:" + signUpDto.getId());
    }
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
