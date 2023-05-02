layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;


    /**
     * 监听表单submit事件，确认按钮监听
     *      form.on('submit(按钮元素的lay-filter属性值)',function (data){
     */
    form.on('submit(addOrUpdateRole)',function (data){

        //提交数据时的加载层,在ajax请求之前。
        var index = top.layer.msg('数据提交中，请稍候...', {
            icon: 16,
            time: false,
            shade: 0.8
        });

        //发送ajax请求不需要添加参数，因为参数都已经通过data传了过去。
        var url = ctx + "/role/add";//添加操作

        //但是这个角色id,要在当前页面有，这个id,是通过查询来得到的，放在隐藏域中
        var id = $("[name='id']").val();
        //判断ID是否为空,默认有值。
        if(id != null && id != '' ){
            //更新操作
            url = ctx + "/role/update"
        }
        $.post(url, data.field, function (result) {
            console.log(result);
            //添加操作是否执行成功，200=成功
            if (result.code == 200) {
                setTimeout(function () {
                    //成功
                    //提示成功
                    layer.msg("操作成功！",{icon:6});
                    //关闭加载层
                    top.layer.close(index);
                    //关闭弹出层
                    layer.closeAll("iframe");
                    //刷新父页面，重新加载数据
                    parent.location.reload();
                }, 500);
            } else {
                //失败
                layer.msg(result.msg, {icon: 5});
            }
        });
        //阻止表单提交
        return false;
    });




    /**
     * 关闭弹出层
     */
    $("#closeBtn").click(function (){
        //当你在iframe页面关闭自身时
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭

    })









});


// form.on('submit(addOrUpdateRole)',function (data) {
//     var index= top.layer.msg("数据提交中,请稍后...",{icon:16,time:false,shade:0.8});
//     var url = ctx+"/role/save";
//     if($("input[name='id']").val()){
//         url=ctx+"/role/update";
//     }
//     $.post(url,data.field,function (res) {
//         if(res.code==200){
//             top.layer.msg("操作成功");
//             top.layer.close(index);
//             layer.closeAll("iframe");
//             // 刷新父页面
//             parent.location.reload();
//         }else{
//             layer.msg(res.msg);
//         }
//     });
//     return false;
// });