<%@ page language="java" contentType="text/html; utf-8"
	pageEncoding="utf-8" import="com.yidi.entity.UpperQuestion"
	import="java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>数据库工具</title>
<script src="../lib/jquery.js"></script>
<link href="../css/myshow.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="../css/listswap.css" />
<link
	href="//netdna.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="//cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.6.3/css/bootstrap-select.min.css" />
</head>
<body>
	<br>
	<div>
		<div class="leftitem form-group">
			<textarea id="textn" name="description" rows="15" cols="30"
				onClick="SelectText()"></textarea>
		</div>
		<button type="button" class="leftitem" id="tijiao">提交</button>
		<div class="leftitem">
			<div class="leftitem">
				<label for="solution">Solution:</label>
				<textarea id="solution" rows="15" cols="30"></textarea>
			</div>
			<div class="leftitem">
				<div><label for='youxian'>优先级：</label><input type='text' id='youxian'></div>
				<div>
					<button id="tianjia" class="btn btn-default">添加新的Solution</button>
				</div>
			</div>
		</div>
		<div class="endleft"></div>
		<div class="row-fluid leftitem">
			<span class="help-inline">一级问题</span> <select id="firstselect"
				class="selectpicker" data-show-subtext="true"
				data-live-search="true">
				<c:forEach items="${firstlist}" var="item" varStatus="status">
					<option value="${item.getId()}">${item.getQuestion()}</option>
				</c:forEach>
			</select>
		</div>
		<div class="row-fluid leftitem">
			<span class="help-inline">二级问题</span> <select id="secondselect"
				class="selectpicker" data-show-subtext="true"
				data-live-search="true">
				<option value="0">无</option>
				<c:forEach items="${secondlist}" var="item" varStatus="status">
					<option value="${item.getId()}">${item.getQuestion()}</option>
				</c:forEach>
			</select>
		</div>
		<div class="endleft form-group" id="parameterlist">
			<table id="tbe" class="table"
				style="border-collapse: separate; border-spacing: 0px 7px;">
			</table>
		</div>
	</div>
	<script
		src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<script src="../js/jquery.listswap.js"></script>
	<script src="../js/index.js" charset="utf-8"></script>
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
	<script
		src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.6.3/js/bootstrap-select.min.js"></script>
</body>
</html>