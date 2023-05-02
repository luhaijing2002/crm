$(function (){
    //加载树形结构
    loadModuleData();
});

//加载树形结构
function loadModuleData(){
    var zTreeObj;
    //配置信息对象 zTree的参数配置
    var setting = {
        //使用复选框
        check: {
            enable: true
        },
        //使用简单的JSON数据
        data: {
            simpleData: {
                enable: true
            }
        },
        callback: {
            //onCheck函数：当checkbox/radio被选中或取消选中时触发的函数
            onCheck: zTreeOnCheck
        }
    };

    //数据
    //通过ajax查询资源列表
    $.ajax({
        type:"get",
        url:ctx + "/module/queryAllModules",
        data:{
            roleId:$("[name='roleId']").val()
        },
        dataType:"json",
        success:function (data){//查询到的数据
            //data:查询到的资源列表
            //加载zTree树插件
                //因为你是异步的请求，所以要渲染要写到异步的回调函数里面，这样才能等到数据返回时，加载树形结构。
                //指定树形结构存放的位置
                zTreeObj = $.fn.zTree.init($("#test1"), setting, data);

        }
    })


    /**
     * 当checkbox/radio被选中或取消选中时触发的函数
     * @param event 事件
     * @param treeId 树id
     * @param treeNode 树节点
     */
    function zTreeOnCheck(event, treeId, treeNode) {
        //alert(treeNode.tId + ", " + treeNode.name + "," + treeNode.checked);
        //getCheckedNodes(checked):获取所有被勾选的节点集合，
        //如果checked=true,表示获取勾选的节点；如果checked=false,表示获取未勾选的节点。
        var nodes = zTreeObj.getCheckedNodes(true);
        // console.log(nodes);

        //获取所有的资源的id值， mIds=1&mIds=2&mIds=3
        //行进行判断数组
        //
        var mIds = '';
        if(nodes != null && nodes.length > 0){
            //遍历节点集合，获取资源的id
            for (var i = 0; i < nodes.length; i++) {
                if(i < nodes.length - 1){
                    mIds += "mIds=" + nodes[i].id + '&';
                }else {
                    mIds += "mIds=" + nodes[i].id;
                }
            }
            console.log(mIds);
        }
        //获取需要授权的角色ID的值，(隐藏域)
        var roleId = $("[name = 'roleId']").val();

        //发送ajax请求，执行角色的授权操作
        $.ajax({
            type:"post",
            url: ctx + "/role/addGrant",
            data:mIds + "&roleId="+roleId,
            dataType:"json",
            success:function (data){
                console.log(data);
            }
        })

    };

}
















































// $(function () {
//     loadModuleInfo();
// });
//
//
// var zTreeObj;
// function loadModuleInfo() {
//     $.ajax({
//         type:"post",
//         url:ctx+"/module/queryAllModules?roleId="+$("input[name='roleId']").val(),
//         dataType:"json",
//         success:function (data) {
//
//             // zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）
//             var setting = {
//                 check: {
//                     enable: true
//                 },
//                 data: {
//                     simpleData: {
//                         enable: true
//                     }
//                 },
//                 callback: {
//                     onCheck: zTreeOnCheck
//                 }
//             };
//             zTreeObj = $.fn.zTree.init($("#test1"), setting, data);
//         }
//     })
// }
//
//
// function zTreeOnCheck(event, treeId, treeNode) {
//     //alert(treeNode.tId + ", " + treeNode.name + "," + treeNode.checked);
//     var nodes= zTreeObj.getCheckedNodes(true);
//     var mids="mids=";
//     for(var i=0;i<nodes.length;i++){
//         if(i<nodes.length-1){
//             mids=mids+nodes[i].id+"&mids=";
//         }else{
//             mids=mids+nodes[i].id;
//         }
//     }
//
//     $.ajax({
//         type:"post",
//         url:ctx+"/role/addGrant",
//         data:mids+"&roleId="+$("input[name='roleId']").val(),
//         dataType:"json",
//         success:function (data) {
//             console.log(data);
//         }
//     })
//
// }