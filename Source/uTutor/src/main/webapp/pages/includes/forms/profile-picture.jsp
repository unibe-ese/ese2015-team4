<div class="center">
<form name="frmPicture" class="form-box" enctype="multipart/form-data" method="POST">
<input type="hidden" name="action" id="action" value="upload">
<h1>Profile picture</h1>
<c:if test="${exception_message != null}">
	<div class="exception">
		<c:out value="${exception_message}" />
	</div>
</c:if>
<img src="<%=request.getContextPath()%>/img/user.jpg?userId=<c:out value="${userId}"/>" class="profile-picture"/>
<label><input type="file" name="picture" accept="image/jpeg,image/jpg,.jpg" onChange="document.frmPicture.submit();"></label>
<c:if test="${hasProfilePic}"><label><input class="red submit" type="button" value="Delete" onClick="document.getElementById('action').value='delete';document.frmPicture.submit();"></label></c:if>
<label><input class="grey submit" type="button" value="Cancel" onClick="document.location.href='<%=request.getContextPath()%>/user/profile';"></label>
</form>
</div>