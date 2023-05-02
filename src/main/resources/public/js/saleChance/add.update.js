layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    /**
     * 监听表单submit事件
     *      form.on('submit(按钮元素的lay-filter属性值)',function (data){
     */
    form.on('submit(addOrUpdateSaleChance)',function (data){

        //提交数据时的加载层,在ajax请求之前。
        var index = top.layer.msg('数据提交中，请稍候...', {
            icon: 16,
            time: false,
            shade: 0.8
        });

        //发送ajax请求不需要添加参数，因为参数都已经通过data传了过去。
        var url = ctx + "/sale_chance/add";//添加操作

        //通过营销机会的ID来判断当前需要执行添加操作还是修改操作
        //如果营销机会的id为空，则表示执行添加操作，如果id不为空，则表示执行更新操作
        //但是这个id,要在当前页面有，这个id,是通过查询来得到的，放在隐藏域中
        var saleChanceId = $("[name='id']").val();
        //判断ID是否为空
        if(saleChanceId != null && saleChanceId != '' ){
            //更新操作
            url = ctx + "/sale_chance/update"
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

    /**
     * 加载指派人的下拉框，需要在进行这个页面时就执行，所以不用进行判断，也不用放到方法中，顺序执行就好了。
     */
    $.ajax({
        type:"get",
        url: ctx + "/user/queryAllSales",
        data:{},
        success:function (data){
            console.log(data);
            //判断返回的数据是否为空
            if(data != null){
                //获取隐藏域设置的指派人id
                var assignManId = $("#assignManId").val();
                //遍历返回的数据
                for(var i = 0; i< data.length;i++){
                    //如果循环得到的id与隐藏域的id相等，则表示被选中
                    var opt = "";
                    if(assignManId == data[i].id){
                        //设置下拉选项，设置下拉选项选中 selected
                        opt = "<option value ='"+data[i].id+"' selected>"+data[i].uname+"</option>";
                    }else{
                        //设置下拉选项
                        opt = "<option value ='"+data[i].id+"'>"+data[i].uname+"</option>";
                    }
                    //将下拉项设置到下拉框中
                    $("#assignMan").append(opt);
                }
            }
            //重新渲染下拉框的内容
            layui.form.render("select");
        }

    })

















});




// $.post(ctx+"/user/queryAllCustomerManager",function (res) {
//     for (var i = 0; i < res.length; i++) {
//         if($("input[name='man']").val() == res[i].id ){
//             $("#assignMan").append("<option value=\"" + res[i].id + "\" selected='selected' >" + res[i].name + "</option>");
//         }else {
//             $("#assignMan").append("<option value=\"" + res[i].id + "\">" + res[i].name + "</option>");
//         }
//     }
//     //重新渲染
//     layui.form.render("select");
// });
//
//
// form.on("submit(addOrUpdateSaleChance)", function (data) {
//     var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});
//     //弹出loading
//     var url=ctx + "/sale_chance/save";
//     if($("input[name='id']").val()){
//         url=ctx + "/sale_chance/update";
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
//                 res.msg, {
//                     icon: 5
//                 }
//             );
//         }
//     });
//     return false;
// });