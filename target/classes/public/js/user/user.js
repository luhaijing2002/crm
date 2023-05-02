layui.use(['table','layer',"form"],function(){
       var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;



    /**
     * 加载计划项数据表格,加载数据表格必须要进行一次请求，这个id是通过数据表格重载时传递的。则会将查询到的符合数据表格的JSON格式的数据进行组合返回给数据表格
     */
    var tableIns = table.render({
        //容器元素的ID属性值
        elem: '#userList'
        //设置容器高度 full-差值
        ,height: 'full-125'
        //单元格的最小宽度。
        ,cellMinWidth: 95
        //访问数据的URL(后台数据接口)设置flag参数，表示查询的是客户开发计划数据
        ,url: ctx + '/user/list'//数据接口
        //默认每页显示的数量
        ,limit:10
        //每页页数的可选项
        ,limits:[10,20,30,40,50]
        //开启头部工具栏
        ,toolbar:"#toolbarDemo"
        //数据表单进行判断选中行时所设id
        ,id : "userTable"
        //表头
        ,cols: [[
            //要求field属性值与返回的数据中对应的属性字段名一致
            //title:设置列的标题
            //sort:是否允许排序（默认：false)
            //fixed:固定列

            //设置一个复选框
            {type:'checkbox',fixed: 'center'}//增加一个复选框
            ,{field: 'id', title: '编号',sort: true, fixed: 'left'}
            ,{field: 'userName', title: '用户名', align:'center'}
            ,{field: 'email', title: '用户邮箱', align:'center'}
            ,{field: 'trueName', title: '真实姓名', align:'center'}
            ,{field: 'phone', title: '手机号码', align:'center'}
            ,{field: 'createDate', title: '创建时间', align:'center'}
            ,{field: 'updateDate', title: '修改时间', align:'center'}
            ,{title: '操作',templet:'#userListBar',fixed: 'right',align:'center',minWidth:150}//行工具栏绑定
        ]]
    })



    /**
     * 探索按钮的点击事件
     */

    $(".search_btn").click(function (){
        //首先要确定哪些表格被重载，通过数据表格的实例对象来确定
        tableIns.reload({
            //设置需要传递给后端的参数
            where:{
                userName:$('[name="userName"]').val(),//用户姓名
                email:$('[name="email"]').val(),//用户邮箱
                phone:$('[name="phone"]').val()//手机号码
            }
            ,page:{
                curr:1//重新从第1页开始
            }
        })

    })


    //添加用户和删除用户需要监听头部工具栏
    /**
     * 监听头部工具栏事件
     * 格式
     *      table.on('toolbar(数据表格的lay-filter属性值)', function(data){
     */
    table.on('toolbar(users)', function(data){
        //data.event:对应的按钮元素上设置的lay-event属性值，用来区分按钮是什么类型的。
        console.log(data);
        //判断对应的事件类型
        if(data.event == "add"){
            //添加操作
            openAddOrUpdateUserDialog();

        }else if(data.event == "del"){
            //删除操作
            deleteUsers(data);
        }
    });

    /**
     * 监听行工具栏
     * data:里面存放了行相关的数据
     *
     */

    table.on("tool(users)",function (data){
        if(data.event == "edit"){//更新计划项,也就是编辑
            //更新计划项,对话框打开,因为打开的是同一个对话框要进行区分
            openAddOrUpdateUserDialog(data.data.id);

        }else if(data.event == "del"){//删除计划项
            //删除计划项,不是打开对话框，而是一个确认访问框
            // deleteUser(data.data.id);
            layer.confirm('确认要删除该记录吗?',{icon:3,title:"用户管理"},function (index){
                //关闭确认框
                layer.close(index);
                //发送ajax请求，删除记录
                $.ajax({
                    type:"post",
                    url:ctx + "/user/delete",
                    data:{
                        ids:data.data.id
                    },
                    success:function (result){
                        //判断删除结果
                        if(result.code == 200){
                            //提示成功
                            layer.msg("删除成功!",{icon:6});
                            //刷新表格，进行重载
                            tableIns.reload();

                        }else{
                            //提示失败
                            layer.msg(result.msg,{icon:5})
                        }
                    }
                })
            })
        }
    })





    /**
     * 删除营销机会(删除多条记录)
     * @param data
     */
    function deleteUsers(data){
        //获取数据表格选中的行数据 table.checkStatus('数据表格的id属性值'); 数据表格的id还可以通过data.config.id来获取表格中选中状态的行数据。
        var checkStatus = table.checkStatus('userTable');
        console.log(checkStatus);

        //获取所有被选中的记录对应的数据，以数组的形式取到
        var saleChanceDate = checkStatus.data;
        //判断数组为的长度，判断用户是否选择的记录(选中行的数量大于0)
        if(saleChanceDate.length < 1){
            layer.msg("请选择要删除的记录！",{icon:5});
            return;
        }
        //访问用户是否确认删除
        layer.confirm('您确定要删除选中的记录吗？',{icon:3,title:'用户管理'},function (index){
            //关闭确认框
            layer.close(index);
            //传递的参数是数组 ids=1&ids=2&ids=3
            var ids = "ids=";
            //循环选中的行记录的数据
            for(var i = 0;i < saleChanceDate.length;i++){
                //如果当前循环的次数<数组长度-1[下标最大值]
                if(i < saleChanceDate.length - 1){
                    ids = ids + saleChanceDate[i].id + "&ids=";
                }else {
                    ids = ids + saleChanceDate[i].id ;
                }
            }
            console.log(ids);

            // 发送ajax请求，执行删除营销机会
            $.ajax({
                type:"post",
                url:ctx+"/user/delete",
                data:ids,//传递的参数是数组 ids=1&ids=2&ids=3
                success:function (result){
                    //判断删除结果
                    if(result.code == 200){
                        //提示成功
                        layer.msg("删除成功!",{icon:6});
                        //刷新表格，进行重载
                        tableIns.reload();
                    }else{
                        //提示失败
                        layer.msg(result.msg,{icon:5})
                    }
                }
            })
        })
    }




    /**
     * 打开添加/修改用户的对话框
     */
    /**
     * 打开添加/修改 营销机会数据的窗口（专门用于弹出层）,也只可以进行发送请求，通过拿到的这一行的数据data
     * 通过主键来区分添加还是修改操作,提供一个视图的容器，让返回的视图页面有地方可以显示，而这个视图是模板.ftl+数据 == 模板引擎生成的视图
     */
    function openAddOrUpdateUserDialog(id){
        //弹出层的标题
        var title ="<h3>用户管理 - 用户添加</h3>>"
        var url = ctx + "/user/toAddOrUpdateUserPage";//这个地址是要跳转到别一个视图

        //判断用户ID是否为空,如果为空，则为添加操作，为空是null和''都算空。
        if(id != null && id != ''){
            //更新操作
            title = "<h3>用户管理 - 用户修改</h3>>"
            //请求地址传递用户的id
            url += '?id=' + id;
        }
        layui.layer.open({
            //类型
            title : title,
            //类型
            type : 2,
            //宽高
            area:["650px","400px"],
            //可以最大化与最小化
            maxmin:true,
            //url地址
            content: url,
        });

    }


});















// //用户列表展示
// var  tableIns = table.render({
//     elem: '#userList',
//     url : ctx+'/user/list',
//     cellMinWidth : 95,
//     page : true,
//     height : "full-125",
//     limits : [10,15,20,25],
//     limit : 10,
//     toolbar: "#toolbarDemo",
//     id : "userListTable",
//     cols : [[
//         {type: "checkbox", fixed:"left", width:50},
//         {field: "id", title:'编号',fixed:"true", width:80},
//         {field: 'userName', title: '用户名', minWidth:50, align:"center"},
//         {field: 'email', title: '用户邮箱', minWidth:100, align:'center'},
//         {field: 'phone', title: '手机号', minWidth:100, align:'center'},
//         {field: 'trueName', title: '真实姓名', align:'center'},
//         {field: 'createDate', title: '创建时间', align:'center',minWidth:150},
//         {field: 'updateDate', title: '更新时间', align:'center',minWidth:150},
//         {title: '操作', minWidth:150, templet:'#userListBar',fixed:"right",align:"center"}
//     ]]
// });
//
//
// // 多条件搜索
// $(".search_btn").on("click",function () {
//     table.reload("userListTable",{
//         page:{
//             curr:1
//         },
//         where:{
//             userName:$("input[name='userName']").val(),// 用户名
//             email:$("input[name='email']").val(),// 邮箱
//             phone:$("input[name='phone']").val()    //手机号
//         }
//     })
// });
//
//
// // 头工具栏事件
// table.on('toolbar(users)',function (obj) {
//     switch (obj.event) {
//         case "add":
//             openAddOrUpdateUserDialog();
//             break;
//         case "del":
//             delUser(table.checkStatus(obj.config.id).data);
//             break;
//     }
// });
//
//
//
// function delUser(datas){
//     /**
//      * 批量删除
//      *   datas:选择的待删除记录数组
//      */
//     if(datas.length==0){
//         layer.msg("请选择待删除记录!");
//         return;
//     }
//     layer.confirm("确定删除选中的记录",{
//         btn:['确定','取消']
//     },function (index) {
//         layer.close(index);
//         // ids=10&ids=20&ids=30
//         var ids="ids=";
//         for(var i=0;i<datas.length;i++){
//             if(i<datas.length-1){
//                 ids=ids+datas[i].id+"&ids=";
//             }else{
//                 ids=ids+datas[i].id;
//             }
//         }
//         $.ajax({
//             type:"post",
//             url:ctx+"/user/delete",
//             data:ids,
//             dataType:"json",
//             success:function (data) {
//                 if(data.code==200){
//                     tableIns.reload();
//                 }else{
//                     layer.msg(data.msg);
//                 }
//             }
//         })
//
//
//
//     })
// }
//
// table.on('tool(users)',function (obj) {
//     var layEvent =obj.event;
//     if(layEvent === "edit"){
//         openAddOrUpdateUserDialog(obj.data.id);
//     }else if(layEvent === "del"){
//         layer.confirm("确认删除当前记录?",{icon: 3, title: "用户管理"},function (index) {
//             $.post(ctx+"/user/delete",{ids:obj.data.id},function (data) {
//                 if(data.code==200){
//                     layer.msg("删除成功");
//                     tableIns.reload();
//                 }else{
//                     layer.msg(data.msg);
//                 }
//             })
//         })
//     }
// });
//
//
// function openAddOrUpdateUserDialog(id) {
//     var title="用户管理-用户添加";
//     var url=ctx+"/user/addOrUpdateUserPage";
//     if(id){
//         title="用户管理-用户更新";
//         url=url+"?id="+id;
//     }
//     layui.layer.open({
//         title:title,
//         type:2,
//         area:["700px","500px"],
//         maxmin:true,
//         content:url
//     })
// }



