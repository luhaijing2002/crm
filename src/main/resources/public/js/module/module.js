layui.use(['table', 'treetable'], function () {
    var $ = layui.jquery;
    var table = layui.table;
    var treeTable = layui.treetable;

    // 渲染表格
    treeTable.render({
        treeColIndex: 1,//资源id
        treeSpid: -1,//父类资源id
        treeIdName: 'id',//资源名称字段
        treePidName: 'parentId',//父资源名称字段
        elem: '#munu-table',//与table元素绑定的id
        url: ctx+'/module/list',//后台地址
        toolbar: "#toolbarDemo",//头部工具栏
        treeDefaultClose:true,//树默认关闭
        page: true,//分页开启
        cols: [[
            {type: 'numbers'},//序列号
            {field: 'moduleName', minWidth: 100, title: '菜单名称'},
            {field: 'optValue',minWidth: 80, title: '权限码'},
            {field: 'url', title: '菜单url'},
            {field: 'createDate', title: '创建时间'},
            {field: 'updateDate', title: '更新时间'},
            {
                field: 'grade', width: 80, align: 'center', templet: function (d) {
                    if (d.grade == 0) {
                        return '<span class="layui-badge layui-bg-blue">目录</span>';
                    }
                    if(d.grade== 1){
                        return '<span class="layui-badge-rim">菜单</span>';
                    }
                    if (d.grade == 2) {
                        return '<span class="layui-badge layui-bg-gray">按钮</span>';
                    }
                }, title: '类型'
            },
            {templet: '#auth-state', width: 200, align: 'center', title: '操作'}
        ]],
        //完成之后，进行加载的。
        done: function () {
            layer.closeAll('loading');
        }
    });


    /**
     * 监听头部工具栏
     * toolbar(table标签lay-filter属性值)
     * treeTable.expandAll("#表格元素id属性");
     * foldAll：全部折叠方法
     * expandAll：全部展开方法
     */
    table.on('toolbar(munu-table)',function (data){
        //判断lay-event属性
        if(data.event == "expand"){//全部展开
            treeTable.expandAll("#munu-table");
        }else if(data.event == "fold"){//全部折叠
            treeTable.foldAll("#munu-table");
        }else if(data.event == "add"){//添加目录
            //层级=0,父菜单=-1
            openAddModuleDialog(0,-1)


        }

    })


    /**
     * 监听行工具栏
     */
    table.on('tool(munu-table)',function (data){
        //判断lay-event属性
        if(data.event == "add"){
            //添加子项
            //判断当前层级(如果是三级菜单，就不能添加)
            if(data.data.grade == 2){
                layer.msg("暂不支持添加四级菜单!",{icon:5})
                return;
            }
            //一级|二级菜单， grade = 当前层级 + 1，parentId=当前资源的Id
            openAddModuleDialog(data.data.grade+1,data.data.id)
        }else if(data.event == "edit"){ //修改资源
            openUpdateModuleDialog(data.data.id);

        }else if(data.event == "del"){
            deleteModule(data.data.id);
        }
    })


    /**
     * 删除资源
     */
    function  deleteModule(id){
        //弹出确认框访问用户是否确认删除
        //访问用户是否确认删除
        layer.confirm('您确定要删除该记录吗？',{icon:3,title:'资源管理'},function (index){

            // 发送ajax请求，执行删除资源
            $.ajax({
                type:"post",
                url:ctx+"/module/delete",
                data:{
                    id:id
                },
                success:function (result){

                    //判断删除结果
                    if(result.code == 200){
                        //关闭加载层的
                        setTimeout(function () {
                            top.layer.close(index);
                            //提示成功
                            top.layer.msg("删除成功！",{icon:6});
                            layer.closeAll("iframe");
                            // //刷新父页面
                            // parent.location.reload();
                            //刷新当前页面
                            location.reload();
                        }, 500);
                    }else{
                        //提示失败
                        layer.msg(result.msg,{icon:5})
                    }
                }
            })
        })

    }




    /**
     * 打开添加资源的对话框
     * @param grade 层级
     * @param parentId 父菜单ID
     */
    function openAddModuleDialog(grade,parentId){
        var title = "<h3>资源管理 - 添加资源</h3>";
        console.log(grade);
        console.log(parentId);
        var url = ctx + "/module/toAddModulePage?grade="+ grade + "&parentId=" + parentId;

        layui.layer.open({
            type:2,
            title:title,
            content:url,
            area:["700px","450px"],
            maxmin:true
        })
    }


    /**
     * 打开修改资源的对话框
     * @param id
     */
    function openUpdateModuleDialog(id){
        var title = "<h3>资源管理 - 修改资源</h3>";
        var url = ctx + "/module/toUpdateModulePage?id=" + id;

        layui.layer.open({
            type:2,
            title:title,
            content:url,
            area:["700px","450px"],
            maxmin:true
        })
    }



















});


// // 渲染表格
// treeTable.render({
//     treeColIndex: 1,//资源id
//     treeSpid: -1,//父类资源id
//     treeIdName: 'id',//资源名称字段
//     treePidName: 'parentId',//父资源名称字段
//     elem: '#munu-table',//与table元素绑定的id
//     url: ctx+'/module/list',//后台地址
//     toolbar: "#toolbarDemo",//头部工具栏
//     treeDefaultClose:true,//树默认关闭
//     page: true,//分页开启
//     cols: [[
//         {type: 'numbers'},//序列号
//         {field: 'moduleName', minWidth: 100, title: '菜单名称'},
//         {field: 'optValue', title: '权限码'},
//         {field: 'url', title: '菜单url'},
//         {field: 'createDate', title: '创建时间'},
//         {field: 'updateDate', title: '更新时间'},
//         {
//             field: 'grade', width: 80, align: 'center', templet: function (d) {
//                 if (d.grade == 0) {
//                     return '<span class="layui-badge layui-bg-blue">目录</span>';
//                 }
//                 if(d.grade==1){
//                     return '<span class="layui-badge-rim">菜单</span>';
//                 }
//                 if (d.grade == 2) {
//                     return '<span class="layui-badge layui-bg-gray">按钮</span>';
//                 }
//             }, title: '类型'
//         },
//         {templet: '#auth-state', width: 180, align: 'center', title: '操作'}
//     ]],
//     //完成之后，进行加载的。
//     done: function () {
//         layer.closeAll('loading');
//     }
// });
//
//
// //监听工具条
// table.on('tool(munu-table)', function (obj) {
//     var data = obj.data;
//     var layEvent = obj.event;
//     if (layEvent === 'add') {
//         if(data.grade==2){
//             layer.msg("暂不支持四级菜单添加操作!");
//             return;
//         }
//         openAddModuleDialog(data.grade+1,data.id);
//     } else if (layEvent === 'edit') {
//         // 记录修改
//         openUpdateModuleDialog(data.id);
//     } else if (layEvent === 'del') {
//         layer.confirm('确定删除当前菜单？', {icon: 3, title: "菜单管理"}, function (index) {
//             $.post(ctx+"/module/delete",{id:data.id},function (data) {
//                 if(data.code==200){
//                     layer.msg("操作成功！");
//                     window.location.reload();
//                 }else{
//                     layer.msg(data.msg, {icon: 5});
//                 }
//             });
//         })
//     }
// });
//
// table.on('toolbar(munu-table)', function(obj){
//     switch(obj.event){
//         case "expand":
//             treeTable.expandAll('#munu-table');
//             break;
//         case "fold":
//             treeTable.foldAll('#munu-table');
//             break;
//         case "add":
//             openAddModuleDialog(0,-1);
//             break;
//     };
// });
//
//
// // 打开添加菜单对话框
// function openAddModuleDialog(grade,parentId){
//     var grade=grade;
//     var url  =  ctx+"/module/addModulePage?grade="+grade+"&parentId="+parentId;
//     var title="菜单添加";
//     layui.layer.open({
//         title : title,
//         type : 2,
//         area:["700px","450px"],
//         maxmin:true,
//         content : url
//     });
// }
//
// function openUpdateModuleDialog(id){
//     var url  =  ctx+"/module/updateModulePage?id="+id;
//     var title="菜单更新";
//     layui.layer.open({
//         title : title,
//         type : 2,
//         area:["700px","450px"],
//         maxmin:true,
//         content : url
//     });
// }