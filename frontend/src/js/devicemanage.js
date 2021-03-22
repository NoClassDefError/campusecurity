let userDto;
let locationId;
const gateway = 'http://localhost:8889';

window.onload = function () {
  let locationPanel = $("#location-panel");//设备表,0,1,2
  let userManage = $("#user-manage");//管理用户的表单,0
  let locationForm = $('#location-form');//添加地点的表单,0,1
  locationPanel.toggle();
  userManage.toggle();
  locationForm.toggle();

  userDto = JSON.parse(sessionStorage.getItem("loginDto"));
  if (userDto == null) {
    window.alert("登录失败");
    location.href = "index.html";
  }
  $("#userId").text(userDto.id);
  $("#username").text(userDto.name);
  $("#userAuth").text(userDto.auth);
  $("#userDes").text(userDto.description);
  console.log(userDto.token);

  if (userDto.auth !== '只读') {
    locationForm.show();
  }

  //用户管理
  if (userDto.auth === '超级管理员') {
    userManage.show();
    layui.use(['table'], function () {
      const table = layui.table;
      table.render(getUsersData());
      table.on('edit(users)', function (obj) {
        const auth = judgeAuth(obj.data.auth);
        if (auth !== -1) {
          $.post({
            url: gateway + "/user/admin/signUp",
            data: JSON.stringify({
              "id": obj.data.id,
              "name": obj.data.name,
              "description": obj.data.description,
              "auth": auth
            }),
            contentType: 'application/json',
            headers: {
              "access-token": userDto.token
            },
            success: function (res) {
              if (res.status === 'modify') {
                $('#message4').text('修改成功');
                table.reload();
              }
            }
          })
        } else {
          $('#message4').text('修改失败，用户权限只能为超级管理员、管理员或者只读')
        }
      })
    });
  }

  //地点管理
  layui.use(['table', 'laydate'], function () {
    const locations = layui.table;
    locations.render(getLocationsData());
    locations.on('rowDouble(test)', function (obj) {
      const location = obj.data;
      console.log("locations: rowDouble " + location);
      locationPanel.show();
      // sessionStorage.setItem("location", location);
      // window.location.href = "location.html";
      locationId = location.id;
      $('#current-location').text(location.id + " " + location.name);
      locations.render(getDevicesData(locationId));
      let laydate = layui.laydate;
      laydate.render({elem: '#start'});
      laydate.render({elem: '#end'});
    });
    if (userDto.auth !== '只读') {
      locations.on('edit(test)', function (obj) {
        $.post({
          url: gateway + "/device/admin/changeLocation",
          data: JSON.stringify({
            id: obj.data.id,
            name: obj.data.name,
            des: obj.data.description
          }),
          contentType: 'application/json',
          headers: {
            "access-token": userDto.token
          },
          success: function () {
            window.alert('修改成功');
            layui.table.reload(getLocationsData());
          },
          error: function () {
            window.alert('修改失败');
          }
        });
      });
    }
  });
};

function getUsersData() {
  return {
    elem: '#users',
    method: 'post',
    url: gateway + "/user/admin/getUsers",
    parseData: function (res) {
      return {
        "code": 0,
        "message": "",
        "count": Object.keys(res).length,
        "data": res
      }
    },
    headers: {
      "access-token": userDto.token
    },
    page: true,
    cols: [[
      {field: 'id', title: 'ID', width: 80, sort: true, fixed: 'left'},
      {field: 'name', title: '名称', width: 80, edit: 'text'},
      {field: 'description', title: '描述', width: 500, edit: 'text'},
      {field: 'auth', title: '权限', width: 120, edit: 'text'},
    ]]
  }
}

function getLocationsData() {
  return {
    elem: '#locations',
    method: 'post',
    url: gateway + "/device/getLocations",
    parseData: function (res) {
      return {
        "code": 0,
        "message": "",
        "count": Object.keys(res).length,
        "data": res
      }
    },
    headers: {
      "access-token": userDto.token
    },
    page: true,
    cols: [[
      {field: 'id', title: 'ID', width: 80, sort: true, fixed: 'left'},
      {field: 'name', title: '名称', width: 80, edit: 'text'},
      {field: 'description', title: '位置', width: 500, edit: 'text'},
      {field: 'deviceNum', title: '运行设备数', width: 120, sort: true},
      {field: 'record', title: '日志数', width: 120}
    ]]
  };
}

function getDevicesData(locationId) {
  return {
    elem: '#devices',
    url: gateway + "/device/getDeviceByLocation",
    page: true,
    where: {id: locationId},
    parseData: function (res) {
      return {
        "code": 0,
        "message": "",
        "count": Object.keys(res).length,
        "data": res
      }
    },
    headers: {
      "access-token": userDto.token
    },
    method: 'post',
    cols: [[{field: 'id', title: '设备ID', width: 100, sort: true, fixed: 'left'},
      {field: 'name', title: '名称', width: 100},
      {field: 'version', title: '软件版本', width: 100},
      {field: 'category', title: '类型', width: 80},
      {field: 'description', title: '描述', width: 300},
      {field: 'url', title: 'URL', width: 600}
    ]]
  };
}

function changeDescription() {
  $.post({
    url: gateway + "/user/changeDescription",
    data: {
      d: $("#changeDes").val()
    },
    headers: {
      "access-token": userDto.token
    },
    success: function (res) {
      console.log(res)
      if (res.result.status === 'success') {
        userDto.description = $("#changeDes").val();
        sessionStorage.setItem("loginDto", JSON.stringify(userDto))
        window.alert('修改成功');
        location.reload();
      }
    }
  })
}

function changePassword() {
  $.post({
    url: gateway + "/user/changePassword",
    headers: {
      "access-token": userDto.token
    },
    data: {
      original: $('#origin').val(),
      newPassword: $('#newP').val()
    },
    success: function (res) {
      if (res.result.status === 'success') {
        window.alert('密码修改成功，请重新登录');
        logout()
      }else window.alert('密码错误')
    }
  });
}

function getRecords() {
  layui.table.render({
    elem: "#records",
    method: 'post',
    where: {
      locationId: locationId,
      start: new Date($('#start').val().replace(new RegExp("-", "gm"), "/")).getTime(),
      end: new Date($('#end').val().replace(new RegExp("-", "gm"), "/")).getTime()
    },
    headers: {
      "access-token": userDto.token
    },
    url: gateway + "/device/getRecords",
    parseData: function (res) {
      return {
        "code": 0,
        "message": "",
        "count": Object.keys(res).length,
        "data": res
      }
    },
    page: true,
    cols: [[{field: 'id', title: 'ID', width: 80, sort: true, fixed: 'left'},
      {field: 'time', title: '时间', width: 80},
      {field: 'description', title: '描述', width: 177},
      {field: 'personnel', title: '人物id', width: 80},
      {field: 'url', title: '文件记录URL', width: 177},
      {field: 'personName', title: '人物名称', width: 120}]]
  });
}

function addLocation() {
  $.post({
    url: gateway + "/device/admin/changeLocation",
    data: {
      name: $('#name').val(),
      description: $('#des').val()
    },
    headers: {
      "access-token": userDto.token
    },
    success: function (res) {
      console.log(res.result.status);
      if (res.result.status === "add") {
        console.log(res);
        $("#tableMessage").text('添加成功');
        layui.table.reload('locations', getLocationsData());
      }
    }
  });
}

function addUser() {
  $.post({
    url: gateway + "/user/admin/signUp",
    data: {
      name: $('#user-name').val(),
      description: $('#user-description').val(),
      auth: judgeAuth($('#user-auth').val())
    },
    headers: {
      "access-token": userDto.token
    },
    success: function (res) {
      if (res.status === 'add') {
        $("#message4").text("添加成功，新用户id：" + res.id + " 初始密码：123456");
        layui.table.reload('users', getUsersData());
      }
    }
  });
}

function judgeAuth(auth) {
  if (auth === "超级管理员") return 0;
  if (auth === "管理员") return 1;
  if (auth === "只读") return 2;
  return -1;
}

function logout() {
  sessionStorage.clear();
  window.location.href = "index.html";
}
