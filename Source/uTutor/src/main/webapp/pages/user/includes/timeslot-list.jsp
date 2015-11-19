				<c:set var="timeSlotHeaderShowed" value="${false}"/>
				<c:set var="billableHours" value="0"/>
				<c:if test="${timeSlotListSettingStatus == 'AVAILABLE'}">
					<%@include file="timeslot-list-header.jsp"%>
					
				</c:if>
				<c:forEach items="${timeSlotList}" var="timeSlot">
					<c:if test="${(timeSlot.status == timeSlotListSettingStatus) && ((timeSlotListSettingFuture && timeSlot.beginDateTime > now) || (!timeSlotListSettingFuture && timeSlot.beginDateTime < now))}">
						<c:if test="${!timeSlotHeaderShowed}">
							<%@include file="timeslot-list-header.jsp"%>
						</c:if>
						
						<div class="row">
							<c:if test="${timeSlotListSettingStatus != 'AVAILABLE'}">
								<div class="cell">
									<c:choose>
										<c:when test="${ownProfile && timeSlot.student.id == user.id}">
											You
										</c:when>
										<c:otherwise>										
											<a href="<%=request.getContextPath()%>/user/profile/?userId=${timeSlot.student.id}">
												<div style="width:32px;height:32px;background-image:url('<%=request.getContextPath()%>/img/user.jpg?userId=${timeSlot.student.id}');background-size:cover;background-position:top center;float:left;margin-right:10px"></div>
												<div style="float:left;margin-top:4px"><c:out value="${timeSlot.student.firstName} ${timeSlot.student.lastName}" /></div>
											</a>
											<div class="clear"></div>
										</c:otherwise>
									</c:choose>
								</div>
								<div class="cell">
									<c:choose>
										<c:when test="${ownProfile && timeSlot.tutor.id == user.id}">
											You
										</c:when>
										<c:otherwise>										
											<a href="<%=request.getContextPath()%>/user/profile/?userId=${timeSlot.tutor.id}">
												<div style="width:32px;height:32px;background-image:url('<%=request.getContextPath()%>/img/user.jpg?userId=${timeSlot.tutor.id}');background-size:cover;background-position:top center;float:left;margin-right:10px"></div>
												<div style="float:left;margin-top:4px"><c:out value="${timeSlot.tutor.firstName} ${timeSlot.tutor.lastName}" /></div>
											</a>
											<div class="clear"></div>
										</c:otherwise>
									</c:choose>
								</div>
							</c:if>
							<div class="cell">
								<fmt:formatDate value="${timeSlot.beginDateTime}" pattern="dd.MM.yyyy" />
							</div>
							<div class="cell">
								<fmt:formatDate value="${timeSlot.beginDateTime}" pattern="HH" />:00 - <fmt:formatDate value="${timeSlot.beginDateTime}" pattern="HH" />:59
							</div>
							<div class="cell" style="text-align:right">
							<c:choose>
								<c:when test="${ownProfile}">
									<c:if test="${timeSlotListSettingStatus == 'AVAILABLE'}">
					    					<img class="action-icon" src="<%=request.getContextPath()%>/img/delete.png" onClick="hiddenAction('deleteTimeSlot',<c:out value="${timeSlot.id}"/>);" />
									</c:if>
									<c:if test="${timeSlotListSettingStatus == 'REQUESTED' && user.id == timeSlot.tutor.id}">
					    					<a class="button action green" href="javascript:void(0);" onClick="hiddenAction('acceptTimeSlot',<c:out value="${timeSlot.id}"/>);">
												accept
											</a> 
											<a class="button action red" href="javascript:void(0);" onClick="hiddenAction('rejectTimeSlot',<c:out value="${timeSlot.id}"/>);">
												reject
											</a>
									</c:if>
									<c:if test="${ownProfile && user.isTutor && user.id == timeSlot.tutor.id && timeSlotListSettingStatus == 'ACCEPTED' && timeSlot.beginDateTime < now}">
										CHF <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${user.price}" />
										<c:set var="billableHours" value="${billableHours+1}"/>
									</c:if>
								</c:when>
								<c:otherwise>
									<c:if test="${timeSlotListSettingStatus == 'AVAILABLE'}">
					    					<a class="button action" href="javascript:void(0);" onClick="hiddenAction('requestTimeSlot',<c:out value="${timeSlot.id}"/>);">
												send request
											</a> 
									</c:if>
								</c:otherwise>
								</c:choose>
							</div>
						</div>
					</c:if>
				</c:forEach>
			<c:if test="${timeSlotHeaderShowed}">
				<c:if test="${billableHours > 0}">
					<div class="row">
						<div class="cell">
							<strong>Total</strong>
						</div>
						<div class="cell"></div>
						<div class="cell"></div>
						<div class="cell"></div>
						<div class="cell" style="text-align:right">
							<strong>CHF <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${user.price * billableHours}" /></strong>
						</div>
					</div>
					<div class="row">
						<div class="cell">
							<strong>Fee to pay (10%)</strong> 
						</div>
						<div class="cell"></div>
						<div class="cell"></div>
						<div class="cell"></div>
						<div class="cell" style="text-align:right">
							<strong>CHF <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${user.price * billableHours * 0.1}" /></strong>
						</div>
					</div>
				</c:if>
				</div>
			</c:if>
			<c:if test="${ownProfile && timeSlotHeaderShowed && timeSlotListSettingStatus == 'AVAILABLE'}">
				<a class="button action" href="<%=request.getContextPath()%>/user/add-timeslots">
					+ add time-slots
				</a>
			</c:if>