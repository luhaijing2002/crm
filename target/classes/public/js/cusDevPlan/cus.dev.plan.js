layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    //将原来写在营销机会管理的数据表格，格式化方法进行拷贝过
    /**
     * 加载数据表格
     */
    var tableIns = table.render({
        //容器元素的ID属性值
        elem: '#saleChanceList'
        //设置容器高度 full-差值
        ,height: 'full-125'
        //单元格的最小宽度。
        ,cellMinWidth: 95
        //访问数据的URL(后台数据接口)设置flag参数，表示查询的是客户开发计划数据
        ,url: ctx + '/sale_chance/list?flag=1' //数据接口
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
            ,{field: 'id', title: '编号',sort: true, fixed: 'left'}//营销机会的id
            ,{field: 'chanceSource', title: '机会来源', align:'center'}
            ,{field: 'customerName', title: '客户名称', align:'center'}
            ,{field: 'cgjl', title: '成功几率', align:'center'}
            ,{field: 'overview', title: '概要', align:'center'}
            ,{field: 'linkMan', title: '联系人', align:'center'}
            ,{field: 'linkPhone', title: '联系号码', align:'center'}
            ,{field: 'description', title: '描述', align:'center'}
            ,{field: 'createMan', title: '创建人', align:'center'}
            ,{field: 'createDate', title: '创建时间', align:'center'}
            ,{field: 'updateDate', title: '修改时间', align:'center'}
            ,{field: 'devResult', title: '开发状态', align:'center', templet: function (d){
                    //调用函数，返回格式化的结果
                    return formatDevState(d.devResult);
                }}
            ,{title: '操作',templet:'#op',fixed: 'right',align:'center',minWidth:150}
        ]]
    });


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
                ,devResult:$("#devResult").val()//开发状态
            }
            ,page: {
                curr: 1 //重新从第 1 页开始
            }
        });
    });

    /**
     * 行工具栏监听
     */
    table.on("tool(saleChances)",function (data){
        console.log(data);
        //判断类型
        if(data.event == "dev"){//开发
            //打开计划项开发与详情页面
            openCusDevPlanDialog('计划项数据开发',data.data.id);

        }else if(data.event == "info"){//详情
            //打开计划项开发与详情页面
            openCusDevPlanDialog('计划项数据维护',data.data.id);

        }
    });


    /**
     * 打开计划项开发或详情页面,并进行后台请求
     * @param title
     * @param id
     */
    function openCusDevPlanDialog(title,id){

        //打开窗口
        layui.layer.open({
            //类型
            title : title,
            //类型
            type : 2,
            //宽高
            area:["750px","550px"],
            //可以最大化与最小化
            maxmin:true,
            //url地址
            content: ctx + "/cus_dev_plan/toCusDevPlanPage?id="+id,
        });

    }

















});



// //用户列表展示
// var  tableIns = table.render({
//     elem: '#saleChanceList',
//     url : ctx+'/sale_chance/list?state=1&flag=1',
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
//         {field: 'devResult', title: '开发状态', align:'center',templet:function (d) {
//                 return formatterDevResult(d.devResult);
//             }},
//         {title: '操作',fixed:"right",align:"center", minWidth:150,templet:"#op"}
//     ]]
// });
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
//
// // 多条件搜索
// $(".search_btn").on("click",function(){
//     table.reload("saleChanceListTable",{
//         page: {
//             curr: 1 //重新从第 1 页开始
//         },
//         where: {
//             customerName: $("input[name='customerName']").val(),  //客户名
//             createMan: $("input[name='createMan']").val(), //创建人
//             devResult:$("#devResult").val()  //开发状态
//         }
//     })
// });
//
//
// /**
//  * 行监听
//  */
// table.on("tool(saleChances)", function(obj){
//     var layEvent = obj.event;
//     if(layEvent === "dev") {
//         openCusDevPlanDialog("计划项数据维护",obj.data.id);
//     }else if(layEvent === "info") {
//         openCusDevPlanDialog("计划项数据详情",obj.data.id);
//     }
//
// });
//
//
// // 打开开发计划对话框
// function openCusDevPlanDialog(title,sid){
//     layui.layer.open({
//         title : title,
//         type : 2,
//         area:["750px","550px"],
//         maxmin:true,
//         content : ctx+"/cus_dev_plan/toCusDevPlanDataPage?sid="+sid
//     });
// }
