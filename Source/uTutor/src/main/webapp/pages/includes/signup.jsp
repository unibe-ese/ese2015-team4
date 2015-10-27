<form:form method="post" class="form-box" modelAttribute="signUpForm" action="signup" id="signUpForm" autocomplete="off">
	<h1>Sign up</h1>
	<c:if test="${exception_message != null}">
		<div class="exception">
			<c:out value="${exception_message}" />
		</div>
	</c:if>
	<label>
		<form:input path="firstName" id="field-firstName" placeholder="First name"/>
    	<form:errors path="firstName" element="div" class="error"/>
	</label>
	
	<label>
		<form:input path="lastName" id="field-lastName" placeholder="Last name"/>
    	<form:errors path="lastName" element="div" class="error"/>
	</label>
	
	<label>
		<form:input path="email" id="field-email" placeholder="Email"/>
    	<form:errors path="email" element="div" class="error"/>
	</label>
	
	<label>
		<form:input type="password" path="password" id="field-password" placeholder="Enter your password"/>
    	<form:errors path="password" element="div" class="error"/>
	</label>
	
	<label>
		<form:input type="password" path="passwordRepeat" id="field-passwordRepeat" placeholder="Repeat your password"/>
    	<form:errors path="passwordRepeat" element="div" class="error"/>
	</label>
	
	<label>
		<input type="submit" value="Sign up!" />
	</label>
</form:form>