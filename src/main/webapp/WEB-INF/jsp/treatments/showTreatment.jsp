<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="treatment">

    <h2>Treatment Information</h2>
	<h3><b><c:out value="${treatment.name}"/></b></h3>

    <table id="treatmentTable" class="table table-striped">
        <tr>
            <th>Description</th>
            <td><c:out value="${treatment.description}"/></td>
        </tr>
        <tr>
            <th>Expiration Date</th>
            <td><c:out value="${treatment.timeLimit}"/></td>
        </tr>
        <tr>
            <th>Medicines</th>
            <td>
	            <ul>
	            	<c:forEach items="${treatment.medicines}" var="medicine">
	            		<li><c:out value="${medicine.code} - ${medicine.name}"/></li>
	            	</c:forEach>
	            </ul>
            </td>
        </tr>
    </table>
    <div class="row">
	<c:if test="${isEditableTreatment}">
    	<div class="col-md-4">
			<spring:url value="/vets/pets/{petId}/treatments/{treatmentId}/edit" var="editUrl">
				<spring:param name="petId" value="${petId}"/>
		        <spring:param name="treatmentId" value="${treatment.id}"/>
		    </spring:url>
			<a id="editBtn" href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit Treatment</a>
			<spring:url value="/vets/pets/{petId}/treatments" var="editUrl">
				<spring:param name="petId" value="${petId}"/>
		    </spring:url>
			<a id="returnBtn" href="${fn:escapeXml(editUrl)}" class="btn btn-default">Get back</a>
    	</div>
	</c:if>
    </div>
	<br>
	<c:if test="${not empty treatmentHistory}">
	<h3><b>Treatment History</b></h3>
	<table id="treatmentHistoryTable" class="table table-striped">
		<thead>
	        <tr>
	            <th>Name</th>
	            <th>Description</th>
	            <th>Time Limit</th>
	            <th>Medicines</th>
            	<th></th>
	        </tr>
	    </thead>
	    <tbody>
	    	<c:forEach items="${treatmentHistory}" var="historyEntry">
	    		<tr>
	    			<td><c:out value="${historyEntry.treatment.name}"/></td>
	    			<td><c:out value="${historyEntry.treatment.description}"/></td>
	    			<td><c:out value="${historyEntry.treatment.timeLimit}"/></td>
	    			<td>
	    				<ul>
		            	<c:forEach items="${historyEntry.medicines}" var="medicine">
			            	<c:if test="${not empty medicine}"><li><c:out value="${medicine}"/></li></c:if>
		            	</c:forEach>
	            		</ul>
	    			</td>
	    			<td>
		    			<spring:url value="/vets/pets/{petId}/treatments/{treatmentId}/history/{treatmentHistoryId}/delete" var="deleteUrl">
			        		<spring:param name="petId" value="${petId}"/>
			        		<spring:param name="treatmentId" value="${treatment.id}"/>
			        		<spring:param name="treatmentHistoryId" value="${historyEntry.treatment.id}"/>
			    		</spring:url>
						<a href="${fn:escapeXml(deleteUrl)}">Delete register</a>
	    			</td>
	    		</tr>
	    	</c:forEach>
	    </tbody>
    </table>
    </c:if>
</petclinic:layout>
