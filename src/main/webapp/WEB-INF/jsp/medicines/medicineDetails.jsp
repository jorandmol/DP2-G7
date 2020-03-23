<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="medicines">

    <h2>Medicine Information</h2>
	<h3><b><c:out value="${medicine.name}"/></b></h3>

    <table class="table table-striped">
        <tr>
            <th>Code</th>
            <td><c:out value="${medicine.code}"/></td>
        </tr>
        <tr>
            <th>Expiration Date</th>
            <td><c:out value="${medicine.expirationDate}"/></td>
        </tr>
        <tr>
            <th>Description</th>
            <td><c:out value="${medicine.description}"/></td>
        </tr>
      
    </table>
	<div>
		<a class="btn btn-default" href='<spring:url value="/medicines" htmlEscape="true"/>'>Return</a>
	</div>


</petclinic:layout>
