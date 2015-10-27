<%@include file="../includes/header.jsp"%>
<div class="center">
	<form:form method="post" class="form-box" modelAttribute="addLectureForm" action="add-lecture" id="add-lecture" autocomplete="off">
		<h1>Add a lecture to your profile!</h1>	
		<c:if test="${exception_message != null}">
			<div class="exception">
				<c:out value="${exception_message}" />
			</div>
		</c:if>
		<%@include file="includes/add-lecture.jsp" %>
		<label>
			<input type="submit" value="Add Lecture!" />
		</label>
	</form:form>
</div>
<%@include file="../includes/footer.jsp"%>