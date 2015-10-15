<%@include file="includes/header.jsp"%>
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
