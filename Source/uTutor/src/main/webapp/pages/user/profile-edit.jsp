<%@include file="../includes/header.jsp"%>
<div class="center">
	<form:form method="post" class="form-box" modelAttribute="profileEditForm" id="profileEditForm" autocomplete="off">
		<h1>Edit profile</h1>
		<c:if test="${exception_message != null}">
			<div class="exception">
				<c:out value="${exception_message}" />
			</div>
		</c:if>
		<label>
			<form:input path="firstName" id="field-firstName"/>
	    	<form:errors path="firstName" element="div" class="error"/>
		</label>
		
		<label>
			<form:input path="lastName" id="field-lastName"/>
	    	<form:errors path="lastName" element="div" class="error"/>
		</label>
		
		<c:if test="${ isTutor == true }">
			<c:choose>	
				<c:when test="${empty profileEditForm.description}">
					<form:textarea path="description" id="field-description" placeholder="Describe yourself here..." maxlength="1000"></form:textarea>
					<form:errors path="description" element="div" class="error"/>
				</c:when>
				<c:otherwise>
					<form:textarea path="description" id="field-description" maxlength="1000"></form:textarea>
					<form:errors path="description" element="div" class="error"/>
				</c:otherwise>
			</c:choose>
		</c:if>
		
		<label>
			<input type="submit" value="Save" />
		</label>
		
		<label>
			<input class="grey submit" type="button" value="Cancel" onClick="document.location.href='<%=request.getContextPath()%>/user/profile';">
		</label>
	</form:form>
</div>
<%@include file="../includes/footer.jsp"%>