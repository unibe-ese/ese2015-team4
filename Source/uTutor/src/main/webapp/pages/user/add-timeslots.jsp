<%@include file="../includes/header.jsp"%>

<div class="center">
<!-- BEGIN ADD TIMESLOTS FORM -->
	<form:form method="post" class="form-box" modelAttribute="addTimeslotsForm" action="add-timeslots" id="add-timeslots" autocomplete="off">
		<h1>Add timeslots to your profile!</h1>	
		<c:if test="${exception_message != null}">
			<div class="exception">
				<c:out value="${exception_message}" />
			</div>
		</c:if>
		<label>
			<form:input type="date" path="date" id="field-date" placeholder="yyyy-mm-dd"/>
   			<form:errors path="date" element="div" class="error"/>
		</label>
		<label>
			<form:select multiple="true" path="timeslots" id="field-timeslots">
				<form:options items="${possibleTimeslots}"/>
			</form:select>
   			<form:errors path="timeslots" element="div" class="error"/>
		</label>
		
		<label>
			<input type="submit" value="Add Timeslots!" />
		</label>
	</form:form>
<!-- END ADD LECTURE FORM -->
</div>

<%@include file="../includes/footer.jsp"%>