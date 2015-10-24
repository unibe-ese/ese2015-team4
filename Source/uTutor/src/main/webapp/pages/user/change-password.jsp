<%@include file="../includes/header.jsp"%>
<div class="center">
<form:form method="post" class="form-box" modelAttribute="changePasswordForm" id="changePasswordForm" autocomplete="off">
<h1>Change password</h1>
	<c:if test="${exception_message != null}">
		<div class="exception">
			<c:out value="${exception_message}" />
		</div>
	</c:if>
	<label>
		<form:input path="oldPassword" type="password" id="field-oldPassword" value="${oldPassword}" placeholder="Actual password"/>
    	<form:errors path="oldPassword" element="div" class="error"/>
	</label>
	<label>
		<form:input path="newPassword" type="password" id="field-newPassword" value="${newPassword}" placeholder="New password"/>
    	<form:errors path="newPassword" element="div" class="error"/>
	</label>
	<label>
		<form:input path="newPasswordRepeat" type="password" id="field-newPasswordRepeat" value="${newPasswordRepeat}" placeholder="Repeat new password"/>
    	<form:errors path="newPasswordRepeat" element="div" class="error"/>
	</label>
	
	<label>
		<input type="submit" value="Save" />
	</label>
	<label>
		<input class="grey submit" type="button" value="Cancel" onClick="document.location.href='<%=request.getContextPath()%>/user/profile';">
	</label>
</form:form>
</div>
<%@include file="../includes/footer.jsp"%>