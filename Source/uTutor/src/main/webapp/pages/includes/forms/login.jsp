<form class="form-box" action="j_spring_security_check" method="post" >
			<h1>Login</h1>
			<c:if test="${param.error}" >
			<div class="exception">Username or password not correct.</div>
			</c:if>
			<c:if test="${!isHome}"><div>New to uTutor? <a href="<%=request.getContextPath()%>/signup/">Sign up</a></div></c:if>
			<label for="j_username"><input id="j_username" name="j_username" type="text" placeholder="Email"<c:if test="${param.username!=null}"> value="<c:out value="${param.username}"/>"</c:if>/></label>
			<label><input id="j_password" name="j_password" type="password" placeholder="Password"<c:if test="${param.username!=null}"> autofocus</c:if>/></label>
			
	<label><input type="submit" value="Login"/></label>
</form>
