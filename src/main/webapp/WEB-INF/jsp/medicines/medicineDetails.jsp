<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="medicines">

    <h2>Owner Information</h2>


    <table class="table table-striped">
        <tr>
            <th>Medicine´s Identificator</th>
            <td><b><c:out value="${medicine.identificator}"/></b></td>
        </tr>
        <tr>
            <th>Medicine´s Expiration Date</th>
            <td><c:out value="${medicine.expirationDate}"/></td>
        </tr>
        <tr>
            <th>Medicine´s Description</th>
            <td><c:out value="${medicine.description}"/></td>
        </tr>
      
    </table>



</petclinic:layout>
