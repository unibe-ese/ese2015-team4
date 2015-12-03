<%@include file="partials/header.jsp"%>
<h1>Searched for 
<c:choose>
	<c:when test="${query!=''}">
		&quot;<c:out value="${query}" />&quot;
	</c:when>
	<c:otherwise>
		all lectures
	</c:otherwise>
</c:choose></h1>

<c:if test="${exception_message == null}">
	<form method="get" name="searchsort">
	<input type="hidden" id="query" name="query" value="<c:out value="${query}"/>"/>
	<input type="hidden" id="sort" name="sort" value="<c:out value="${sort}"/>"/>
	</form>
	<p>
		Sort by 
		<select onChange="document.getElementById('sort').value=this.options[this.selectedIndex].value;document.searchsort.submit();">
			<option value="lecture.name"<c:if test="${sort == 'lecture.name'}"> selected</c:if>>lecture name</option>
			<option value="tutor.rating"<c:if test="${sort == 'tutor.rating'}"> selected</c:if>>tutor rating</option>
			<option value="grade"<c:if test="${sort == 'grade'}"> selected</c:if>>tutor grade</option>
			<option value="tutor.price"<c:if test="${sort == 'tutor.price'}"> selected</c:if>>tutor price</option>
		</select>
	</p>
</c:if>

<%@include file="partials/exception.jsp"%>

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
	    		<%@include file="partials/rating.jsp"%>
			</div>
		</div>
	</c:forEach>
</div>
<%@include file="partials/footer.jsp"%>