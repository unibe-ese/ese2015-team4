<%@include file="../includes/header.jsp"%>

<div class="center">
<!-- BEGIN CHANGE PASSWORD FORM -->
	<form:form method="post" class="form-box" modelAttribute="changePasswordForm" id="changePasswordForm" autocomplete="off">
		<h1>Change password</h1>
		
		<%@include file="../includes/exception.jsp"%>
		
		<label>
			<form:input path="oldPassword" type="password" id="field-oldPassword" placeholder="Actual password"/>
	    	<form:errors path="oldPassword" element="div" class="error"/>
		</label>
		
		<label>
			<form:input path="newPassword" type="password" id="field-newPassword" placeholder="New password"/>
	    	<form:errors path="newPassword" element="div" class="error"/>
		</label>
		
		<label>
			<form:input path="newPasswordRepeat" type="password" id="field-newPasswordRepeat" placeholder="Repeat new password"/>
	    	<form:errors path="newPasswordRepeat" element="div" class="error"/>
		</label>
		
		<label>
			<input type="submit" value="Save" />
		</label>
		
		<label>
			<input class="grey submit" type="button" value="Cancel" onClick="document.location.href='<%=request.getContextPath()%>/user/profile';">
		</label>
	</form:form>
<!-- END CHANGE PASSWORD FORM -->
</div>

<%@include file="../includes/footer.jsp"%>