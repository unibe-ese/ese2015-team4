<%@include file="includes/header.jsp"%>
<h1>User Profile</h1>
<div>
<table><tr>
			<td>First Name:</td>
			<td><c:out value="${user.firstName}" /></td>
		</tr>
		<tr>
			<td>Last Name:</td>
			<td><c:out value="${user.lastName}" /></td>
		</tr>
		<tr>
			<td>E-Mail:</td>
			<td><c:out value="${user.username}" /></td>
		</tr>
</table>
<a class="button" href="/ututor/user/profile/edit">Edit</a>
</div>
<%@include file="includes/footer.jsp"%>