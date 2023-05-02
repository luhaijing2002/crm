layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    /**
     * 表单submit提交
     * form.on('submit(按钮的lay-filter属性值)', function(data)
     *      return false; //阻止表单跳转。
     *  });
     */
    form.on('submit(login)', function(data){
        // console.log(data.elem) //被执行事件的元素DOM对象，一般为button对象
        // console.log(data.form) //被执行提交的form对象，一般在存在form标签时才会返回
        console.log(data.field) //当前容器的全部表单字段，名值对形式：{name: value}

        /*表单元素的非空校验*/

        /*发送ajax请求，传递用户姓名与密码，捃用户登录操作*/
        $.ajax({
            type:"post",
            url:ctx + "/user/login",
            data:{//发送的名字要与后端保持一致，并且要指定
                userName: data.field.username,
                userPwd:data.field.password
            },
            dataType:"json",
            success:function (result){//result是回调函数，用来接收后端返回的数据
                console.log(result);
                //判断是否登录成功如果code=200,则表示成功，否则表示失败
                if(result.code == 200){
                    //登陆成功
                    /**
                     * 设置用户是登录状态
                     * 1.利用session会话
                     *      保存用户信息，如果会话存在，则用户是登录状态，否则是非登陆状态
                     *      缺点：服务器关闭，会话则会失效。
                     * 2.利用cookie
                     *      保存用户信息 ，cookie未失效，则用户是登录状态，(默认关闭浏览器失效，服务器关闭不会失效)
                     */
                    layer.msg("登录成功",function (){
                        //判断用户是否选择记住密码，(判断复选框是否被选中，如果选中，则设置cookie对象7天生效)
                        if($("#rememberMe").prop("checked")){
                            $.cookie("userIdStr",result.result.userIdStr,{expires:7});
                            $.cookie("userName",result.result.userName,{expires:7});
                            $.cookie("trueName",result.result.trueName,{expires:7});
                        }else {
                            //将用户信息设置到cookie中
                            $.cookie("userIdStr",result.result.userIdStr);
                            $.cookie("userName",result.result.userName);
                            $.cookie("trueName",result.result.trueName);
                        }

                        //登录成功后，跳转到首页
                        window.location.href = ctx + "/main";
                    })

                } else{
                    //登陆失败
                    layer.msg(result.msg,{icon:5})
                }

            }

        });

        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });


});















// // 进行登录操作
// form.on('submit(login)', function (data) {
//     data = data.field;
//     if ( data.username =="undefined" || data.username =="" || data.username.trim()=="") {
//         layer.msg('用户名不能为空');
//         return false;
//     }
//     if ( data.password =="undefined" || data.password =="" || data.password.trim()=="")  {
//         layer.msg('密码不能为空');
//         return false;
//     }
//     $.ajax({
//         type:"post",
//         url:ctx+"/user/login",
//         data:{
//             userName:data.username,
//             userPwd:data.password
//         },
//         dataType:"json",
//         success:function (data) {
//             if(data.code==200){
//                 layer.msg('登录成功', function () {
//                     var result =data.result;
//                     $.cookie("userIdStr",result.userIdStr);
//                     $.cookie("userName",result.userName);
//                     $.cookie("trueName",result.trueName);
//                     // 如果点击记住我 设置cookie 过期时间7天
//                     if($("input[type='checkbox']").is(':checked')){
//                         // 写入cookie 7天
//                         $.cookie("userIdStr",result.userIdStr, { expires: 7 });
//                         $.cookie("userName",result.userName, { expires: 7 });
//                         $.cookie("trueName",result.trueName, { expires: 7 });
//                     }
//                     window.location.href=ctx+"/main";
//                 });
//             }else{
//                 layer.msg(data.msg);
//             }
//         }
//     });
//     return false;
// });