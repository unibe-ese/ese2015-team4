<%@include file="../includes/header.jsp"%>
	
	<c:if test="${exception_message != null}">
		<h1>Receiver not found!</h1>
		<div class="exception">
			<c:out value="${exception_message}"/>
		</div>
	</c:if>
	
	<form:form method="post" class="form-box" modelAttribute="newMessageForm" id="newMessageForm" autocomplete="off">
		
		<h1>New message:</h1>
		<div class="hashMap table" style="max-width:560px;overflow:hidden">
				<div class="row">
					<div class="cell key">To</div>
		    		<div class="cell value"><c:out value="${receiverName}" /></div>
				</div>		
		</div>
		
		<div class="hashMap table" style="max-width:560px;overflow:hidden">
				<div class="row">
					<div class="cell key">Subject</div>
		    		<div class="cell value">
		    			<label>
		    				<form:input path="subject" id="field-subject" placeholder="Subject" value="${messageSubject}" />
 						  	<form:errors path="subject" element="div" class="error"/> 						  	
						</label>
		    		</div>
				</div>		
		</div>
		
		<label>
			<form:textarea path="message" id="field-message" placeholder="Please enter your message..." maxlength="255"></form:textarea>
	    	<form:errors path="message" element="div" class="error"/>
		</label>
		
		<label>
			<input type="submit" value="Send!" />
		</label>
		
	</form:form>

<%@include file="../includes/footer.jsp"%>