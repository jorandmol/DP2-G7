<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="treatments">
	<div class="row">
		<div class="col-md-10">
		    <h2>Current Treatments</h2>		
		</div>
		<div class="col-md-2">
            <c:if test="${isVet}">
            <spring:url value="treatments/new" var="addUrl"></spring:url>
            <a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Add New Treatment</a>
            </c:if>
		</div>
	</div>
	

    <table id="treatmentsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Name</th>
            <th>Description</th>
            <th>Medicines</th>
            <th>Time Limit</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${treatments}" var="treatment">
            <tr>
                <td>
                    <c:out value="${treatment.name}"/>
                </td>
                <td>
                    <c:out value="${treatment.description} "/>
                </td>
                <td>
                	<c:forEach var="medicine" items="${treatment.medicines}">
                        <c:out value="${medicine.name} "/>
                    </c:forEach>
                </td>
               	<td>
               		<c:out value="${treatment.timeLimit}"></c:out>
                </td>
                <td>
                    <spring:url value="treatments/{treatmentId}/edit" var="editUrl">
                        <spring:param name="treatmentId" value="${treatment.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit Treatment</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div class="col-md-11">
    	<h2>Expired Treatments</h2>
    </div>
    <table id="treatmentsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Name</th>
            <th>Description</th>
            <th>Medicines</th>
            <th>Time Limit</th>
        </tr>
        </thead>
        <tbody>
        
        <c:forEach items="${treatmentsDone}" var="treatmentDone">
            <tr>
                <td>
                    <c:out value="${treatmentDone.name}"/>
                </td>
                <td>
                    <c:out value="${treatmentDone.description} "/>
                </td>
                <td>
                	<c:forEach var="medicine" items="${treatmentDone.medicines}">
                        <c:out value="${medicine.name} "/>
                    </c:forEach>
                </td>
               	<td>
               		<c:out value="${treatmentDone.timeLimit}"></c:out>
               	</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
