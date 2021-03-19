var userDto;
var locationId;
var gateway='http://localhost:8889'
window.onload = function () {
    userDto = sessionStorage.getItem("loginDto");
    $("#userId").innerText = userDto.id;
    $("#username").innerText = userDto.name;
    $("#userAuth").innerText = userDto.auth;
    $("#userDes").innerText = userDto.description;
    layui.use(['table', 'laydate'], function () {
        var table = layui.table;
        table.render({
            elem: '#locations',
            height: 312,
            method: 'post',
            url: gateway + "/device/getLocations", //数据接口
            page: true, //开启分页
            cols: [[ //表头
                {field: 'id', title: 'ID', width: 80, sort: true, fixed: 'left'},
                {field: 'name', title: '名称', width: 80, edit: 'text'},
                {field: 'description', title: '描述', width: 80, edit: 'text'},
                {field: 'deviceNum', title: '运行设备数', width: 80, sort: true},
                {field: 'record', title: '日志数', width: 177}
            ]]
        });
        table.on('rowDouble(test)', function (obj) {
            var location = obj.data;
            console.log(location);
            // sessionStorage.setItem("location", location);
            // window.location.href = "location.html";
            locationId = location.id;
            $('#current-location').innerText = location.id + " " + location.name;
            table.render({
                elem: '#devices',
                url: gateway + "/device/getDeviceByLocation",
                page: true,
                where: {id: locationId},
                cols: [[{field: 'id', title: '设备ID', width: 80, sort: true, fixed: 'left'},
                    {field: 'name', title: '名称', width: 80},
                    {field: 'description', title: '描述', width: 80},
                    {field: 'category', title: '类型', width: 80},
                    {field: 'url', title: 'URL', width: 177},
                    {field: 'version', title: '软件版本', width: 80}]]
            });
            var laydate = layui.laydate;
            laydate.render({elem: '#start'});
            laydate.render({elem: '#end'});
        });
        table.on('edit(test)', function (obj) {
            $.post({
                url: gateway + "/device/changeLocation",
                data: {
                    id: obj.data.id,
                    name: obj.data.name,
                    des: obj.data.description
                },
                success: function () {
                    $("#tableMessage").innerText = '修改成功';
                    window.reload();
                },
                error: function () {
                    $("#tableMessage").innerText = '修改失败';
                }
            });
        });
    });
};

function changeDescription() {
    $.post({
        url: gateway + "/user/changeDescription",
        data: {
            d: $("#changeDes").innerText
        },
        success: function (res) {
            if (res.result.get(0) === 'success') {
                window.alert('修改成功');
                window.reload();
            }
        }
    })
}

function changePassword() {
    $.post({
        url: gateway + "/user/changePassword",
        data: {
            original: $('#origin').val(),
            newPassword: $('#newP').val()
        },
        success: function (res) {
            if (res.result.get(0) === 'success') {
                window.alert('密码修改成功，请重新登录');
                sessionStorage.clear();
                window.location.href = 'index.html';
            }
        }
    });
}

function getRecords() {
    layui.table.render({
        elem: "#records",
        method: 'post',
        where: {
            locationId: locationId,
            start: $('#start').val(),
            end: $('#end').val()
        },
        url: gateway + "/device/getRecords",
        page: true,
        cols: [[{field: 'id', title: 'ID', width: 80, sort: true, fixed: 'left'},
            {field: 'time', title: '时间', width: 80},
            {field: 'description', title: '描述', width: 177},
            {field: 'personnel', title: '人物id', width: 80},
            {field: 'url', title: '文件记录URL', width: 177},
            {field: 'personName', title: '人物名称', width: 80}]]
    })
}

function addLocation() {
    $.post({
        url: gateway + "/device/changeLocation",
        data: {
            name: $('#name').val(),
            description: $('#des').val()
        },
        success: function (res) {
            if (res === 'success') {
                window.alert('修改成功');
                window.reload();
            }
        }
    });
}
