<c:if test="${exception_message != null}">
	<!-- BEGIN EXCEPTION MESSAGE -->
	<div class="exception">
		<c:out value="${exception_message}"/>
	</div>
	<!-- END EXCEPTION MESSAGE -->
</c:if>