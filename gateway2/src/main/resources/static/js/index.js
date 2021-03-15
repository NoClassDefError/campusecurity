function login() {
    var id = $("#userId");
    var password = $("#password");
    var message = $("#message");
    $.post({
        url: window.document.location.href + "/user/login",
        data: {
            userId: id.innerText,
            password: password.innerText
        },
        success: function (msg) {
            //此处返回的是LoginDto2对象
            var loginDto = JSON.parse(msg);
            console.log(loginDto);
            if (loginDto.status === "success") {
                sessionStorage.setItem("loginDto", loginDto);
                window.location.href = "location.html";
            } else {
                message.innerText = "账号或密码错误";
            }
        },
        error: function () {
            message.innerText = "连接失败"
        }
    })
}