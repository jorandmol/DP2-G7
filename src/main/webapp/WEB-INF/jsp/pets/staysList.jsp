<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>  

<petclinic:layout pageName="stays">
	<b>Pet</b>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>Name</th>
            <th>Birth Date</th>
            <th>Type</th>
            <th>Owner</th>
        </tr>
        </thead>
        <tr>
            <td><c:out value="${pet.name}"/></td>
            <td><petclinic:localDate date="${pet.birthDate}" pattern="yyyy/MM/dd"/></td>
            <td><c:out value="${pet.type.name}"/></td>
            <td><c:out value="${pet.owner.firstName} ${pet.owner.lastName}"/></td>
        </tr>
    </table>


    <h2>Stays</h2>
    <table id="medicinesTable" class="table table-striped">
        <thead>
        <tr>
        	<th>Register date</th>
        	<th>Release date</th>
        	<th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${stays}" var="stay">
            <tr>
                <td>
                   <c:out value="${stay.registerDate}"/>
                </td>
                <td>
                   <c:out value="${stay.releaseDate}"/>
                </td>
                <td><spring:url value="/owners/{ownerId}/pets/{petId}/stays/{stayId}/delete" var="deleteUrl">
                    <spring:param name="ownerId" value="${stay.pet.owner.id}"/>
                    <spring:param name="petId" value="${stay.pet.id}"/>
                    <spring:param name="stayId" value="${stay.id}"/>
                </spring:url>
                <a href="${fn:escapeXml(deleteUrl)}">Delete</a></td>      
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div>
		<a class="btn btn-default" href='<spring:url value="/owners/${pet.owner.id}/pets/${pet.id}/stays/new" htmlEscape="true"/>'>New stay</a>
    </div>
</petclinic:layout>