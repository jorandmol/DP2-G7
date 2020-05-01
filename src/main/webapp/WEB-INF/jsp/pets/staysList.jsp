<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>  
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

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
    <div>
    <span class="error-text"> <c:out value="${errors}"></c:out> </span>
    </div>
    <table id="medicinesTable" class="table table-striped">
        <thead>
        <tr>
        	<th>Register date</th>
        	<th>Release date</th>
        <sec:authorize access="hasAuthority('owner')"> 	
        	<th>Actions</th>
        </sec:authorize>   	
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
                <sec:authorize access="hasAuthority('owner')"> 
                <td>
                    <spring:url value="/owners/{ownerId}/pets/{petId}/stays/{stayId}/delete" var="deleteUrl">
                    <spring:param name="ownerId" value="${stay.pet.owner.id}"/>
                    <spring:param name="petId" value="${stay.pet.id}"/>
                    <spring:param name="stayId" value="${stay.id}"/>
                </spring:url>
                    <spring:url value="/owners/{ownerId}/pets/{petId}/stays/{stayId}/edit" var="updateUrl">
                    <spring:param name="ownerId" value="${stay.pet.owner.id}"/>
                    <spring:param name="petId" value="${stay.pet.id}"/>
                    <spring:param name="stayId" value="${stay.id}"/>
                </spring:url>
                <p>
                <a href="${fn:escapeXml(updateUrl)}">Update</a> /
                <a href="${fn:escapeXml(deleteUrl)}">Delete</a>
                </p>
               </td>    
               </sec:authorize>    
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div>
    	<c:if test="${pet.active}">
      		<sec:authorize access="hasAuthority('owner')">   
				<a class="btn btn-default" href='<spring:url value="/owners/${pet.owner.id}/pets/${pet.id}/stays/new" htmlEscape="true"/>'>New stay</a>
     		</sec:authorize>  		
     	</c:if>
    </div>
</petclinic:layout>
