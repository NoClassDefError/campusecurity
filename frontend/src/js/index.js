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
      'userId': id.val(),
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
