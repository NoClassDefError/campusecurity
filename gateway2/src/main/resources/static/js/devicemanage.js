var userDto;
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
            url: window.document.location.href + "/device/getLocations", //数据接口
            page: true, //开启分页
            cols: [[ //表头
                {field: 'id', title: 'ID', width: 80, sort: true, fixed: 'left'}
                , {field: 'name', title: '名称', width: 80, edit: 'text'}
                , {field: 'description', title: '描述', width: 80, edit: 'text'}
                , {field: 'deviceNum', title: '运行设备数', width: 80, sort: true}
                , {field: 'record', title: '日志数', width: 177}
            ]]
        });
        table.on('rowDouble(test)', function (obj) {
            var location = obj.data;
            console.log(location);
            // sessionStorage.setItem("location", location);
            // window.location.href = "location.html";
            $('#current-location').innerText = location.id + " " + location.name;
            table.render({
                elem: '#devices',
                url: "",
                page: true,
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
                url: window.document.location.href + "/device/changeLocation",
                data: {
                    id: obj.data.id,
                    name: obj.data.name,
                    des: obj.data.description
                },
                success: function () {
                    $("#tableMessage").innerText = '修改成功'
                },
                error: function () {
                    $("#tableMessage").innerText = '修改失败'
                }
            });
        });
    });
};

function changeDescription() {
    $.post({
        url: window.document.location.href + "/user/changeDescription",
        data: {
            d: $("#changeDes").innerText
        },
        success: function () {

        }
    })
}

function changePassword() {
    $.post({
        url: window.document.location.href + "/user/changePassword",
        success: function () {

        }
    })
}

function getRecords() {

}

function addLocation() {

}