layui.use(['table','layer'],function(){
       var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
       /**
        * 加载计划项数据表格,加载数据表格必须要进行一次请求，这个id是通过数据表格重载时传递的。则会将查询到的符合数据表格的JSON格式的数据进行组合返回给数据表格
        */
       var tableIns = table.render({
              //容器元素的ID属性值
              elem: '#roleList'
              //设置容器高度 full-差值
              ,height: 'full-125'
              //单元格的最小宽度。
              ,cellMinWidth: 95
              //访问数据的URL(后台数据接口)设置flag参数，表示查询的是客户开发计划数据
              ,url: ctx + '/role/list'//数据接口
              //默认每页显示的数量
              ,limit:10
              //每页页数的可选项
              ,limits:[10,20,30,40,50]
              //开启头部工具栏
              ,toolbar:"#toolbarDemo"
              //数据表单进行判断选中行时所设id
              ,id : "roleTable"
              //表头
              ,cols: [[
                     //要求field属性值与返回的数据中对应的属性字段名一致
                     //title:设置列的标题
                     //sort:是否允许排序（默认：false)
                     //fixed:固定列
                     //设置一个复选框
                     {type:'checkbox',fixed: 'center'}//增加一个复选框
                     ,{field: 'id', title: '编号',sort: true, fixed: 'left'}
                     ,{field: 'roleName', title: '角色名称', align:'center'}
                     ,{field: 'roleRemark', title: '角色备注', align:'center'}
                     ,{field: 'createDate', title: '创建时间', align:'center'}
                     ,{field: 'updateDate', title: '修改时间', align:'center'}
                     ,{title: '操作',templet:'#roleListBar',fixed: 'right',align:'center',minWidth:150}//行工具栏绑定
              ]]
       })



       //搜索按钮，进行绑定
       $(".layui-btn").click(function (){
              //表格重载
              tableIns.reload({
                     //设置给后端的参数
                     where:{
                            //是取的数据表格中的
                            roleName:$('[name="roleName"]').val()//角色姓名
                     },page: {
                                curr:1//重第一页开始
                         }
              })
       })


       /**
        * 监听头部工具栏事件
        * 格式
        *      table.on('toolbar(数据表格的lay-filter属性值)', function(data){
        */
       table.on('toolbar(roles)', function(data){
              //data.event:对应的按钮元素上设置的lay-event属性值，用来区分按钮是什么类型的。
              console.log(data);
              //判断对应的事件类型
              if(data.event == "add"){
                     //添加操作
                     openAddOrUpdateUserDialog();

              }else if(data.event == "grant"){//授权操作
                     //获取数据表格选中的记录数据,通过表格的一个选中行检查的方法对数据表格进行检查
                     var checkStatus = table.checkStatus(data.config.id);//数据表格容器的id，对选中行的记录用数组进行保存
                     //打开权限资源的对话框
                     openAddGrantDialog(checkStatus.data);
              }
       });


       /**
        * 打开授权页面
        * @param data 你选中的所有对象，以数组的形式
        */
       function openAddGrantDialog(data){
              //判断是否选择了角色记录
              if(data.length == 0){
                     layer.msg("请选择要授权的角色!",{icon:5});
                     return;
              }
              //只支持单个角色授权
              if(data.length > 1){
                     layer.msg("暂不支持批量角色授权!",{icon:5});
                     return;
              }
              //将角色id，传入到下一个页面上。
              var url = ctx + "/module/toAddGrantPage?roleId=" + data[0].id;
              var title = "<h3>角色管理-角色授权</h3>";

              layui.layer.open({
                     //类型
                     title:title,
                     //类型
                     type : 2,
                     //宽高
                     area:["400px","500px"],
                     //可以最大化与最小化
                     maxmin:true,
                     //url地址
                     content:url
              });



       }


       /**
        * 监听行工具栏
        * data:里面存放了行相关的数据
        *
        */

       table.on("tool(roles)",function (data){
              if(data.event == "edit"){//更新计划项,也就是编辑
                     //更新计划项,对话框打开,因为打开的是同一个对话框要进行区分，把当前更新行的id拿到
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
                                   url:ctx + "/role/delete",
                                   data:{
                                          id:data.data.id
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
        * 打开添加或添加的对话框
        */
       function openAddOrUpdateUserDialog(id){
              //弹出层的标题
              var title ="<h3>角色管理 - 角色添加</h3>>"
              var url = ctx + "/role/toAddOrUpdateUserPage";//这个地址是要跳转到别一个视图

              //判断用户ID是否为空,如果为空，则为添加操作，为空是null和''都算空。
              if(id != null && id != ''){
                     //更新操作
                     title = "<h3>角色管理 - 角色修改</h3>>"
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





// //角色列表展示
// var  tableIns = table.render({
//     elem: '#roleList',
//     url : ctx+'/role/list',
//     cellMinWidth : 95,
//     page : true,
//     height : "full-125",
//     limits : [10,15,20,25],
//     limit : 10,
//     toolbar: "#toolbarDemo",
//     id : "roleListTable",
//     cols : [[
//         {type: "checkbox", fixed:"left", width:50},
//         {field: "id", title:'编号',fixed:"true", width:80},
//         {field: 'roleName', title: '角色名', minWidth:50, align:"center"},
//         {field: 'roleRemark', title: '角色备注', minWidth:100, align:'center'},
//         {field: 'createDate', title: '创建时间', align:'center',minWidth:150},
//         {field: 'updateDate', title: '更新时间', align:'center',minWidth:150},
//         {title: '操作', minWidth:150, templet:'#roleListBar',fixed:"right",align:"center"}
//     ]]
// });
//
// // 多条件搜索
// $(".search_btn").on("click",function () {
//     table.reload("roleListTable",{
//         page:{
//             curr:1
//         },
//         where:{
//             // 角色名
//             roleName:$("input[name='roleName']").val()
//         }
//     })
// });
//
// // 头工具栏事件
// table.on('toolbar(roles)',function (obj) {
//     switch (obj.event) {
//         case "add":
//             openAddOrUpdateRoleDialog();
//             break;
//     }
// });
//
//
// table.on('tool(roles)',function (obj) {
//     var layEvent =obj.event;
//     if(layEvent === "edit"){
//         openAddOrUpdateRoleDialog(obj.data.id);
//     }else if(layEvent === "del"){
//         layer.confirm("确认删除当前记录?",{icon: 3, title: "角色管理"},function (index) {
//             $.post(ctx+"/role/delete",{id:obj.data.id},function (data) {
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
//
//
// function openAddOrUpdateRoleDialog(id) {
//     var title="角色管理-角色添加";
//     var url=ctx+"/role/addOrUpdateRolePage";
//     if(id){
//         title="角色管理-角色更新";
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
