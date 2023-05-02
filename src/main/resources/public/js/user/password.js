layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);
    /*引入相关js的模块,初始化容器*/

    /**
     * 表单的submit监听
     * saveBtn按钮的lay-filter属性值
     */
    form.on('submit(saveBtn)', function(data){
        //所有表单元素的值，layui默认已经判断是否为空。
        console.log(data.field);

        //发送ajax请求
        $.ajax({
            type:"post",
            url:ctx + "/user/updatePwd",
            data:{
                sourcePwd:data.field.old_password,
                newPwd:data.field.new_password,
                confirmPwd:data.field.again_password            },
            success:function (result){
                //判断是否修改成功
                if(result.code == 200){
                    //修改密码成功后，清空cookie数据
                    layer.msg("用户密码修改成功，系统将在3秒钟后退出...",function (){
                        //清空cookie
                        $.removeCookie("userIdStr",{domain:"localhost",path:"/crm"});
                        $.removeCookie("userName",{domain:"localhost",path:"/crm"});
                        $.removeCookie("trueName",{domain:"localhost",path:"/crm"});
                        //跳转到登录页面(父窗口跳转)
                        window.parent.location.href = ctx + "/index";
                    })
                }else{
                    //哭脸
                    layer.msg(result.msg,{icon:5})
                }
            }

        })


    });




});
