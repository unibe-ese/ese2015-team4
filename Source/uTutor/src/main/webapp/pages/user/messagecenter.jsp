<%@include file="../includes/header.jsp"%>

<%@include file="includes/hidden-actions.jsp"%>

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
		<c:set var="show" value="-1"/>
	</c:when>
	<c:otherwise>
		<c:set var="show" value="${param.show}"/>
	</c:otherwise>
</c:choose>

<h1>Message center</h1>
<div id="message-center">
	<div class="messages">
<!-- BEGIN VIEW SELECT -->
		<select name="view" class="view" onChange="document.location.href='?view='+this.options[this.selectedIndex].value;">
			<option value="inbox">Inbox</option>
			<option value="outbox"<c:if test="${view=='outbox'}"> selected</c:if>>Outbox</option>
			<option value="trash"<c:if test="${view=='trash'}"> selected</c:if>>Trash</option>
		</select>
<!-- END VIEW SELECT -->
<!-- BEGINN MESSAGE LIST -->
		<c:choose>
			<c:when test="${empty messageList}">
				<div class="no-messages">No messages found.</div>
			</c:when>
			<c:otherwise>
				<div id="messages">
					<c:forEach begin="0" items="${messageList}" var="message" varStatus="i">
						<div class="el<c:if test="${i.index==show}"> active</c:if>" onClick="document.location.href='?view=<c:out value="${view}"/>&show=<c:out value="${i.index}"/>&messageId=<c:out value="${message.id}"/>'">
							<c:if test="${view=='inbox' || view=='trash'}">
								<strong>From:</strong> <c:out value="${message.sender.firstName}"/> <c:out value="${message.sender.lastName}"/><br>
							</c:if>
							<c:if test="${view=='outbox' || view=='trash'}">
								<strong>To:</strong> <c:out value="${message.receiver.firstName}"/> <c:out value="${message.receiver.lastName}"/><br>
							</c:if>
							<strong><c:out value="${message.subject}"/></strong><br>
							<i><fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${message.dateAndTime}" /></i>
							<c:if test="${message.isRead}">
								<img class="action-icon" style="position:absolute;right:25px;top:5px" src="<%=request.getContextPath()%>/img/readed.png"/>
							</c:if>
						</div>
					</c:forEach>
				</div>
			</c:otherwise>
		</c:choose>
<!-- END MESSAGE LIST -->
	</div>
	<c:if test="${messageList!=null && !empty messageList[show]}">
<!-- BEGIN MESSAGE VIEW -->
		<c:set var="message" value="${messageList[show]}"/>
		<div id="message-container">
			<div id="message">
				<div id="message-inner">
	<!-- BEGIN MESSAGE HEADER -->
					<div class="table mailHeader">
						<div class="row">
							<div class="cell key">
								From:
							</div>
							<div class="cell value">
								<a href="<%=request.getContextPath()%>/user/profile/?userId=<c:out value="${message.sender.id}"/>">
									<c:out value="${message.sender.firstName}"/> <c:out value="${message.sender.lastName}"/>
								</a>
							</div>
						</div>
						<div class="row">
							<div class="cell key">
								To:
							</div>
							<div class="cell value">
								<a href="<%=request.getContextPath()%>/user/profile/?userId=<c:out value="${message.receiver.id}"/>">
									<c:out value="${message.receiver.firstName}"/> <c:out value="${message.receiver.lastName}"/>
								</a>
							</div>
						</div>
						<div class="row">
							<div class="cell key">
								Subject:
							</div>
							<div class="cell value">
								<strong>
									<c:out value="${message.subject}"/>
								</strong>
							</div>
						</div>
						<div class="row">
							<div class="cell key">
								Date/Time:
							</div>
							<div class="cell value">
								<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${message.dateAndTime}" />
							</div>
						</div>
					</div>
	<!-- END MESSAGE HEADER -->
				</div>
	<!-- BEGIN MESSAGE (don't add white-spaces inside the div!!!) -->
				<div class="message-txt pre"><c:out value="${message.message}"/></div>
	<!-- END MESSAGE -->
			</div>
			<div id="message-actions">
	<!-- BEGIN MESSAGE ACTIONS -->
				<a href="<%=request.getContextPath() %>/user/messagecenter/reply/?replyToMessageId=<c:out value="${message.id}"/>">
					<img class="action-icon" style="margin-left:5px" src="<%=request.getContextPath()%>/img/reply.png" />
				</a>
				<c:if test="${view!='trash'}">
					<img class="action-icon" style="margin-left:5px" src="<%=request.getContextPath()%>/img/delete.png" onClick="hiddenAction('delete',<c:out value="${message.id}"/>);" />
				</c:if>
	<!-- END MESSAGE ACTIONS -->
			</div>
		</div>
<!-- END MESSAGE VIEW -->
	</c:if>
	<div class="clear"></div>
</div>
<%@include file="../includes/footer.jsp"%>