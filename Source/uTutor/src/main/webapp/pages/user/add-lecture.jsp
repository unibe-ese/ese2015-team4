<%@include file="../partials/header.jsp"%>

<div class="center">
<!-- BEGIN ADD LECTURE FORM -->
	<form:form method="post" class="form-box" modelAttribute="addLectureForm" action="add-lecture" id="add-lecture" autocomplete="off">
		<h1>Add a lecture to your profile!</h1>	
		<c:if test="${exception_message != null}">
			<div class="exception">
				<c:out value="${exception_message}" />
			</div>
		</c:if>
		<%@include file="partials/add-lecture.jsp" %>
		<label>
			<input type="submit" value="Add Lecture!" />
		</label>
	</form:form>
<!-- END ADD LECTURE FORM -->
</div>

<%@include file="../partials/footer.jsp"%>