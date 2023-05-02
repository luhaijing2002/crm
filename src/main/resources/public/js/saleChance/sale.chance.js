layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;


    /**
     * 加载数据表格,也会进行一次请求
     */
    var tableIns = table.render({
        //容器元素的ID属性值
        elem: '#saleChanceList'
        //设置容器高度 full-差值
        ,height: 'full-125'
        //单元格的最小宽度。
        ,cellMinWidth: 95
        //访问数据的URL(后台数据接口)
        ,url: ctx + '/sale_chance/list' //数据接口
        ,page: true //开启分页
        //默认每页显示的数量
        ,limit:10
        //每页页数的可选项
        ,limits:[10,20,30,40,50]
        //开启头部工具栏
        ,toolbar:"#toolbarDemo"
        //数据表单进行判断选中行时所设id
        ,id : "saleChanceListTable"
        //表头
        ,cols: [[
            //要求field属性值与返回的数据中对应的属性字段名一致
            //title:设置列的标题
            //sort:是否允许排序（默认：false)
            //fixed:固定列

            //设置一个复选框
            {type:'checkbox',fixed: 'center'}
            ,{field: 'id', title: '编号',sort: true, fixed: 'left'}
            ,{field: 'chanceSource', title: '机会来源', align:'center'}
            ,{field: 'customerName', title: '客户名称', align:'center'}
            ,{field: 'cgjl', title: '成功几率', align:'center'}
            ,{field: 'overview', title: '概要', align:'center'}
            ,{field: 'linkMan', title: '联系人', align:'center'}
            ,{field: 'linkPhone', title: '联系号码', align:'center'}
            ,{field: 'description', title: '描述', align:'center'}
            ,{field: 'createMan', title: '创建人', align:'center'}
            ,{field: 'uname', title: '分配人', align:'center'}
            /*,{field: 'uname', title: '指派人', align:'center'}*/
            ,{field: 'assignTime', title: '分配时间', align:'center'}
            ,{field: 'createDate', title: '创建时间', align:'center'}
            ,{field: 'updateDate', title: '修改时间', align:'center'}
            ,{field: 'state', title: '分配状态', align:'center',templet: function (d){ //这个d就是一条记录上的所以字段
                //调用函数，返回格式化的结果
                return formatState(d.state);
            }}
            ,{field: 'devResult', title: '开发状态', align:'center',templet: function (d){
                //调用函数，返回格式化的结果
                return formatDevState(d.devResult);
            }}
            ,{title: '操作',templet:'#saleChanceListBar',fixed: 'right',align:'center',minWidth:150}
        ]]
    });


    /**
     * 格式化分配状态值
     * 0=未分配
     * 1=已分配
     * 其他=未知
     * @param state
     */
    function formatState(state){
        if(state == 0){
            return"<div style='color: blueviolet'>未分配</div>"
        }else if(state == 1){
            return"<div style='color: green'>已分配</div>"
        }else {
            return"<div style='color: red'>已分配</div>"
        }
    }

    /**
     * 格式化开发状态值
     * 0=表示未开发
     * 1=开发中
     * 2=开发成功
     * 3=开发失败
     * @param state
     */
    function formatDevState(devResult){
        if(devResult == 0){
            return"<div style='color:blueviolet'>未开发</div>"
        }else if(devResult == 1){
            return"<div style='color: orange'>开发中</div>"
        }else if(devResult == 2){
            return"<div style='color: green'>开发成功</div>"
        } else if(devResult == 3){
            return"<div style='color: red'>开发失败</div>"
        } else {
            return"<div style='color: blue'>未知</div>"
        }
    }


    /**
     * 探索按钮的点击事件
     */
    $(".search_btn").click(function (){
        /**
         * 表格重载
         */
        //这里以搜索为例tableIns:当前表格容器的实例,进行表格重载，
        tableIns.reload({
            //设置需要传递给后端的参数
            where: { //设定异步数据接口的额外参数，任意设
                customerName: $("[name='customerName']").val()//客户名称
                ,createMan: $("[name='createMan']").val()//创建人
                ,state:$("#state").val()//状态
            }
            ,page: {
                curr: 1 //重新从第 1 页开始
            }
        });
    });


    //触发事件
    /**
     * 监听头部工具栏事件
     * 格式
     *      table.on('toolbar(数据表格的lay-filter属性值)', function(data){
     */
    table.on('toolbar(saleChances)', function(data){
        //data.event:对应的按钮元素上设置的lay-event属性值，用来区分按钮是什么类型的。
        console.log(data);
        //判断对应的事件类型
        if(data.event == "add"){
            //添加操作
            openSaleChanceDialog();

        }else if(data.event == "del"){
            //删除操作
            deleteSaleChance(data);
        }


    });


    /**
     * 删除营销机会(删除多条记录)
     *
     * 获取数据表格中被选中的行的记录。
     * @param data
     */
    function deleteSaleChance(data){
        //获取数据表格选中的行数据 table.checkStatus('数据表格的id属性值');表格中选中状态的行数据。
        var checkStatus = table.checkStatus('saleChanceListTable');
        console.log(checkStatus);

        //获取所有被选中的记录对应的数据
        var saleChanceDate = checkStatus.data;
        //判断数组为的长度，判断用户是否选择的记录(选中行的数量大于0)
        if(saleChanceDate.length < 1){
            layer.msg("请选择要删除的记录！",{icon:5});
            return;
        }
        //访问用户是否确认删除
        layer.confirm('您确定要删除选中的记录吗？',{icon:3,title:'营销机会管理'},function (index){
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
                url:ctx+"/sale_chance/delete",
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
     * 打开添加/修改 营销机会数据的窗口（专门用于弹出层）,也只可以进行发送请求，通过拿到的这一行的数据data
     * 通过主键来区分添加还是修改操作,提供一个视图的容器，让返回的视图页面有地方可以显示，而这个视图是模板.ftl+数据 == 模板引擎生成的视图
     */
    function openSaleChanceDialog(saleChanceId){
        //弹出层的标题
        var title ="<h3>营销机会管理 - 添加营销机会</h3>"
        var url = ctx + "/sale_chance/toSaleChancePage"

        //判断营销机会ID是否为空
        if(saleChanceId != null){
            //更新操作
            title = "<h3>营销机会管理 - 修改营销机会</h3>"
            //请求地址传递营销机会的id
            url += '?saleChanceId=' + saleChanceId;
        }

            layui.layer.open({
                //类型
                title : title,
                //类型
                type : 2,
                //宽高
                area:["500px","620px"],
                //可以最大化与最小化
                maxmin:true,
                //url地址
                content: url,
            });

    }

    /**
     * 行工具栏监听事件
     * table.on('数据表格的lay-filter属性值',function (data)
     * 会把数据表格中的数据拿到当前页面的数据和绑定的一些工具栏
     *
     * 打开
     */
    table.on('tool(saleChances)',function (data){
        //判断类型
        if(data.event == "edit"){//编辑操作
            console.log(data);
            //得到营销机会的id,因为添加操作时没有主键id,则修改时要有主键id。通过主键进行修改
            var saleChanceId = data.data.id;
            //打开修改营销机会窗口
           openSaleChanceDialog(saleChanceId);

        }else if(data.event == "del"){//删除操作
            //弹出确认框，访问用户是否确认删除
            // index是当前弹出框的索引，
            // layer.confirm 的弹出框
            //设置相应的图标和标题，和确认之后要做的操作
            layer.confirm('确认要删除该记录吗?',{icon:3,title:"营销机会管理"},function (index){
                //关闭确认框
                layer.close(index);
                //发送ajax请求，删除记录
                $.ajax({
                    type:"post",
                    url:ctx + "/sale_chance/delete",
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





});
















// //用户列表展示
// var  tableIns = table.render({
//     elem: '#saleChanceList',
//     url : ctx+'/sale_chance/list',
//     cellMinWidth : 95,
//     page : true,
//     height : "full-125",
//     limits : [10,15,20,25],
//     limit : 10,
//     toolbar: "#toolbarDemo",
//     id : "saleChanceListTable",
//     cols : [[
//         {type: "checkbox", fixed:"center"},
//         {field: "id", title:'编号',fixed:"true"},
//         {field: 'chanceSource', title: '机会来源',align:"center"},
//         {field: 'customerName', title: '客户名称',  align:'center'},
//         {field: 'cgjl', title: '成功几率', align:'center'},
//         {field: 'overview', title: '概要', align:'center'},
//         {field: 'linkMan', title: '联系人',  align:'center'},
//         {field: 'linkPhone', title: '联系电话', align:'center'},
//         {field: 'description', title: '描述', align:'center'},
//         {field: 'createMan', title: '创建人', align:'center'},
//         {field: 'createDate', title: '创建时间', align:'center'},
//         {field: 'uname', title: '指派人', align:'center'},
//         {field: 'assignTime', title: '分配时间', align:'center'},
//         {field: 'state', title: '分配状态', align:'center',templet:function(d){
//                 return formatterState(d.state);
//             }},
//         {field: 'devResult', title: '开发状态', align:'center',templet:function (d) {
//                 return formatterDevResult(d.devResult);
//             }},
//         {title: '操作', templet:'#saleChanceListBar',fixed:"right",align:"center", minWidth:150}
//     ]]
// });
//
// function formatterState(state){
//     if(state==0){
//         return "<div style='color:yellow '>未分配</div>";
//     }else if(state==1){
//         return "<div style='color: green'>已分配</div>";
//     }else{
//         return "<div style='color: red'>未知</div>";
//     }
// }
//
// function formatterDevResult(value){
//     /**
//      * 0-未开发
//      * 1-开发中
//      * 2-开发成功
//      * 3-开发失败
//      */
//     if(value==0){
//         return "<div style='color: yellow'>未开发</div>";
//     }else if(value==1){
//         return "<div style='color: #00FF00;'>开发中</div>";
//     }else if(value==2){
//         return "<div style='color: #00B83F'>开发成功</div>";
//     }else if(value==3){
//         return "<div style='color: red'>开发失败</div>";
//     }else {
//         return "<div style='color: #af0000'>未知</div>"
//     }
// }
//
// // 多条件搜索
// $(".search_btn").on("click",function(){
//     table.reload("saleChanceListTable",{
//         page: {
//             curr: 1 //重新从第 1 页开始
//         },
//         where: {
//             customerName: $("input[name='customerName']").val(),  //客户名
//             createMan: $("input[name='createMan']").val(),  //创建人
//             state: $("#state").val()  //状态
//         }
//     })
// });
//
// //头工具栏事件
// table.on('toolbar(saleChances)', function(obj){
//     var checkStatus = table.checkStatus(obj.config.id);
//     switch(obj.event){
//         case "add":
//             openAddOrUpdateSaleChanceDialog();
//             break;
//         case "del":
//             delSaleChance(checkStatus.data);
//             break;
//     };
// });
//
//
// /**
//  * 行监听
//  */
// table.on("tool(saleChances)", function(obj){
//     var layEvent = obj.event;
//     if(layEvent === "edit") {
//         openAddOrUpdateSaleChanceDialog(obj.data.id);
//     }else if(layEvent === "del") {
//         layer.confirm('确定删除当前数据？', {icon: 3, title: "机会数据管理"}, function (index) {
//             $.post(ctx+"/sale_chance/delete",{ids:obj.data.id},function (data) {
//                 if(data.code==200){
//                     layer.msg("操作成功！");
//                     tableIns.reload();
//                 }else{
//                     layer.msg(data.msg, {icon: 5});
//                 }
//             });
//         })
//     }
// });
//
//
// // 打开添加机会数据页面
// function openAddOrUpdateSaleChanceDialog(sid){
//     var url  =  ctx+"/sale_chance/addOrUpdateSaleChancePage";
//     var title="营销机会管理-机会添加";
//     if(sid){
//         url = url+"?id="+sid;
//         title="营销机会管理-机会更新";
//     }
//     layui.layer.open({
//         title : title,
//         type : 2,
//         area:["700px","560px"],
//         maxmin:true,
//         content : url
//     });
// }
//
//
// /**
//  * 批量删除
//  * @param datas
//  */
// function delSaleChance(datas) {
//     if(datas.length==0){
//         layer.msg("请选择删除记录!", {icon: 5});
//         return;
//     }
//     layer.confirm('确定删除选中的机会数据？', {
//         btn: ['确定','取消'] //按钮
//     }, function(index){
//         layer.close(index);
//         var ids= "ids=";
//         for(var i=0;i<datas.length;i++){
//             if(i<datas.length-1){
//                 ids=ids+datas[i].id+"&ids=";
//             }else {
//                 ids=ids+datas[i].id
//             }
//         }
//         $.ajax({
//             type:"post",
//             url:ctx+"/sale_chance/delete",
//             data:ids,
//             dataType:"json",
//             success:function (data) {
//                 if(data.code==200){
//                     tableIns.reload();
//                 }else{
//                     layer.msg(data.msg, {icon: 5});
//                 }
//             }
//         })
//     });
// }