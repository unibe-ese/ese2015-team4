<%@include file="../includes/header.jsp"%>
<div class="center">
	<form:form method="post" class="form-box" modelAttribute="becomeTutorForm" action="become-tutor" id="becomeTutorForm" autocomplete="off">
		<h1>Become a tutor!</h1>	
		
		<c:if test="${exception_message != null}">
			<div class="exception">
				<c:out value="${exception_message}" />
			</div>
		</c:if>
		
		<c:choose>
			<c:when test="${empty becomeTutorForm.description}">
				<form:textarea path="description" id="field-description" placeholder="Describe yourself here..." maxlength="1000"></form:textarea>
				<form:errors path="description" element="div" class="error"/>
			</c:when>
			<c:otherwise>
				<form:textarea path="description" id="field-description" maxlength="1000"></form:textarea>
				<form:errors path="description" element="div" class="error"/>
			</c:otherwise>
		</c:choose>
				
		<label>
			<form:input path="price" id="field-price" placeholder="Price per hour"/>
	    	<form:errors path="price" element="div" class="error"/>
		</label>
		
		<%@include file="includes/add-lecture.jsp" %>
		
		<label>
			<input type="submit" value="Become tutor!" />
		</label>
	</form:form>
</div>
<%@include file="../includes/footer.jsp"%>