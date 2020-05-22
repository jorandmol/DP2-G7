<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="owners">
    <jsp:body>
        <h2><c:if test="${visit['new']}">New </c:if>Visit</h2>

        <h3>Pet information</h3>
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
                <td><strong><c:out value="${visit.pet.name}"/></strong></td>
                <td><petclinic:localDate date="${visit.pet.birthDate}" pattern="yyyy/MM/dd"/></td>
                <td><c:out value="${visit.pet.type.name}"/></td>
                <td><c:out value="${visit.pet.owner.firstName} ${visit.pet.owner.lastName}"/></td>
            </tr>
        </table>

        <form:form modelAttribute="visit" class="form-horizontal">
            <div class="form-group has-feedback">
                <petclinic:inputField readonly="true" label="Date" name="date"/>
                <petclinic:inputField label="Description" name="description"/>
                <div class="form-group">
                	<label class="col-sm-2 control-label"><strong>Medical tests</strong></label>
                	<div class="col-sm-offset-2 col-sm-10">
                		<form:checkboxes  element="div" cssStyle="width:50px" items="${tests}" path="medicalTests"/>
                	</div>
                </div>
            </div>

            <div class="form-group has-feedback">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="petId" value="${visit.pet.id}"/>
                    <button class="btn btn-default" type="submit">
                    <c:choose><c:when test="${visit['new']}">Add Visit</c:when><c:otherwise>Update Visit</c:otherwise></c:choose>
                    </button>
                </div>
            </div>
        </form:form>
	
        <br/>
        <c:if test="${visit['new']}">
        <b>Previous Visits</b>
        <table class="table table-striped">
            <tr>
                <th>Date</th>
                <th>Description</th>
            </tr>
            <c:forEach var="visit" items="${visit.pet.visits}">
                <c:if test="${!visit['new']}">
                    <tr>
                        <td><petclinic:localDate date="${visit.date}" pattern="yyyy/MM/dd"/></td>
                        <td><c:out value="${visit.description}"/></td>
                    </tr>
                </c:if>
            </c:forEach>
        </table>
        </c:if>
    </jsp:body>

</petclinic:layout>
