<%@include file="includes/header.jsp"%>

<!-- TEMP RATING VALUE -->
<c:set var="rating" scope="page" value="${4}"/>

<h1>Searched for &quot;<c:out value="${query}" />&quot;</h1>

<%@include file="includes/exception.jsp"%>

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
				<c:set var="rating" value="${result.tutor.rating}" />
	    		<%@include file="includes/rating.jsp"%>
			</div>
		</div>
	</c:forEach>
</div>
<%@include file="includes/footer.jsp"%>