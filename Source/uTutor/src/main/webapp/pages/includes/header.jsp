<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="contextUrl" value="${fn:replace(requestScope['javax.servlet.forward.request_uri'], pageContext.request.contextPath, '')}"/>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>uTutor</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/main.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/forms.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/icons.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/messages.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/table.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/text.css">
	
	<link rel="apple-touch-icon" sizes="57x57" href="<%=request.getContextPath()%>/favicon/apple-touch-icon-57x57.png">
	<link rel="apple-touch-icon" sizes="60x60" href="<%=request.getContextPath()%>/favicon/apple-touch-icon-60x60.png">
	<link rel="apple-touch-icon" sizes="72x72" href="<%=request.getContextPath()%>/favicon/apple-touch-icon-72x72.png">
	<link rel="apple-touch-icon" sizes="76x76" href="<%=request.getContextPath()%>/favicon/apple-touch-icon-76x76.png">
	<link rel="apple-touch-icon" sizes="114x114" href="<%=request.getContextPath()%>/favicon/apple-touch-icon-114x114.png">
	<link rel="apple-touch-icon" sizes="120x120" href="<%=request.getContextPath()%>/favicon/apple-touch-icon-120x120.png">
	<link rel="apple-touch-icon" sizes="144x144" href="<%=request.getContextPath()%>/favicon/apple-touch-icon-144x144.png">
	<link rel="apple-touch-icon" sizes="152x152" href="<%=request.getContextPath()%>/favicon/apple-touch-icon-152x152.png">
	<link rel="apple-touch-icon" sizes="180x180" href="<%=request.getContextPath()%>/favicon/apple-touch-icon-180x180.png">
	<link rel="icon" type="image/png" href="<%=request.getContextPath()%>/favicon/favicon-32x32.png" sizes="32x32">
	<link rel="icon" type="image/png" href="<%=request.getContextPath()%>/favicon/favicon-194x194.png" sizes="194x194">
	<link rel="icon" type="image/png" href="<%=request.getContextPath()%>/favicon/favicon-96x96.png" sizes="96x96">
	<link rel="icon" type="image/png" href="<%=request.getContextPath()%>/favicon/android-chrome-192x192.png" sizes="192x192">
	<link rel="icon" type="image/png" href="<%=request.getContextPath()%>/favicon/favicon-16x16.png" sizes="16x16">
	<link rel="manifest" href="<%=request.getContextPath()%>/manifest.json">
	<link rel="mask-icon" href="<%=request.getContextPath()%>/favicon/safari-pinned-tab.svg" color="#5bbad5">
	<meta name="msapplication-TileColor" content="#da532c">
	<meta name="msapplication-TileImage" content="<%=request.getContextPath()%>/favicon/mstile-144x144.png">
	<meta name="theme-color" content="#ffffff">

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
					<option value="/user/messagecenter">Message center</option>
					<option value="/user/password">Change password</option>
					<option value="/logout">Logout</option>
				</select>
				</sec:authorize>
			</div>
			<div class="clear"></div>
		</div>
	</div>
	<div id="content-container">