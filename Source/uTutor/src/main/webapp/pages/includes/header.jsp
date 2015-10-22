<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
<a href="<%=request.getContextPath()%>"><img id="logo" src="<%=request.getContextPath()%>/img/logo.png"></a>
<form class="search-small" method="GET" action="<%=request.getContextPath()%>/search"><label><input type="text" name="query" placeholder="Search lecture"><input type="submit"></label></form>
<div id="header-actions">
<sec:authorize access="not isAuthenticated()">
<a href="<%=request.getContextPath()%>/login" class="button">Login</a><a href="<%=request.getContextPath()%>/signup" class="button">Sign up</a>
</sec:authorize>
<sec:authorize access="isAuthenticated()">
<select style="max-width:250px;font-size:16px;height:48px;" onChange="var url=this.options[this.selectedIndex].value;if(url!=''){document.location.href='<%=request.getContextPath()%>'+url;}">
	<option value=""><sec:authentication property="principal.username" /></option>
	<option value="/user/profile">My profile</option>
	<option value="/user/password">Change password</option>
	<option value="/logout">Logout</option>
</select>
</sec:authorize>
</div>
<div class="clear"></div>
</div>
</div>
<div id="content-container">