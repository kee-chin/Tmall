<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2020/5/14
  Time: 9:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %> <!--与其他jsp页设置保持完全一致-->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="../include/admin/adminHeader.jsp"%>
<%@ include file="../include/admin/adminNavigator.jsp"%>

<script>
    $(function () {
        $("#editForm").submit(function () {
            if(!checkEmpty("name", "分类名称"))
                return false;
            return true;
        });
    });
</script>

<div class="workingArea">
    <ol class="breadcrumb">
        <li><a href="admin_category_list">所有分类</a> </li>
        <li class="active">编辑分类</li>
    </ol>

    <div class="panel panel-warning editDiv">
        <div class="panel-heading">编辑分类</div>
        <div class="panel-body"> <!--1.method="post"提交中文；2.enctype="multipart/form-data"提交二进制文件-->
            <form method="post" id="editForm" action="admin_category_update" enctype="multipart/form-data">
                <table class="editTable">
                    <tr>
                        <td>分类名称</td>
                        <td>      <!--controller实体类名称对应，否则400，我在controller以其小写命名实体类-->
                            <input id="name" name="name" value="${category.name}" type="text" class="form-control" />
                        </td>
                    </tr>
                    <tr>
                        <td>分类图片</td>
                        <td>
                            <input id="categoryPic" accept="image/*" type="file" name="image" />
                        </td>
                    </tr>
                    <tr class="submitTR">
                        <td colspan="2" align="center"><!--前端提交数据的字段名称与后台的实体类需一致-->
                            <input type="hidden" name="id" value="${category.id}" />
                            <button type="submit" class="btn btn-success">提交</button>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>
