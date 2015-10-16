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
			<td><c:out value="${user.email}" /></td>
		</tr>
</table>
<button id="edit" onclick="document.location.href='/ututor/edit?userId=${user.id}'">Edit</button>
</div>
<%@include file="includes/footer.jsp"%>