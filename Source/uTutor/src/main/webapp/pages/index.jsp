<%@include file="includes/header.jsp"%>
<div class="center">
	<h1>Welcome to uTutor</h1>
	<p style="font-size:75%;font-style:italic">
		<strong style="color:#ff0000">
			Note:
		</strong><br/>
		If you have tested an earlier version of uTutor, please open phpMyAdmin, drop the database (<code style="white-space:nowrap;">DROP DATABASE ututor</code>)<br/>
		and build again, so the db has the correct structure after rebuild.
	</p>
	<p>Description.</p>
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
		<%@include file="includes/login.jsp"%>
		<%@include file="includes/signup.jsp"%>
	</div>
</sec:authorize>
<%@include file="includes/footer.jsp"%>
