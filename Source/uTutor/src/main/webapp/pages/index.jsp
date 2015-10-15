<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
<title>uTutor</title>
</head>
<body>

<form:form method="post" modelAttribute="signUpForm" action="signup" id="signUpForm" autocomplete="off">
	
	
	
	<div>
		<form:input path="firstName" id="field-firstName" placeholder="First name"/>
    	<form:errors path="firstName" element="span"/>
	</div>
	
	<div>
		<form:input path="lastName" id="field-lastName" placeholder="Last name"/>
    	<form:errors path="lastName" element="span"/>
	</div>
	
	<div>
		<form:input path="email" id="field-email" tabindex="1" maxlength="45" placeholder="Email"/>
    	<form:errors path="email" element="span"/>
	</div>
	
	<div>
		<form:input type="password" path="password" id="field-password" placeholder="Enter your password"/>
    	<form:errors path="password" element="span"/>
	</div>
	
	<div>
		<form:input type="password" path="passwordRepeat" id="field-passwordRepeat" placeholder="Repeat your password"/>
    	<form:errors path="passwordRepeat" element="span"/>
	</div>
	
	<div>
		<button type="submit" class="btn btn-primary button">Sign up!</button>
	</div>
	
</form:form>

<c:if test="${page_error != null }">
        <div>
            <h4>Error!</h4>
                ${page_error}
        </div>
    </c:if>

</body>
</html>