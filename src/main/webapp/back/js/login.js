$(document).ready(function () {
    let form = layui.form;
    form.on('submit(login)', function(data){
        var index
        $.ajax({
            url:config.url+"/user?method=login",
            type:"post",
            data:data.field,
            // beforeSend:function (xhr) {
            //     xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded")
            // },
            timeout:3000,
            success:function (res) {
                layer.close(index)
                index =layer.load(2, {time: 2*1000})
                if(res.code==1){
                    console.log(res.data)
                    location.href=config.url+"/back/index.html";

                }else {
                    layer.msg(res.msg);
                    layer.close(index)
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log("ss",errorThrown)
                layer.msg("请求超时");
                layer.close(index)
            }
        })
        return false;
    });
})