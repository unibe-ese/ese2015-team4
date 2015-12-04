<%@include file="../partials/header.jsp"%>

<div class="center">
<!-- BEGIN BECOME TUTOR FORM -->
	<form:form method="post" class="form-box" modelAttribute="becomeTutorForm" action="become-tutor" id="becomeTutorForm" autocomplete="off">
		<h1>Become a tutor!</h1>	
		
		<%@include file="../partials/exception.jsp"%>
		
		<form:textarea path="description" id="field-description" placeholder="Describe yourself here..."></form:textarea>
		<form:errors path="description" element="div" class="error"/>
				
		<label>
			<form:input path="price" id="field-price" placeholder="Price per hour"/>
	    	<form:errors path="price" element="div" class="error"/>
		</label>
		
		<%@include file="partials/add-lecture.jsp" %>
		
		<label>
			<input type="submit" value="Become tutor!" />
		</label>
	</form:form>
<!-- END BECOME TUTOR FORM -->
</div>

<%@include file="../partials/footer.jsp"%>