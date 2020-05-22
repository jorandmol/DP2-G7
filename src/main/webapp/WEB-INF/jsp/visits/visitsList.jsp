<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="visits">
	
	<h2>Pet information</h2>
	
    <table id="petsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Owner</th>
            <th>Name</th>
            <th>Birth date</th>
            <th>Pet type</th>
        </tr>
        </thead>
        <tbody>
            <tr id= "pet">
                <td style="width: 300px">
                    <c:out value="${pet.owner.firstName} ${pet.owner.lastName}"/>
                </td>
                <td style="width: 250px">
                    <strong><c:out value="${pet.name}"/></strong>
                </td>
                <td>
                    <c:out value="${pet.birthDate}"/>
                </td>
                <td>
                    <c:out value="${pet.type}"/>
                </td>           
            </tr>
        </tbody>
    </table>
    <br/>


    <h2>Visits</h2>

	<c:choose>
    <c:when test="${!pet.visits.isEmpty()}">
    <table id="visitsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Medical tests</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${pet.visits}" var="visit">
            <tr>
                <td style="width: 150px">
                    <c:out value="${visit.date}"/>
                </td>
                <td style="width: 500px">
                	<c:out value="${visit.description}"></c:out>
                </td>
                <td style="width: 300px">
                	<c:choose>
                	<c:when test="${!visit.medicalTests.isEmpty()}">
                		<ul>
	            			<c:forEach items="${visit.medicalTests}" var="medicalTest">
	            				<li><c:out value="${medicalTest.name}"/></li>
	            			</c:forEach>
	            		</ul>
	            	</c:when>
	            	<c:otherwise><c:out value="no tests were performed"></c:out></c:otherwise>
	            	</c:choose>
	            </td>
                <td>
                	<c:if test="${editableVisitsIds.contains(visit.id)}">
                		<spring:url value="/vets/pets/{petId}/visits/{visitId}" var="editVisitUrl">
                       		<spring:param name="petId" value="${pet.id}"></spring:param>
                        	<spring:param name="visitId" value="${visit.id}"></spring:param>
                    	</spring:url>
                    	<a href="${fn:escapeXml(editVisitUrl)}"><c:out value="Edit"></c:out></a>
                    </c:if>
                </td>  
            </tr>
        </c:forEach>
        </tbody>
    </table>
    </c:when>
    <c:otherwise><c:out value="This pet has no visits yet"></c:out></c:otherwise>
    </c:choose>
</petclinic:layout>
