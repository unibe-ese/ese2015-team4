<%@include file="includes/header.jsp"%>

<c:if test="${exception_message != null}">
	<h1>Sorry, something went wrong!</h1>
	<div class="exception">
		<c:out value="${exception_message}" />
	</div>
</c:if>

<%@include file="includes/footer.jsp"%>