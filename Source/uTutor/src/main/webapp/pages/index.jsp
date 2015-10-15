<%@include file="includes/header.jsp"%>
<div class="center">
	<form id="search-big" class="form-box search-big" method="GET">
	<h1>Find a tutor</h1>
	<label><input type="text" name="query" placeholder="lecture name"><input type="submit"></label>
	</form>
</div>
<div class="center top">
	<%@include file="includes/forms/signup.jsp"%>
</div>

<c:if test="${page_error != null }">
        <div>
            <h4>Error!</h4>
                ${page_error}
        </div>
    </c:if>

<%@include file="includes/footer.jsp"%>
