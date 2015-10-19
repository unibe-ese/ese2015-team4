<%@include file="includes/header.jsp"%>
<div class="center">
	<form id="search-big" class="form-box search-big" method="GET">
	<h1>Find a tutor</h1>
	<label><input type="text" name="query" placeholder="lecture name"><input type="submit"></label>
	</form>
</div>
<sec:authorize access="not isAuthenticated()">
<div class="center top">
	<%@include file="includes/forms/login.jsp"%>
	<%@include file="includes/forms/signup.jsp"%>
</div>
</sec:authorize>
<%@include file="includes/footer.jsp"%>
