<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="adoptions">
	<jsp:body>
		<h2>Results</h2>
		<p><c:out value="${notFound}"></c:out></p>
		<p><c:out value="${res}"></c:out></p>
	</jsp:body>
</petclinic:layout>