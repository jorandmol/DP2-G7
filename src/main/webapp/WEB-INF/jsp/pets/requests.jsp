<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="requests">
    <h2>Pending pet requests</h2>

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
       	<c:forEach items="${pets}" var="pet">
            <tr id= "pet">
                <td>
                    <c:out value="${pet.owner.firstName} ${pet.owner.lastName}"/>
                </td>
                 <td>
                    <spring:url value="/owners/{ownerId}/pet/{petId}" var="petRequestUrl">
                        <spring:param name="petId" value="${pet.id}"/>                       
                        <spring:param name="ownerId" value="${pet.owner.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(petRequestUrl)}"> <strong><c:out value="${pet.name}"/></strong></a>
                </td>
                <td>
                    <c:out value="${pet.birthDate}"/>
                </td>
                <td>
                    <c:out value="${pet.type}"/>
                </td>           
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
