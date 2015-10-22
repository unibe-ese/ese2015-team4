<%@include file="includes/header.jsp"%>
<c:set var="rating" scope="page" value="${2}"/>
<h1>Searched for &quot;<c:out value="${query}" />&quot;</h1>
<div class="search-result">
<%--<c:forEach items="${results}" var="result">--%>
		<div class="item">
			<div class="cell picture"><img src="<%=request.getContextPath()%>/img/default_avatar.jpg"/></div>
			<div class="cell data">
				<div class="line">
					<div class="lecture"><%-- ${result.lecture.name} --%>Einführung in Software Engineering</div>
					<div class="grade"><%-- ${result.grade} --%>5.0</div>
					<div class="clear"></div>
				</div>
				<div class="line">
					<div class="tutor"><%-- ${result.tutor.name} --%>Fritz Muster</div>
					<div class="price"><%-- ${result.tutor.price} --%>CHF 29.50/h</div>
					<div class="clear"></div>
				</div>
				<c:forEach var="i" begin="1" end="5">
					<c:if test="${i <= rating}">
						<img src="<%=request.getContextPath()%>/img/star_full.png"/>
					</c:if>
					<c:if test="${i > rating}">
						<img src="<%=request.getContextPath()%>/img/star_empty.png"/>
					</c:if>
				</c:forEach>
			</div>
		</div>
		<div class="item">
			<div class="cell picture"><img src="<%=request.getContextPath()%>/img/default_avatar.jpg"/></div>
			<div class="cell data">
				<div class="line">
					<div class="lecture"><%-- ${result.lecture.name} --%>Einführung in Software Engineering</div>
					<div class="grade"><%-- ${result.grade} --%>5.0</div>
					<div class="clear"></div>
				</div>
				<div class="line">
					<div class="tutor"><%-- ${result.tutor.name} --%>Fritz Muster</div>
					<div class="price"><%-- ${result.tutor.price} --%>CHF 29.50/h</div>
					<div class="clear"></div>
				</div>
				<c:forEach var="i" begin="1" end="5">
					<c:if test="${i <= rating}">
						<img src="<%=request.getContextPath()%>/img/star_full.png"/>
					</c:if>
					<c:if test="${i > rating}">
						<img src="<%=request.getContextPath()%>/img/star_empty.png"/>
					</c:if>
				</c:forEach>
			</div>
		</div>
<%--</c:forEach>--%>
	</div>
<%@include file="includes/footer.jsp"%>