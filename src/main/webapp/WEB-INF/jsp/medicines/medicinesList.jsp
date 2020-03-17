<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="medicines">
    <h2>Medicines</h2>

    <table id="medicinesTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">expirationDate</th>
            <th style="width: 200px;">description</th>
            <th>City</th>
            <th style="width: 120px">identificator</th>
            <th>Pets</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${selections}" var="medicine">
            <tr>
                <td>
 
                   <c:out value="${medicine.identificator}"/>
                </td>
                <td>
                    <c:out value="${medicine.description}"/>
                </td>
                <td>
                    <c:out value="${medicine.expirationDate}"/>
                </td>
                
      
<!--
                <td> 
                    <c:out value="${owner.user.username}"/> 
                </td>
                <td> 
                   <c:out value="${owner.user.password}"/> 
                </td> 
-->
                
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
