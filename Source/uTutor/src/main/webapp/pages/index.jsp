<%@include file="includes/header.jsp"%>
<div class="center">
<%@include file="includes/forms/search.jsp"%>
</div>
<sec:authorize access="not isAuthenticated()">
<div class="center top">
	<%@include file="includes/forms/login.jsp"%>
	<%@include file="includes/forms/signup.jsp"%>
</div>
</sec:authorize>
<%@include file="includes/footer.jsp"%>
