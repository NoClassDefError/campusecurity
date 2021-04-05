var gateway = 'http://localhost:8889'

function login() {
  var id = $("#userId");
  var password = $("#password");
  var message = $("#message");
  $.post({
    url: gateway + "/user/login",
    dataType: 'json',
    contentType: 'application/json',
    data: JSON.stringify({
      'email': id.val(),
      'password': password.val()
    }),
    success: function (msg) {
      console.log(msg);
      //此处返回的是LoginDto2对象
      var loginDto = msg;
      if (loginDto.status === "success") {
        sessionStorage.setItem("loginDto", JSON.stringify(msg));
        window.location.href = "devicemanage.html";
      } else {
        //innerText 火狐不支持 message.innerText = "账号或密码错误";
        message.text("账号或密码错误");
      }
    },
    error: function () {
      message.text("连接失败");
    }
  })
}

function verifyPassword() {
  let password = $('#user-password').val();
  if (password !== $('#user-password2').val())
    $('#message2').text('密码不一致');
  else $('#message2').text("");
}

function send() {
  $.post({
    url: gateway + "/user/send",
    data: {
      email: $('#user-phone').val()
    },
    success: function (res) {
      console.log(res)
      if (res.result.status === 'success') {
        $("#message2").text('邮件已发送，请打开邮箱查看验证码');
      } else $("#message2").text(res.result.info);
    }
  });
}

function addUser() {
  $.post({
    url: gateway + "/user/signUp",
    data: JSON.stringify({
      name: $('#user-name').val(),
      password: $('#user-password').val(),
      code: $('#user-code').val()
    }),
    contentType: 'application/json',
    success: function (res) {
      console.log(res)
      if (res.result.status === 'success') {
        $("#message2").text("添加成功，新用户id：" + res.result.id);
        // layui.table.reload('users', getUsersData());
      } else $("#message2").text(res.result.info);
    }
  });
}
