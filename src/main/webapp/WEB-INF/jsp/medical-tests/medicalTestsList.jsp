<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>  

<petclinic:layout pageName="medicalTests">
    <h2>Medical Tests</h2>

    <table id="medicalTestsTable" class="table table-striped">
        <thead>
        <tr>
        	<th>Name</th>
            <th>Description</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${medicalTests}" var="medicalTest">
            <tr id="medicalTest">
                <td>
                   <c:out value="${medicalTest.name}"/>
                </td>
                <td>
                    <c:out value="${medicalTest.description}"/>
                </td>
                <td>
                	<spring:url value="/medical-tests/{medicalTestId}/edit" var="editMedicalTestUrl">
                    	<spring:param name="medicalTestId" value="${medicalTest.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(editMedicalTestUrl)}" class="btn btn-default">Edit</a>
                </td>              
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div>
    	<sec:authorize access="hasAuthority('admin')">
			<a class="btn btn-default" href='<spring:url value="medical-tests/new" htmlEscape="true"/>'>New medical test</a>
		</sec:authorize>
    </div>
</petclinic:layout>
