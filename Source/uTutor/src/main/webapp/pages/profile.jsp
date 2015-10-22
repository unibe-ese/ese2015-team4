<%@include file="includes/header.jsp"%>
<c:if test="${exception_message != null}">
<h1>Exception</h1>
<div class="exception"><c:out value="${exception_message}"/></div>
</c:if>
<c:if test="${exception_message == null}">
<h1>
<c:out value="${user.firstName}" /> <c:out value="${user.lastName}" />
</h1>
<div class="profile-data">
<div class="profile-picture">
<img class="profile-picture" src="<%=request.getContextPath()%>/user/img/avatar.jpg?userId=<c:out value="${user.id}"/>"/><br/>
<c:if test="${ownProfile}"><a href="<%=request.getContextPath()%>/user/profile/picture/"><img class="action-icon" src="<%=request.getContextPath()%>/img/edit.png"></a></c:if>
</div>
<div class="float-left">
	<div class="hashMap table">
		<div class="hashMap row">
			<div class="hashMap cell key">First name</div>
		    <div class="hashMap cell value"><c:out value="${user.firstName}" /></div>
		    <div class="hashMap cell icon"><c:if test="${ownProfile}"><a href="<%=request.getContextPath()%>/user/profile/edit/"><img class="action-icon" src="<%=request.getContextPath()%>/img/edit.png"></a></c:if></div>
		</div>
		<div class="hashMap row">
			<div class="hashMap cell key">Last name</div>
		    <div class="hashMap cell value"><c:out value="${user.lastName}" /></div>
		    <div class="hashMap cell icon"><c:if test="${ownProfile}"><a href="<%=request.getContextPath()%>/user/profile/edit/"><img class="action-icon" src="<%=request.getContextPath()%>/img/edit.png"></a></c:if></div>
		</div>
		<c:if test="${ownProfile}">
		<div class="hashMap row">
			<div class="hashMap cell key">Email</div>
		    <div class="hashMap cell value"><c:out value="${user.username}" /> <span class="private">(not public)</span></div>
		</div>
		</c:if>
		<div class="hashMap row">
			<div class="hashMap cell key">State</div>
		    <div class="hashMap cell value"><c:if test="${!user.isTutor}">Student</c:if><c:if test="${user.isTutor}">Tutor</c:if></div>
		</div>		
	</div>
</div>
<div class="clear"></div>
</div>
<c:if test="${!user.isTutor && ownProfile}">	
	<div>
		<a href="<%=request.getContextPath()%>/user/become-tutor" class="button">Become Tutor!</a>
	</div>
</c:if>
</c:if>
<%@include file="includes/footer.jsp"%>