<%@include file="includes/header.jsp"%>
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
		<%@include file="includes/login.jsp"%>
		<%@include file="includes/signup.jsp"%>
	</div>
</sec:authorize>
<%@include file="includes/footer.jsp"%>
