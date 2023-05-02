layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;


    /**
     * 表单submit监听
     */
    form.on('submit(addOrUpdateCusDevPlan)',function (data){
        //提交数据时的加载层,在ajax请求之前。
        var index = top.layer.msg('数据提交中，请稍候...', {
            icon: 16,
            time: false,
            shade: 0.8
        });

        //得到所有表单元素的值
          var formData = data.field;
        //请求的地址
        var url = ctx + "/cus_dev_plan/add";

        //判断计划项Id是否为空,这个id在隐藏域中
        if( $('[name="id"]').val()){
            url = ctx + "/cus_dev_plan/update";
        }


        $.post(url, formData, function (result) {
            console.log(result);
            //添加操作是否执行成功，200=成功
            if (result.code == 200) {
                setTimeout(function () {
                    //成功 top：能在最上层看到提示信息
                    //提示成功
                    top.layer.msg("操作成功！",{icon:6});
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


    })







    // form.on("submit(addOrUpdateCusDevPlan)", function (data) {
    //     console.log(data);
    //     var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});
    //
    //
    //     //弹出loading
    //     var url=ctx + "/cus_dev_plan/add";
    //     if($("[name='id']").val() != null && $("[name='id']").val() != ""){
    //         url=ctx + "/cus_dev_plan/update";
    //     }
    //     $.post(url, data.field, function (res) {
    //         if (res.code == 200) {
    //             setTimeout(function () {
    //                 top.layer.close(index);
    //                 top.layer.msg("操作成功！");
    //                 layer.closeAll("iframe");
    //                 //刷新父页面
    //                 parent.location.reload();
    //             }, 500);
    //         } else {
    //             layer.msg(
    //                     res.msg, {
    //                         icon: 5
    //                     }
    //                 );
    //         }
    //     });
    //     return false;
    // });
















    /**
     * 关闭弹出层
     */
    $("#closeBtn").click(function (){
        //当你在iframe页面关闭自身时
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭

    })

});