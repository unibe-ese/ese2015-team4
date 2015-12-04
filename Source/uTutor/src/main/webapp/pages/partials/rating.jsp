<!-- BEGIN RATING -->
<c:choose>
	<c:when test="${rating == null}">
		<i>Not rated yet.</i>
	</c:when>
	<c:otherwise>
		<div class="star-container">
			<c:forEach var="i" begin="1" end="5">
				<div class="star clickable<c:if test="${i > rating}"> empty</c:if>"></div>
			</c:forEach>
		</div>
	</c:otherwise>
</c:choose>
<!-- END RATING -->