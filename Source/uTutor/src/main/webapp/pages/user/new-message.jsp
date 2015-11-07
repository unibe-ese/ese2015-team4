<%@include file="../includes/header.jsp"%>

	<c:if test="${exception_message != null}">
		<div class="exception">
			<c:out value="${exception_message}"/>
		</div>
	</c:if>
	<div class="center">
<!-- BEGIN NEW MESSAGE FORM -->
		<form:form method="post" class="form-box" modelAttribute="newMessageForm" id="newMessageForm" autocomplete="off">
			<form:input path="receiverId" id="field-receiverId" type="hidden" value="${receiverId}" />
			<form:input path="receiverDisplayName" id="field-receiverDisplayName" type="hidden" value="${receiverDisplayName}" />
			
			<h1>New message:</h1>
			<div class="table hashMap">
				<div class="row">
					<div class="cell key">
						To
					</div>
		    		<div class="cell value">
		    			<c:out value="${newMessageForm.receiverDisplayName}" />
		    		</div>
				</div>		
			</div>
			<label>
		    	<form:input path="subject" id="field-subject" placeholder="Subject" value="${messageSubject}" style="width:600px"/>
 				<form:errors path="subject" element="div" class="error"/> 						  	
			</label>
			<label>
				<form:textarea path="message" id="field-message" placeholder="Please enter your message..." maxlength="255" style="width:600px;height:200px"></form:textarea>
	    		<form:errors path="message" element="div" class="error"/>
			</label>
		
			<label>
				<input type="submit" value="Send!" />
			</label>
		</form:form>
<!-- END NEW MESSAGE FORM -->
</div>

<%@include file="../includes/footer.jsp"%>