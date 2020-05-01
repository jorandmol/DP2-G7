<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="visits">

    <h2>Visit Information</h2>


    <table class="table table-striped">
        <tr>
            <th>Date</th>
            <td><b><c:out value="${visit.date}"/></b></td>
        </tr>
        <tr>
            <th>Pet</th>
            <td><c:out value="${visit.pet.name}"/></td>
        </tr>
        <tr>
            <th>Description</th>
            <td><c:out value="${visit.description}"/></td>
        </tr>
    </table>

    <br/>
    <br/>
    
    <c:if test="${!visit.medicalTests.isEmpty()}">
    <h2>Medical tests</h2>
	
	<table class="table table-striped">
        <thead>
        <tr>
            <th>Name</th>
            <th>Description</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${visit.medicalTests}" var="medicalTest">
            <tr>
               <td><c:out value="${medicalTest.name}"/></td>
                <td><c:out value="${medicalTest.description}"/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    </c:if>
    
</petclinic:layout>
