<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="petTypes">
	<h2>Pet types</h2>

	<table id="petTypeTable" class="table table-striped">
		<thead>
			<tr>
				<th style="width: 150px;">Name</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${petTypes}" var="type">
				<tr>
					<td><spring:url value="/pet-type/{petTypeId}/edit" var="editUrl">
							<spring:param name="petTypeId" value="${type.id}" />
						</spring:url> <a href="${fn:escapeXml(editUrl)}"> <c:out
								value="${type.name}" /></a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<form action="/pet-type/new">
		<button type="submit" class="btn btn-default">Add a new pet
			type</button>
	</form>
</petclinic:layout>
