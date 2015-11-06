<%@include file="../includes/header.jsp"%>
<form name="frmHiddenActions" method="POST">
	<input type="hidden" name="action" id="action" value="show" />
	<input type="hidden" name="objectId" id="objectId" value="0" />
</form>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/hidden-actions.js"></script>
<c:choose>
<c:when test="${empty param.view}">
	<c:set var="view" value="inbox"/>
</c:when>
<c:otherwise>
	<c:set var="view" value="${param.view}"/>
</c:otherwise>
</c:choose>
<c:choose>
<c:when test="${empty param.show}">
	<c:set var="show" value="0"/>
</c:when>
<c:otherwise>
	<c:set var="show" value="${param.show}"/>
</c:otherwise>
</c:choose>
<h1>Message center</h1>
<div id="message-center">
	<div class="messages">
		<select name="view" class="view" onChange="document.location.href='?view='+this.options[this.selectedIndex].value;">
			<option value="inbox">Inbox</option>
			<option value="outbox"<c:if test="${view=='outbox'}"> selected</c:if>>Outbox</option>
			<option value="trash"<c:if test="${view=='trash'}"> selected</c:if>>Trash</option>
		</select>
		<c:choose>
			<c:when test="${messageList==null}">
		<center style="margin-top:10px"><i>No messages found.</i></center>
			
			</c:when>
			<c:otherwise>
		<div id="messages">
			<c:forEach begin="0" items="${messageList}" var="message" varStatus="i">
				<div class="el<c:if test="${i.index==show}"> active</c:if>" onClick="document.location.href='?view=<c:out value="${view}"/>&show=<c:out value="${i.index}"/>'">
				<c:if test="${view=='inbox' || view=='trash'}">
					<strong>From:</strong> <c:out value="${message.sender.firstName}"/> <c:out value="${message.sender.lastName}"/><br>
				</c:if>
				<c:if test="${view=='outbox' || view=='trash'}">
					<strong>To:</strong> <c:out value="${message.receiver.firstName}"/> <c:out value="${message.receiver.lastName}"/><br>
				</c:if>
				<strong><c:out value="${message.subject}"/></strong><br>
				<i><fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${message.dateAndTime}" /></i>
				</div>
			</c:forEach>
		</div>
		</c:otherwise>
		</c:choose>
	</div>
	<c:if test="${messageList!=null && !empty messageList[show]}">
		<c:set var="message" value="${messageList[show]}"/>
		<div style="margin-left:60px;width:600px;height:500px;float:left;position:relative;overflow:hidden">
		<div id="message" style="position:absolute;top:0px;left:0px;width:600px;height:500px;overflow:auto">
			<div style="border-bottom:1px solid rgb(212, 215, 228);margin-bottom:25px">
			<div class="table mailHeader">
				<div class="row">
					<div class="cell key">From:</div><div class="cell value"><c:out value="${message.sender.firstName}"/> <c:out value="${message.sender.lastName}"/></div>
				</div>
				<div class="row">
					<div class="cell key">To:</div><div class="cell value"><c:out value="${message.receiver.firstName}"/> <c:out value="${message.receiver.lastName}"/></div>
				</div>
				<div class="row">
					<div class="cell key">Subject:</div><div class="cell value"><strong><c:out value="${message.subject}"/></strong></div>
				</div>
				<div class="row">
					<div class="cell key">Date/Time:</div><div class="cell value"><fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${message.dateAndTime}" /></div>
				</div>
			</div>
			</div>
			<div style="white-space:pre-wrap;max-width:600px"><c:out value="${message.message}"/></div>
		</div>
		<c:if test="${view!='trash'}">
			<a href="<%=request.getContextPath() %>/user/messagecenter/reply/?replyToMessageId=<c:out value="${message.id}"/>"><img class="action-icon" style="position:absolute;top:5px;right:34px" src="<%=request.getContextPath()%>/img/reply.png" /></a>
			<img class="action-icon" style="position:absolute;top:5px;right:5px" src="<%=request.getContextPath()%>/img/delete.png" onClick="hiddenAction('delete',<c:out value="${message.id}"/>);" />
		</c:if>
		</div>
	</c:if>
	<div class="clear"></div>
</div>
<%@include file="../includes/footer.jsp"%>