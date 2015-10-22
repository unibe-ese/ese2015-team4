<%@include file="includes/header.jsp"%>
<c:set var="rating" scope="page" value="${4}"/>
<h1>Searched for &quot;<c:out value="${query}" />&quot;</h1>
<c:if test="${exception_message!=null}">
	<div class="exception"><c:out value="${exception_message}"/></div>
</c:if>
<div class="search-result">
<c:forEach items="${results}" var="result">
		<div class="item" onClick="document.location.href='<%=request.getContextPath()%>/user/profile/?userId=<c:out value="${result.tutor.id}"/>';">
			<div class="cell picture"><img src="<%=request.getContextPath()%>/img/user.jpg?userId=<c:out value="${result.tutor.id}"/>"/></div>
			<div class="cell data">
				<div class="line">
					<div class="lecture"><c:out value="${result.lecture.name}"/></div>
					<div class="grade"><c:out value="${result.grade}"/></div>
					<div class="clear"></div>
				</div>
				<div class="line">
					<div class="tutor"><c:out value="${result.tutor.firstName} ${result.tutor.lastName}"/></div>
					<div class="price">CHF <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${result.tutor.price}" />/h</div>
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
</c:forEach>
	</div>
<%@include file="includes/footer.jsp"%>