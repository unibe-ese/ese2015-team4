<form:form method="post" class="form-box" modelAttribute="profileEditForm" id="profileEditForm" autocomplete="off">
<h1>Edit profile</h1>
	<c:if test="${exception_message != null}">
		<div class="exception">
			<c:out value="${exception_message}" />
		</div>
	</c:if>
	<label>
		<form:input path="firstName" id="field-firstName" value="${user.firstName}"/>
    	<form:errors path="firstName" element="div" class="error"/>
	</label>
	
	<label>
		<form:input path="lastName" id="field-lastName" value="${user.lastName}"/>
    	<form:errors path="lastName" element="div" class="error"/>
	</label>
	
	<label>
		<input type="submit" value="Save" />
	</label>
	<label>
		<input class="grey submit" type="button" value="Cancel" onClick="document.location.href='<%=request.getContextPath()%>/user/profile';">
	</label>
</form:form>
