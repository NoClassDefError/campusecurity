package cn.macswelle.campusecurity.userservice.service;

import cn.macswelle.campusecurity.common.dto.requestDto.LoginDto;
import cn.macswelle.campusecurity.common.dto.requestDto.SignUpDto;
import cn.macswelle.campusecurity.common.dto.responseDto.HttpResult;
import cn.macswelle.campusecurity.common.dto.responseDto.LoginDto2;
import cn.macswelle.campusecurity.common.dto.responseDto.LogoutDto;
import cn.macswelle.campusecurity.common.dto.responseDto.UserDto;
import cn.macswelle.campusecurity.common.utils.JwtUtil;
import cn.macswelle.campusecurity.database.entities.User;
import cn.macswelle.campusecurity.database.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class UserServiceImpl implements LoginService, SignUpService {

  @Autowired
  protected UserRepository userRepository;

  @Autowired
  protected HttpServletRequest request;

  private final String secretKey = "gehanchen";

  @Override
  public LoginDto2 login(LoginDto dto) {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    //对象转换
    LoginDto2 loginDto2 = new LoginDto2();
    User result = userRepository.findByPhoneAndPassword(dto.getEmail(), dto.getPassword());
    if (result != null) {
      loginDto2.setStatus("success");
      loginDto2.setAuth(result.getAuth());
      loginDto2.setDescription(result.getDescription());
      loginDto2.setId(result.getId());
      loginDto2.setName(result.getName());
      loginDto2.setPhone(result.getPhone());
      //记录登录状态，不能像单体应用那样，微服务架构存在session同步问题，要将session存在redis中
//            logger.info("access-token: " + token);
      String token = null;
      try {
        token = JwtUtil.generateToken(new ObjectMapper().writeValueAsString(loginDto2), secretKey);
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
      logger.info("登录: " + result.toString() + "token：" + token);
      loginDto2.setToken(token);
    } else {
      loginDto2.setStatus("failed");
      logger.info("登录失败" + dto.toString());
    }
    return loginDto2;
  }

  /**
   * token一次签发，永久有效，无法在后台使其失效，可以命令前端删除，所以后台不需要logout
   */
  @Deprecated
  @Override
  public LogoutDto logout() {
    LogoutDto result = new LogoutDto();
    LoginDto2 user = null;
    try {
      user = new ObjectMapper().readValue(request.getHeader("user"), LoginDto2.class);
    } catch (IOException e) {
      result.setStatus("already has done that");
    }
    if (user != null) {
      result.setId(user.getId());
      result.setName(user.getName());
      result.setStatus("success");
      //不如session, token一次签发，永久有效，无法在后台使其失效，可以命令前端删除
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
    userDto.setAuth(user.getAuth());
    userDto.setDescription(user.getDescription());
    userDto.setId(user.getId());
    userDto.setName(user.getName());
    userDto.setPhone(user.getPhone());
    return userDto;
  }

  @Override
  public UserDto getUser(String token) {
    String uerId = JwtUtil.getUserInfo(token);
    User user = userRepository.findById(uerId).orElse(null);
    return convertUser(user);
  }

  @Autowired
  private RedisService redisService;

  @Override
  public HttpResult signUp(SignUpDto signUpDto) {
    System.out.println(signUpDto.toString());
    if (userRepository.findByPhone(signUpDto.getPhone()).isPresent())
      return new HttpResult("status:failed", "info:该用户已存在");
    int code = redisService.get(signUpDto.getPhone());
    if (signUpDto.getCode() != code) return new HttpResult("status:failed", "info:验证码错误");
    User user = new User();
    user.setPassword(signUpDto.getPassword());
    user.setPhone(signUpDto.getPhone());
    user.setAuth(0);
    String id = userRepository.save(user).getId();
    return new HttpResult("status:success", "id:" + id);
  }

  @Autowired
  private JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String username;

  @Value("${spring.mail.verify}")
  private int verify;

  @Override
  public HttpResult send(String email) {
    if (email == null) return new HttpResult("status:error");
    if (userRepository.findByPhone(email).isPresent())
      return new HttpResult("status:error", "info:该用户已存在");
    Random random = new Random();
    Integer code = random.nextInt(900000) + 100000;
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper;
    try {
      helper = new MimeMessageHelper(message, true);
      helper.setFrom(username);
      helper.setTo(email);
      helper.setSubject("校园智能安防系统 用户注册");
      helper.setText("<h3>请在" + verify / 60 + "分钟内输入验证码 <b>"
        + code + "</b> ，若非本人操作请忽略(๑´ڡ`๑)</h3>", true);
      Runnable runnable = () -> {
        mailSender.send(message);
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info("Email send!");
      };
      new Thread(runnable).start();
    } catch (MessagingException e) {
      e.printStackTrace();
      return new HttpResult("status:error");
    }
    redisService.set(email, code, verify);
    return new HttpResult("status:success");
  }

  @Override
  public HttpResult changeUser(UserDto signUpDto) {
    System.out.println(signUpDto.toString());
    if (signUpDto.getAuth() < 0 || signUpDto.getAuth() > 2)
      return new HttpResult("info:权限设置错误", "status:error");
    userRepository.updateUser(signUpDto.getId(), signUpDto.getName(),
      signUpDto.getDescription(), signUpDto.getAuth(), signUpDto.getPhone());
    return new HttpResult("status:success");
  }

  @Override
  public HttpResult remove(String id) {
    return null;
  }

  @Override
  public HttpResult changePassword(String original, String newPassword) {
    LoginDto2 user = getUserFromToken();
    if (user == null)
      return new HttpResult("status:error");
    User user1 = userRepository.findByIdAndPassword(user.getId(), original);
    if (user1 == null) return new HttpResult("status:error");
    else {
      userRepository.changePassword(user.getId(), newPassword);
      return new HttpResult("status:success");
    }
  }

  @Override
  public HttpResult changeDescription(String d) {
//    System.out.println(d);
    LoginDto2 user = getUserFromToken();
    if (user == null)
      return new HttpResult("status:error");
    userRepository.changeDescription(user.getId(), d);
    return new HttpResult("status:success");
  }

  private LoginDto2 getUserFromToken() {
    LoginDto2 user;
    try {
      user = new ObjectMapper().readValue(request.getHeader("user"), LoginDto2.class);
    } catch (IOException e) {
      return null;
    }
    return user;
  }
}
