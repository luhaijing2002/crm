layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;


    /**
     * 加载计划项数据表格,加载数据表格必须要进行一次请求，这个id是通过数据表格重载时传递的。则会将查询到的符合数据表格的JSON格式的数据进行组合返回给数据表格
     */
    var tableIns = table.render({
        //容器元素的ID属性值
        elem: '#cusDevPlanList'
        //设置容器高度 full-差值
        ,height: 'full-125'
        //单元格的最小宽度。
        ,cellMinWidth: 95
        //访问数据的URL(后台数据接口)设置flag参数，表示查询的是客户开发计划数据
        ,url: ctx + '/cus_dev_plan/list?saleChanceId='+ $("[name='id']").val() //数据接口 通过隐藏域的来得到营销机会id
        ,page: true //开启分页
        //默认每页显示的数量
        ,limit:10
        //每页页数的可选项
        ,limits:[10,20,30,40,50]
        //开启头部工具栏
        ,toolbar:"#toolbarDemo"
        //数据表单进行判断选中行时所设id
        ,id : "cusDevPlanTable"
        //表头
        ,cols: [[
            //要求field属性值与返回的数据中对应的属性字段名一致
            //title:设置列的标题
            //sort:是否允许排序（默认：false)
            //fixed:固定列

            //设置一个复选框
            {type:'checkbox',fixed: 'center'}
            ,{field: 'id', title: '编号',sort: true, fixed: 'left'}
            ,{field: 'planItem', title: '计划项', align:'center'}
            ,{field: 'planDate', title: '计划时间', align:'center'}
            ,{field: 'exeAffect', title: '执行效果', align:'center'}
            ,{field: 'createDate', title: '创建时间', align:'center'}
            ,{field: 'updateDate', title: '修改时间', align:'center'}
            ,{title: '操作',templet:'#cusDevPlanListBar',fixed: 'right',align:'center',minWidth:150}
        ]]
    })


    /**
     * 监听头部工具栏事件
     * 格式
     *
     * 格式化开发状态值
     * 0=表示未开发
     * 1=开发中
     * 2=开发成功
     * 3=开发失败
     *      table.on('toolbar(数据表格的lay-filter属性值)', function(data){
     */
    table.on('toolbar(cusDevPlans)', function(data){
        //data.event:对应的按钮元素上设置的lay-event属性值，用来区分按钮是什么类型的。
        // console.log(data);
        //判断对应的事件类型
        if(data.event == "add"){//添加计划项

            openCusDevPlansDialog();

        }else if(data.event == "success"){//开发成功
            //更新营销机会的开发状态
            updateSaleChanceDevResult(2);
        }else if(data.event == "failed"){//开发失败
            //更新营销机会的开发状态
            updateSaleChanceDevResult(3);
        }
    });


    /**
     * 更新营销机会的开发状态
     * @param devResult
     */
    function updateSaleChanceDevResult(devResult){
        layer.confirm('您确认要执行该操作吗?', {icon: 3, title:'营销机会管理'}, function(index){
        //得到需要被更新的营销机会id(通过隐藏域获取)
            var sId = $("[name='id']").val();
            //定义要请求的url地址
            var url = ctx +'/sale_chance/updateSaleChanceDevResult'
            //发送ajax请求，更新营销机会的开发状态
            $.post(url,{id:sId,devResult:devResult},function (result){
                if(result.code == 200){
                    layer.msg("更新成功",{icon:6});
                    //关闭所有窗口
                    layer.closeAll();
                    //刷新父页面
                    parent.location.reload();
                }else{
                    layer.msg(result.msg,{icon:5});
                }

            })
        })
    }


    /**
     * 监听行工具栏
     * data:里面存放了行相关的数据
     *
     */

    table.on("tool(cusDevPlans)",function (data){
        if(data.event == "edit"){//更新计划项
            //更新计划项,对话框打开,因为打开的是同一个对话框要进行区分
            openCusDevPlansDialog(data.data.id);

        }else if(data.event == "del"){//删除计划项
            //删除计划项
            deleteCusDevPlans(data.data.id);
        }
    })


    /**
     *删除计划项
     * @param id
     */
    function deleteCusDevPlans(id){
        //弹出确认框，访问用户是否确认删除
        layer.confirm('您确认要删除该记录吗?', {icon: 3, title:'开发'}, function(index){
            //发送ajax请求，执行删除操作
            $.post(ctx + '/cus_dev_plan/delete',{id:id},function (result){
                //判断删除结果
                if(result.code == 200){
                    //提示成功
                    layer.msg('删除成功',{icon:6});
                    //刷新数据表格(表格)
                    tableIns.reload();

                }else{
                    //提示失败原因
                    layer.msg(result.msg(),{icon:5});
                }
                // //关闭访问框
                // layer.close(index);
            })


        });

    }





    /**
     * 打开项管理的添加|更新计划项对话框
     */
    function openCusDevPlansDialog(id){
        //弹出层的标题
        var title ="<h3>计划项管理-添加计划项</h3>>";
        var url = ctx + "/cus_dev_plan/toAddOrUpdateCusDevPlanPage?sId=" + $("[name='id']").val();//这里要返回一个页面

        //判断计划项的id是否为空，(如果为空，则表示添加；不为空则表示更新操作)
        if(id != null){
            title ="<h3>计划项管理-更新计划项</h3>>";
            //如果是更新操作，需要传递一个营销机会的id过去。
            url += "&id="+ id;//都是返回一个页面，不过这个视图会通过这个id查出记录，并进行数据填充
        }

        //iframe层(内嵌)
        layui.layer.open({
            //类型
            title : title,
            //类型
            type : 2,
            //宽高
            area:["500px","300px"],
            //可以最大化与最小化
            maxmin:true,
            //url地址
            content: url,
        });

    }







});












// //计划项数据展示
// var  tableIns = table.render({
//     elem: '#cusDevPlanList',
//     url : ctx+'/cus_dev_plan/list?sid='+$("input[name='id']").val(),
//     cellMinWidth : 95,
//     page : true,
//     height : "full-125",
//     limits : [10,15,20,25],
//     limit : 10,
//     toolbar: "#toolbarDemo",
//     id : "cusDevPlanListTable",
//     cols : [[
//         {type: "checkbox", fixed:"center"},
//         {field: "id", title:'编号',fixed:"true"},
//         {field: 'planItem', title: '计划项',align:"center"},
//         {field: 'exeAffect', title: '执行效果',align:"center"},
//         {field: 'planDate', title: '执行时间',align:"center"},
//         {field: 'createDate', title: '创建时间',align:"center"},
//         {field: 'updateDate', title: '更新时间',align:"center"},
//         {title: '操作',fixed:"right",align:"center", minWidth:150,templet:"#cusDevPlanListBar"}
//     ]]
// });
//
//
// //头工具栏事件
// table.on('toolbar(cusDevPlans)', function(obj){
//     switch(obj.event){
//         case "add":
//             openAddOrUpdateCusDevPlanDialog();
//             break;
//         case "success":
//             updateSaleChanceDevResult($("input[name='id']").val(),2);
//             break;
//         case "failed":
//             updateSaleChanceDevResult($("input[name='id']").val(),3);
//             break;
//     };
// });
//
//
//
// /**
//  * 行监听
//  */
// table.on("tool(cusDevPlans)", function(obj){
//     var layEvent = obj.event;
//     if(layEvent === "edit") {
//         openAddOrUpdateCusDevPlanDialog(obj.data.id);
//     }else if(layEvent === "del") {
//         layer.confirm('确定删除当前数据？', {icon: 3, title: "开发计划管理"}, function (index) {
//             $.post(ctx+"/cus_dev_plan/delete",{id:obj.data.id},function (data) {
//                 if(data.code==200){
//                     layer.msg("操作成功！");
//                     tableIns.reload();
//                 }else{
//                     layer.msg(data.msg, {icon: 5});
//                 }
//             });
//         })
//     }
//
// });
//
//
// // 打开添加计划项数据页面
// function openAddOrUpdateCusDevPlanDialog(id){
//     var url  =  ctx+"/cus_dev_plan/addOrUpdateCusDevPlanPage?sid="+$("input[name='id']").val();
//     var title="计划项管理-添加计划项";
//     if(id){
//         url = url+"&id="+id;
//         title="计划项管理-更新计划项";
//     }
//     layui.layer.open({
//         title : title,
//         type : 2,
//         area:["700px","400px"],
//         maxmin:true,
//         content : url
//     });
// }
//
//
//
//
//
// function updateSaleChanceDevResult(sid,devResult) {
//     layer.confirm('确定执行当前操作？', {icon: 3, title: "计划项维护"}, function (index) {
//         $.post(ctx+"/sale_chance/updateSaleChanceDevResult",
//             {
//                 id:sid,
//                 devResult:devResult
//             },function (data) {
//                 if(data.code==200){
//                     layer.msg("操作成功！");
//                     layer.closeAll("iframe");
//                     //刷新父页面
//                     parent.location.reload();
//                 }else{
//                     layer.msg(data.msg, {icon: 5});
//                 }
//             });
//     })
// }

