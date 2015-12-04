<%@include file="partials/header.jsp"%>

<div class="center">
	<h1>Welcome to uTutor</h1>
	<p>Find a tutor. Or become one.</p>
</div>

<div class="center">
	<!-- BEGIN SEARCH FORM -->
	<form id="search-big" action="<%=request.getContextPath()%>/search" class="form-box search-big" method="GET">
		<h1>Find a tutor</h1>
		<label><input type="text" name="query" placeholder="Search lecture"><input type="submit"></label>
	</form>
	<!-- END SEARCH FORM -->
</div>

<sec:authorize access="not isAuthenticated()">
	<div class="center top">
		<%@include file="partials/login-form.jsp"%>
		<%@include file="partials/signup-form.jsp"%>
	</div>
</sec:authorize>

<%@include file="partials/footer.jsp"%>
