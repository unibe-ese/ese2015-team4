<%@include file="../includes/header.jsp"%>
	
	<div>
		<a href="<%=request.getContextPath()%>/user/message?view=inbox" class="button">Inbox</a>
		<a href="<%=request.getContextPath()%>/user/message?view=outbox" class="button">Outbox</a>
		<a href="<%=request.getContextPath()%>/user/message?view=trash" class="button">Trash bin</a>
	</div>
	
	<div>
		<h1>Message:</h1>
		<c:if test="${ userId == message.receiver.id && !message.receiverDeleted }">	
			<div>
				<a href="<%=request.getContextPath()%>/user/message/new?receiverId=<c:out value="${message.sender.id}" />" class="button">Reply</a>
			</div>
		</c:if>
		<c:if test="${ ( userId == message.receiver.id && !message.receiverDeleted ) || ( userId == message.sender.id && !message.senderDeleted ) }">
			<form name="frmProfileActions" method="POST">
				<input type="hidden" name="action" id="action" value="delete" />
				<input type="hidden" name="objectId" id="objectId" value="<c:out value="${message.id}"/>" />
			</form>
			<div class="cell icon"><img class="action-icon" src="<%=request.getContextPath()%>/img/delete.png" onClick="frmProfileActions.submit();" /></div>
		</c:if>
		
		
	</div>
	
	<div class="hashMap table" style="max-width:560px;overflow:hidden">
		<div class="row">
			<div class="cell key">From</div>
			<div class="cell value"><c:out value="${message.sender.firstName}" /> <c:out value="${message.sender.lastName}" /></div>
		</div>
		<div class="row">
			<div class="cell key">To</div>
			<div class="cell value"><c:out value="${message.receiver.firstName}" /> <c:out value="${message.receiver.lastName}" /></div>
		</div>
		<div class="row">
			<div class="cell key">Date</div>
			<div class="cell value"><fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${message.dateAndTime}" /></div>
		</div>
		<div class="row">
			<div class="cell key">Subject</div>
			<div class="cell value"><c:out value="${message.subject}" /></div>
		</div>
		<div class="row">
			<div class="cell key multiline" style="padding-top:15px">Text</div>
		    <div class="cell value multiline"><c:out value="${message.message}" /></div>
		</div>
	</div>	
	
<%@include file="../includes/footer.jsp"%>	