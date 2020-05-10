<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="treatments">
	<div class="col-md-10"></div>
		<div class="col-md-2">
            <c:if test="${isVet}">
            <spring:url value="/vets/pets/{petId}/treatments/new" var="addUrl">
            	<spring:param name="petId" value="${petId}"/>
            </spring:url>
            <a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Add New Treatment</a>
            </c:if>
		</div>
	<br/>
    <br/>
    
    <div class="row">
		<div class="col-md-10">
		    <div id="crtT"><h2>Current Treatments</h2></div>
		</div>
	</div>
    <table id="treatmentsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Name</th>
            <th>Description</th>
            <th>Medicines</th>
            <th>Time Limit</th>
            <c:if test="${isVet}">
            	<th></th>
            </c:if>
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
                <c:if test="${isVet}">
	                <td>
	                    <spring:url value="treatments/{treatmentId}" var="editUrl">
	                        <spring:param name="treatmentId" value="${treatment.id}"/>
	                    </spring:url>
	                    <a href="${fn:escapeXml(editUrl)}">Show Treatment</a>
	                </td>
                </c:if>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <br/>
    <br/>
    <div class="row">
    	<div class="col-md-10">
    		<div id="expT"><h2>Expired Treatments</h2></div>
   		</div>
   	</div>
    <table id="treatmentsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Name</th>
            <th>Description</th>
            <th>Medicines</th>
            <th>Time Limit</th>
            <c:if test="${isVet}">
            	<th></th>
            </c:if>
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
               	<c:if test="${isVet}">
	                <td>
	                    <spring:url value="treatments/{treatmentId}" var="editUrl">
	                        <spring:param name="treatmentId" value="${treatment.id}"/>
	                    </spring:url>
	                    <a href="${fn:escapeXml(editUrl)}">Show Treatment</a>
	                </td>
                </c:if>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
