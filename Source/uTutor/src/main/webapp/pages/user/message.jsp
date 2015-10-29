<%@include file="../includes/header.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
	<div>
		<a href="<%=request.getContextPath()%>/user/message?view=inbox" class="button<c:if test="${view == 'inbox'}"> active</c:if>">Inbox</a>
		<a href="<%=request.getContextPath()%>/user/message?view=outbox" class="button<c:if test="${view == 'outbox'}"> active</c:if>">Outbox</a>
		<a href="<%=request.getContextPath()%>/user/message?view=trash" class="button<c:if test="${view == 'trash'}"> active</c:if>">Trash bin</a>
	</div>
	
	<c:choose>
		<c:when test="${view == 'inbox'}">
			<h1>Your inbox:</h1>
		</c:when>
		<c:when test="${view == 'outbox'}">
			<h1>Your outbox:</h1>
		</c:when>
		<c:when test="${view == 'trash'}">
			<h1>Your trash bin:</h1>
		</c:when>
		<c:otherwise>
			<h1>Sorry, something went wrong...</h1>
		</c:otherwise>
	</c:choose>
	
	<c:choose>	
		<c:when test="${fn:length(results) == 0}">
			<h2>No e-mails found!</h2>
		</c:when>
		
		<c:otherwise>
			
			<form name="frmProfileActions" method="POST">
				<input type="hidden" name="action" id="action" value="viewInbox" />
				<input type="hidden" name="objectId" id="objectId" value="0" />
			</form>
			<script type="text/javascript" src="<%=request.getContextPath()%>/js/profile.js"></script>
			<c:choose>
				<c:when test="${view == 'inbox'}">	
					<div class="list table">
						<div class="row header">
							<div class="cell">From</div>
							<div class="cell">Date</div>
							<div class="cell">Subject</div>
						</div>
						<c:forEach items="${results}" var="result">
							<div class="row <c:if test="${!result.isRead}">unread</c:if>">
								<div class="cell"><c:out value="${result.sender.firstName}" /> <c:out value="${result.sender.lastName}" /></div>
			    				<div class="cell"><fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${result.dateAndTime}" /></div>
								<div class="cell"><c:out value="${result.subject}" /></div>
								<div class="cell icon"><img class="action-icon" src="<%=request.getContextPath()%>/img/search-big.png" onClick="profileAction('view',<c:out value="${result.id}"/>);" /></div>
								<div class="cell icon"><img class="action-icon" src="<%=request.getContextPath()%>/img/delete.png" onClick="profileAction('delete',<c:out value="${result.id}"/>);" /></div>
							</div>
						</c:forEach>
					</div>
				</c:when>
			
				<c:when test="${view == 'outbox'}">	
					<div class="list table">
						<div class="row header">
							<div class="cell">To</div>
							<div class="cell">Date</div>
							<div class="cell">Subject</div>
						</div>
						<c:forEach items="${results}" var="result">
							<div class="row">
								<div class="cell"><c:out value="${result.receiver.firstName}" /> <c:out value="${result.receiver.lastName}" /></div>
		    					<div class="cell"><fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${result.dateAndTime}" /></div>
								<div class="cell"><c:out value="${result.subject}" /></div>
								<div class="cell icon"><img class="action-icon" src="<%=request.getContextPath()%>/img/search-big.png" onClick="profileAction('view',<c:out value="${result.id}"/>);" /></div>
								<div class="cell icon"><img class="action-icon" src="<%=request.getContextPath()%>/img/delete.png" onClick="profileAction('delete',<c:out value="${result.id}"/>);" /></div>
							</div>
						</c:forEach>
					</div>
				</c:when>
				
				<c:when test="${view == 'trash'}">	
					<div class="list table">
						<div class="row header">
							<div class="cell">From</div>
							<div class="cell">To</div>
							<div class="cell">Date</div>
							<div class="cell">Subject</div>
						</div>
						<c:forEach items="${results}" var="result">
							<div class="row <c:if test="${!result.isRead}">unread</c:if>">
								<div class="cell"><c:out value="${result.sender.firstName}" /> <c:out value="${result.sender.lastName}" /></div>
			    				<div class="cell"><c:out value="${result.receiver.firstName}" /> <c:out value="${result.receiver.lastName}" /></div>
			    				<div class="cell"><fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${result.dateAndTime}" /></div>
								<div class="cell"><c:out value="${result.subject}" /></div>
								<div class="cell icon"><img class="action-icon" src="<%=request.getContextPath()%>/img/search-big.png" onClick="profileAction('view',<c:out value="${result.id}"/>);" /></div>
							</div>
						</c:forEach>
					</div>
				</c:when>
			</c:choose>
		</c:otherwise>
	</c:choose>
	
	
	
<%@include file="../includes/footer.jsp"%>