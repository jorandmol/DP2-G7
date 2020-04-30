<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>  
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="stays">

    <h2>Stays</h2>
    <div>
    <span class="error-text"> <c:out value="${errors}"></c:out> </span>
    </div>
    <table id="stayTables" class="table table-striped">
        <thead>
        <tr>
        	<th>Register date</th>
        	<th>Release date</th>
        	<th>Status</th>
        <sec:authorize access="hasAuthority('admin')"> 	
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
                 <td>
                   <c:out value="${stay.status}"/>
                </td>
                
                <sec:authorize access="hasAuthority('admin')"> 
                <td>
                    <spring:url value="/admin/stays/{stayId}" var="changeUrl">
                    <spring:param name="stayId" value="${stay.id}"/>
                    
                    
                </spring:url>
                
                <p>
                <c:if test="${stay.status == 'PENDING'}">
                <a href="${fn:escapeXml(changeUrl)}">Change Status</a> 
                </c:if>
                </p>
               </td>    
               </sec:authorize>    
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
