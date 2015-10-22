
<form:form method="post" class="form-box" modelAttribute="becomeTutorForm" action="become-tutor" id="becomeTutor" autocomplete="off">
	<h1>Become a tutor!</h1>	
	
	<c:if test="${exception_message != null}">
		<div class="exception">
			<c:out value="${exception_message}" />
		</div>
	</c:if>
	
	<label>
		<form:textarea path="description" id="field-description" placeholder="Describe yourself here..."></form:textarea>
    	<form:errors path="description" element="div" class="error"/>
	</label>
	
	<label>
		<form:input path="price" id="field-price" placeholder="Price per hour"/>
    	<form:errors path="price" element="div" class="error"/>
	</label>
	
	<label>
		<form:input path="lecture" id="field-lecture" placeholder="Lecture"/>
    	<form:errors path="lecture" element="div" class="error"/>
	</label>
	
	<label>
		<div class="controls">
		Grade: 
        		<form:select path="grade">
        			<form:option value="4">4</form:option>
        			<form:option value="4.5">4.5</form:option>
        			<form:option value="5">5</form:option>
        			<form:option value="5.5">5.5</form:option>
        			<form:option value="6">6</form:option>
        		</form:select>
    			<form:errors path="grade" element="div" class="error"/>
        </div>
	</label>
	
	<label>
		<input type="submit" value="Become tutor!" />
	</label>
	
</form:form>