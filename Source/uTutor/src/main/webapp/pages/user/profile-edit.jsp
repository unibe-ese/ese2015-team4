<%@include file="../includes/header.jsp"%>

<div class="center">
<!-- BEGIN PROFILE EDIT FORM -->
	<form:form method="post" class="form-box" modelAttribute="profileEditForm" id="profileEditForm" autocomplete="off">
		<h1>Edit profile</h1>
		
		<%@include file="../includes/exception.jsp"%>
		
		<label>
			<form:input path="firstName" id="field-firstName"/>
	    	<form:errors path="firstName" element="div" class="error"/>
		</label>
		
		<label>
			<form:input path="lastName" id="field-lastName"/>
	    	<form:errors path="lastName" element="div" class="error"/>
		</label>
		
		<c:if test="${ isTutor == true }">
			<form:textarea path="description" id="field-description" placeholder="Describe yourself here..." maxlength="1000"></form:textarea>
			<form:errors path="description" element="div" class="error"/>
		</c:if>
		
		<label>
			<input type="submit" value="Save" />
		</label>
		
		<label>
			<input class="grey submit" type="button" value="Cancel" onClick="document.location.href='<%=request.getContextPath()%>/user/profile';">
		</label>
	</form:form>
<!-- END PROFILE EDIT FORM -->
</div>

<%@include file="../includes/footer.jsp"%>