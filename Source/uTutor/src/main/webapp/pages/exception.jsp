<%@include file="includes/header.jsp"%>
<h1>Sorry, something went wrong!</h1>
<c:if test="${exception_message != null}">
	<div class="exception">
		<c:out value="${exception_message}" />
	</div>
</c:if>
<%@include file="includes/footer.jsp"%>