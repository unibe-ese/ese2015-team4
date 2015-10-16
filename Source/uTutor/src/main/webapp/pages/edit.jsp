<%@include file="includes/header.jsp"%>
<h1>Edit Your Profile</h1>
<form:form method="post" class="form-box" modelAttribute="editForm" action="edit?userId=${user.id}" id="editForm" autocomplete="off">
	<c:if test="${edit_exception != null}">
		<div class="exception">
			<c:out value="${edit_exception}" />
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
		<form:input type="password" path="password" id="field-password" value="${user.password}"/>
    	<form:errors path="password" element="div" class="error"/>
	</label>
	
	<label>
		<form:input type="password" path="passwordRepeat" id="field-passwordRepeat" value="${user.password}"/>
    	<form:errors path="passwordRepeat" element="div" class="error"/>
	</label>
	
	<label>
		<input type="submit" value="Save" />
	</label>
	
</form:form>


<%@include file="includes/footer.jsp"%>