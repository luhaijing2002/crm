<!DOCTYPE html>
<html>
<head>
    <#include "../common.ftl">

</head>
<body class="childrenBody">
<form class="layui-form" style="width:80%;">
    <#--用户id存放在隐藏域中-->
    <input name="id" type="hidden" value="${(updateUser.id)!}"/>

    <div class="layui-form-item layui-row layui-col-xs12">
        <label class="layui-form-label">用户名</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input userName"
                   lay-verify="required" name="userName" id="userName"  value="${(updateUser.userName)!}" placeholder="请输入用户名">
        </div>
    </div>
    <div class="layui-form-item layui-row layui-col-xs12">
        <label class="layui-form-label">真实姓名</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input userName"
                   lay-verify="required" name="trueName" id="trueName" value="${(updateUser.trueName)!}" placeholder="请输入真实姓名">
        </div>
    </div>
    <div class="layui-form-item layui-row layui-col-xs12">
        <label class="layui-form-label">邮箱</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input userEmail"
                   lay-verify="email" name="email" value="${(updateUser.email)!}"
                   id="email"
                   placeholder="请输入邮箱">
        </div>
    </div>

    <div class="layui-form-item layui-row layui-col-xs12">
        <label class="layui-form-label">手机号</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input userEmail"
                   lay-verify="phone" name="phone" value="${(updateUser.phone)!}" id="phone" placeholder="请输入手机号">
        </div>
    </div>

    <#--下拉框-->
    <div class="magb15 layui-col-md4 layui-col-xs12">
        <label class="layui-form-label">角色</label>
        <div class="layui-input-block">
            <select name="roleIds"  xm-select="selectId">
            </select>
        </div>
    </div>


    <br/>
    <div class="layui-form-item layui-row layui-col-xs12">
        <div class="layui-input-block">
            <button class="layui-btn layui-btn-lg" lay-submit=""
                    lay-filter="addOrUpdateUser">确认
            </button>
            <button class="layui-btn layui-btn-lg layui-btn-normal" id="closeBtn">取消</button>
        </div>
    </div>
</form>
<script type="text/javascript" src="${ctx}/js/user/add.update.js"></script>
</body>
</html>