<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>uTutor</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>
<div id="header-container">
<div id="header-inner">
<a href="<%=request.getContextPath()%>"><img id="logo" src="img/logo.png"></a>
<form class="search-small" method="GET"><label><input type="text" name="query" placeholder="lecture"><input type="submit"></label></form>
<div id="header-actions"><a href="<%=request.getContextPath()%>/login" class="button">Login</a><a href="<%=request.getContextPath()%>/signup" class="button">Sign up</a></div>
<div class="clear"></div>
</div>
</div>
<div id="content-container">