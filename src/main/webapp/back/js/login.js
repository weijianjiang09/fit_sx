$(document).ready(function () {
    let form = layui.form;
    form.on('submit(login)', function(data){
        var index
        $.ajax({
            url:config.url+"/login",
            method:"post",
            data:data.field,
            timeout:3000,
            success:function (res) {
                index =layer.load(2, {time: 2*1000})
                if(res.code==1){
                    layer.msg(res.msg);
                    console.log(res.data)
                    // location.href="";
                    layer.close(index)
                }else {
                    layer.msg(res.msg);
                    layer.close(index)
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log("ss")
                layer.msg("请求超时");
                layer.close(index)
            }
        })
        return false;
    });
})