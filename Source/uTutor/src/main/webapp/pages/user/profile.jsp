<%@include file="../includes/header.jsp"%>
<c:if test="${exception_message != null}">
	<h1>Exception</h1>
	<div class="exception">
		<c:out value="${exception_message}"/>
	</div>
</c:if>
<c:if test="${exception_message == null}">
	<c:if test="${ownProfile}">	
		<form name="frmProfileActions" method="POST">
			<input type="hidden" name="action" id="action" value="update" />
			<input type="hidden" name="objectId" id="objectId" value="0" />
		</form>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/profile.js"></script>
	</c:if>
	<h1><c:out value="${user.firstName}" /> <c:out value="${user.lastName}" /></h1>
	<div class="profile-data">
		<div class="profile-picture">
			<img class="profile-picture" src="<%=request.getContextPath()%>/img/user.jpg?userId=<c:out value="${user.id}"/>"/><br/>
			<c:if test="${ownProfile}">
				<a href="<%=request.getContextPath()%>/user/profile/picture/"><img class="action-icon" src="<%=request.getContextPath()%>/img/edit.png"></a>
			</c:if>
		</div>
		<div class="float-left">
			<div class="hashMap table" style="max-width:560px;overflow:hidden">
				<div class="row">
					<div class="cell key">First name</div>
		    		<div class="cell value"><c:out value="${user.firstName}" /></div>
		    		<div class="cell icon"><c:if test="${ownProfile}"><a href="<%=request.getContextPath()%>/user/profile/edit/"><img class="action-icon" src="<%=request.getContextPath()%>/img/edit.png"></a></c:if></div>
				</div>
				<div class="row">
					<div class="cell key">Last name</div>
		    		<div class="cell value"><c:out value="${user.lastName}" /></div>
		    		<div class="cell icon"><c:if test="${ownProfile}"><a href="<%=request.getContextPath()%>/user/profile/edit/"><img class="action-icon" src="<%=request.getContextPath()%>/img/edit.png"></a></c:if></div>
				</div>
				<c:if test="${ownProfile}">
					<div class="row">
						<div class="cell key">Email</div>
		    			<div class="cell value"><c:out value="${user.username}" /> <span class="private">(not public)</span></div>
					</div>
				</c:if>
				<div class="row">
					<div class="cell key">State</div>
		    		<div class="cell value"><c:if test="${!user.isTutor}">Student</c:if><c:if test="${user.isTutor}">Tutor</c:if></div>
				</div>
				<c:if test="${user.isTutor}">
					<div class="row">
						<div class="cell key">Price per hour</div>
		    			<div class="cell value"><c:out value="${user.price}" /></div>
					</div>
					<div class="row">
						<div class="cell key multiline" style="padding-top:15px">Description</div>
		    			<div class="cell value multiline"><c:out value="${user.description}" /></div>
		    			<div class="cell icon multiline"><c:if test="${ownProfile}"><a href="<%=request.getContextPath()%>/user/profile/edit/"><img class="action-icon" src="<%=request.getContextPath()%>/img/edit.png"></a></c:if></div>
					</div>
				</c:if>	
			</div>
		</div>
		<div class="clear"></div>
	<c:if test="${user.isTutor}">
	<h3>Lectures:</h3>	
			<div class="list table">
			<div class="row header">
			<div class="cell">Lecture</div>
			<div class="cell">Grade</div>
			</div>
			<c:forEach items="${lectures}" var="result">
					<div class="row">
						<div class="cell"><c:out value="${result.lecture.name}" /></div>
		    			<div class="cell"><c:out value="${result.grade}" /></div>
		    			<div class="cell icon"><c:if test="${ownProfile}"><img class="action-icon" src="<%=request.getContextPath()%>/img/delete.png" onClick="profileAction('deleteLecture',<c:out value="${result.id}"/>);" /></c:if></div>
					</div>
			</c:forEach>
			<c:if test="${ownProfile}">
			<div class="row">
				<div class="cell"><a class="button action" href="<%=request.getContextPath()%>/user/add-lecture">+ add lecture</a></div>
			</div>
			</c:if>
			</div>
</c:if>

<c:if test="${!user.isTutor && ownProfile}">	
	<div>
		<a href="<%=request.getContextPath()%>/user/become-tutor" class="button">Become Tutor!</a>
	</div>
	
</c:if>
	</div>
</c:if>
<%@include file="../includes/footer.jsp"%>