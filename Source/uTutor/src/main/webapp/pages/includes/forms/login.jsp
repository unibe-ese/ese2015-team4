<form:form method="post" class="form-box" modelAttribute="loginForm" action="login" id="loginForm" autocomplete="off">
	<h1>Login</h1>
	<c:if test="${login_exception != null}">
		<div class="exception">
			<c:out value="${login_exception}" />
		</div>
	</c:if>
	<label>
		<form:input path="email" id="field-email" placeholder="Email"/>
    	<form:errors path="email" element="div" class="error"/>
	</label>
	
	<label>
		<form:input type="password" path="password" id="field-password" placeholder="Password"/>
    	<form:errors path="password" element="div" class="error"/>
	</label>	
	<label>
		<input type="submit" value="Login" />
	</label>
	
</form:form>
