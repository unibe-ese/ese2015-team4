<!-- BEGIN LOGIN FORM -->
<form class="form-box" action="login" method="post" >
	<h1>Login</h1>
	<c:if test="${param.error}" >
		<div class="exception">Username or password not correct.</div>
	</c:if>
	<c:if test="${contextUrl!='/'}">
		<div>New to uTutor? 
			<a href="<%=request.getContextPath()%>/signup/">Sign up</a>
		</div>
	</c:if>
			<label for="username">
				<input id="username" name="username" type="text" placeholder="Email"<c:if test="${param.username!=null}"> value="<c:out value="${param.username}"/>"</c:if>/>
			</label>
			<label>
				<input id="password" name="password" type="password" placeholder="Password"<c:if test="${param.username!=null}"> autofocus</c:if>/>
			</label>
	<label><input type="submit" value="Login"/></label>
</form>
<!-- END LOGIN FORM -->