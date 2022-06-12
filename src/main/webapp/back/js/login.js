$(document).ready(function () {
    let form = layui.form;
    form.on('submit(login)', function(data){
        $.ajax({
            method:"post",
            data:data,
            success:function (res) {
                if(res.code==1){
                    layer.msg(res.msg);
                    location.href="";
                }else {
                    layer.msg(res.msg);
                }
            }
        })
        return false;
    });
})