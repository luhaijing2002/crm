layui.use(['form', 'layer','formSelects'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    var  formSelects = layui.formSelects;





    /**
     * 关闭弹出层
     */
    $("#closeBtn").click(function (){
        //当你在iframe页面关闭自身时
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭

    })



    /**
     * 监听表单submit事件，确认按钮监听
     *      form.on('submit(按钮元素的lay-filter属性值)',function (data){
     */
    form.on('submit(addOrUpdateUser)',function (data){

        //提交数据时的加载层,在ajax请求之前。
        var index = top.layer.msg('数据提交中，请稍候...', {
            icon: 16,
            time: false,
            shade: 0.8
        });

        //发送ajax请求不需要添加参数，因为参数都已经通过data传了过去。
        var url = ctx + "/user/add";//添加操作

        //通过营销机会的ID来判断当前需要执行添加操作还是修改操作
        //如果营销机会的id为空，则表示执行添加操作，如果id不为空，则表示执行更新操作
        //但是这个id,要在当前页面有，这个id,是通过查询来得到的，放在隐藏域中
        var id = $("[name='id']").val();
        //判断ID是否为空,默认有值。
        if(id != null && id != '' ){
            //更新操作
            url = ctx + "/user/update"
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
     * 1.监听select的选中与取消
     *
     * formSelects.on(ID, Function);
     *
     * @param ID        xm-select的值
     * @param Function  自定义处理方法
     * @param isEnd     是否获取实时数据, true/false
     */
    formSelects.on('select1', function(id, vals, val, isAdd, isDisabled){
        //id:           点击select的id
        //vals:         当前select已选中的值
        //val:          当前select点击的值
        //isAdd:        当前操作选中or取消
        //isDisabled:   当前选项是否是disabled




    }, true);



    /**
     * 加载角色下拉框 formSelects
     *
     * 1.配置远程搜索, 请求头, 请求参数, 请求类型等
     *
     * formSelects.config(ID, Options, isJson);
     *
     * @param ID        xm-select的值
     * @param Options   配置项
     * @param isJson    是否传输json数据, true将添加请求头 Content-Type: application/json; charset=UTF-8
     */
    var userId = $("[name='id']").val();
    formSelects.config("selectId",{
        type:"post",//请求方式
        searchUrl: ctx + "/role/queryAllRoles?userId=" + userId,//请求地址
        keyName:'roleName',//下拉框中的文本内容，要与返回的数据中对应key一致
        keyVal: 'id'
    },true);

});
















// var userId=$("input[name='id']").val();
// formSelects.config('selectId',{
//     type:"post",
//     searchUrl:ctx+"/role/queryAllRoles?userId="+userId,
//     //自定义返回数据中name的key, 默认 name
//     keyName: 'roleName',
//     //自定义返回数据中value的key, 默认 value
//     keyVal: 'id'
// },true);
//
//
// form.on('submit(addOrUpdateUser)',function (data) {
//     var index= top.layer.msg("数据提交中,请稍后...",{icon:16,time:false,shade:0.8});
//     var url = ctx+"/user/save";
//     if($("input[name='id']").val()){
//         url=ctx+"/user/update";
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