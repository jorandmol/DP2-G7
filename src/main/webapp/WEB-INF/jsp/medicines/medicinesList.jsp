<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>  

<petclinic:layout pageName="medicines">
    <h2>Medicines</h2>

    <table id="medicinesTable" class="table table-striped">
        <thead>
        <tr>
        	<th>Code</th>
        	<th>Name</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${medicines}" var="medicine">
            <tr id="med">
                <td>
                   <p><c:out value="${medicine.code}"/></p>
                </td>
                <td>
                   <p><c:out value="${medicine.name}"/></p>
                </td>
                <td>
                	<a href='<spring:url value="medicines/${medicine.id}" htmlEscape="true"/>'>See details</a>
                </td>              
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div>
    	<sec:authorize access="hasAuthority('admin')">
			<a class="btn btn-default" href='<spring:url value="medicines/new" htmlEscape="true"/>'>New medicine</a>
		</sec:authorize>
    </div>
</petclinic:layout>
