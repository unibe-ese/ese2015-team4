<%@include file="../includes/header.jsp"%>
<jsp:useBean id="now" class="java.util.Date"/>

<c:if test="${ownProfile || user.isTutor}">
	<%@include file="../includes/hidden-actions.jsp"%>
</c:if>

<h1><c:out value="${user.firstName}" /> <c:out value="${user.lastName}" /></h1>
	
<%@include file="../includes/exception.jsp"%>

<!-- BEGIN PROFIL DATA -->
<div class="profile-data">
	<!-- BEGIN PROFILE PICTURE -->
	<div class="profile-picture">
		<img class="profile-picture" src="<%=request.getContextPath()%>/img/user.jpg?userId=<c:out value="${user.id}"/>"/><br/>
		<c:choose>	
			<c:when test="${ownProfile}">
				<a href="<%=request.getContextPath()%>/user/profile/picture/">
					<img class="action-icon" src="<%=request.getContextPath()%>/img/edit.png">
				</a>
			</c:when>
			<c:otherwise>
				<a href="<%=request.getContextPath()%>/user/messagecenter/new?receiverId=<c:out value="${user.id}"/>" class="button action">
					+ new message
				</a>
			</c:otherwise>
		</c:choose>
	</div>
	<!-- END PROFILE PICTURE -->
	<!-- BEGIN USER DATA -->
	<div class="float-left">
		<div class="hashMap table" style="max-width:560px;overflow:hidden">
			<div class="row">
				<div class="cell key">
					First name
				</div>
	    		<div class="cell value">
	    			<c:out value="${user.firstName}" />
	    		</div>
	    		<div class="cell icon">
	    			<c:if test="${ownProfile}">
	    				<a href="<%=request.getContextPath()%>/user/profile/edit/">
	    					<img class="action-icon" src="<%=request.getContextPath()%>/img/edit.png">
	    				</a>
	    			</c:if>
	    		</div>
			</div>
			<div class="row">
				<div class="cell key">
					Last name
				</div>
	    		<div class="cell value">
	    			<c:out value="${user.lastName}" />
	    		</div>
	    		<div class="cell icon">
	    			<c:if test="${ownProfile}">
	    				<a href="<%=request.getContextPath()%>/user/profile/edit/">
	    					<img class="action-icon" src="<%=request.getContextPath()%>/img/edit.png">
	    				</a>
	    			</c:if>
	    		</div>
			</div>
			<c:if test="${ownProfile}">
				<div class="row">
					<div class="cell key">
						Email
					</div>
	    			<div class="cell value">
	    				<c:out value="${user.username}" /> <span class="private">(not public)</span>
	    			</div>
				</div>
			</c:if>
			<div class="row">
				<div class="cell key">
					State
				</div>
	    		<div class="cell value">
	    			<c:if test="${!user.isTutor}">
	    				Student
	    			</c:if>
	    			<c:if test="${user.isTutor}">
	    				Tutor
	    			</c:if>
	    		</div>
			</div>
			<c:if test="${user.isTutor}">
				<div class="row">
					<div class="cell key">
						Price per hour
					</div>
	    			<div class="cell value">
	    				CHF <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${user.price}" />
	    			</div>
				</div>
				<div class="row">
					<div class="cell key">
						Rating
					</div>
	    			<div class="cell value" style="font-weight:normal">
	    				<c:set var="rating" value="${user.rating}"/>
	    				<%@include file="../includes/rating.jsp"%>
	    			</div>
				</div>
				<div class="row">
					<div class="cell key multiline" style="padding-top:15px">
						Description
					</div>
		<!-- BEGIN DESCRIPTION (don't add white-spaces inside the div!!!)  -->
	    			<div class="cell value multiline pre"><c:out value="${user.description}" /></div>
		<!-- END DESCRIPTION -->
	    			<div class="cell icon multiline">
	    				<c:if test="${ownProfile}">
	    					<a href="<%=request.getContextPath()%>/user/profile/edit/">
	    						<img class="action-icon" src="<%=request.getContextPath()%>/img/edit.png">
	    					</a>
	    				</c:if>
	    			</div>
				</div>
			</c:if>	
		</div>
	</div>
	<!-- END USER DATA -->
	<div class="clear"></div>
	
	<c:if test="${!user.isTutor && ownProfile}">	
		<div>
			<a href="<%=request.getContextPath()%>/user/become-tutor" class="button">
				Become Tutor!
			</a>
		</div>
	</c:if>
	
	<c:if test="${user.isTutor}">
	<!-- BEGIN LECTURES -->
		<h3>Lectures:</h3>	
		<div class="list table striped">
			<div class="row header">
				<div class="cell">
					Lecture
				</div>
				<div class="cell">
					Grade
				</div>
			</div>
			<c:forEach items="${lectures}" var="result">
				<div class="row">
					<div class="cell">
						<c:out value="${result.lecture.name}" />
					</div>
		    		<div class="cell">
		    			<c:out value="${result.grade}" />
		    		</div>
		    		<div class="cell">
		    			<c:if test="${ownProfile}">
		    				<img class="action-icon" src="<%=request.getContextPath()%>/img/delete.png" onClick="hiddenAction('deleteLecture',<c:out value="${result.id}"/>);" />
		    			</c:if>
		    		</div>
				</div>
			</c:forEach>
		</div>
		<c:if test="${ownProfile}">
			<a class="button action" href="<%=request.getContextPath()%>/user/add-lecture">
				+ add lecture
			</a>
		</c:if>
	<!-- END LECTURES -->
	<p></p>
	<!-- BEGIN AVAILABILITIES -->
			<c:set var="timeSlotListSettingTitle" value="Availability" />
			<c:set var="timeSlotListSettingStatus" value="AVAILABLE" />
			<c:set var="timeSlotListSettingFuture" value="${true}" />
			<c:set var="timeSlotListSettingPast" value="${false}" />
			<%@include file="includes/timeslot-list.jsp"%>
	<!-- END AVAILABILITIES -->	
	<p></p>
	</c:if>
	
	<c:if test="${ownProfile}">
	<!-- BEGIN REQUESTS -->
			<c:set var="timeSlotListSettingTitle" value="Requests" />
			<c:set var="timeSlotListSettingStatus" value="REQUESTED" />
			<c:set var="timeSlotListSettingFuture" value="${true}" />
			<c:set var="timeSlotListSettingPast" value="${false}" />
			<%@include file="includes/timeslot-list.jsp"%>
	<!-- END REQUESTS -->
	<p></p>
	<!-- BOOKED -->
			<c:set var="timeSlotListSettingTitle" value="Booked" />
			<c:set var="timeSlotListSettingStatus" value="ACCEPTED" />
			<c:set var="timeSlotListSettingFuture" value="${true}" />
			<c:set var="timeSlotListSettingPast" value="${false}" />
			<%@include file="includes/timeslot-list.jsp"%>
	<!-- END BOOKED -->
	<p></p>
	<!-- HISTORY -->
			<c:set var="timeSlotListSettingTitle" value="History" />
			<c:set var="timeSlotListSettingStatus" value="ACCEPTED" />
			<c:set var="timeSlotListSettingFuture" value="${false}" />
			<c:set var="timeSlotListSettingPast" value="${true}" />
			<%@include file="includes/timeslot-list.jsp"%>
	<!-- END HISTORY -->
	</c:if>
</div>
<!-- END PROFIL DATA -->

<%@include file="../includes/footer.jsp"%>