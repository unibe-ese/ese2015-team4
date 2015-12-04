<%
	/**
	/*	This jsp should only be included from timeslot-list.jsp
	*/
%>

<jsp:useBean id="now" class="java.util.Date"/>
				
<h3><c:out value="${timeSlotListSettingTitle}"/></h3>
<div class="list table striped">
	<div class="row header">
		<c:if test="${timeSlotListSettingStatus != 'AVAILABLE'}">
			<div class="cell">
				Student
			</div>
			<div class="cell">
				Tutor
			</div>
		</c:if>
		<div class="cell">
			Date
		</div>
		<div class="cell">
			Time
		</div>
		<c:if test="${ownProfile && user.isTutor && timeSlotListSettingStatus == 'ACCEPTED' && timeSlot.beginDateTime < now}">
			<div class="cell">
				Revenue
			</div>
		</c:if>
	</div>
	<c:set var="timeSlotHeaderShowed" value="${true}"/>
				