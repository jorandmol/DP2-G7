<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="pets">

    <h2>Pets</h2>

    <table class="table table-striped">
        <c:forEach var="pet" items="${pets}">
            <tr>
                <td valign="top">
                    <dl class="dl-horizontal">
                        <dt>Name</dt>
                        <dd><c:out value="${pet.name}"/></dd>
                        <dt>Birth Date</dt>
                        <dd><petclinic:localDate date="${pet.birthDate}" pattern="yyyy-MM-dd"/></dd>
                        <dt>Type</dt>
                        <dd><c:out value="${pet.type.name}"/></dd>
                        <dt>
                        	<spring:url value="/owners/{ownerId}/pets/{petId}/treatments" var="treatmentsUrl">
       							<spring:param name="ownerId" value="${owner.id}"/>
       							<spring:param name="petId" value="${pet.id}"/>
    						</spring:url>
    						<a href="${fn:escapeXml(treatmentsUrl)}">Treatments</a>
                        </dt>
                        <dt>
                        	<spring:url value="/owners/{ownerId}/pets/{petId}/stays" var="stayUrl">
                            	<spring:param name="ownerId" value="${owner.id}"/>
                                <spring:param name="petId" value="${pet.id}"/>
                            </spring:url>
                                <a href="${fn:escapeXml(stayUrl)}">Stays</a>
                        </dt>
                    </dl>
                </td>
                <td valign="top">
                    <table class="table-condensed">
                        <thead>
                        <tr>
                            <th>Visit Date</th>
                        </tr>
                        </thead>
                        <c:forEach var="visit" items="${pet.visits}">
                            <tr>
                               <td>
                                    <spring:url value="/owners/{ownerId}/pets/{petId}/visits/{visitId}" var="visitUrl">
                                       	<spring:param name="ownerId" value="${owner.id}"/>
                                       	<spring:param name="petId" value="${pet.id}"></spring:param>
                                       	<spring:param name="visitId" value="${visit.id}"></spring:param>
                                    </spring:url>
                                    <a href="${fn:escapeXml(visitUrl)}">
                                    	<petclinic:localDate date="${visit.date}" pattern="yyyy-MM-dd"/>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </td>
                <td valign="top">
                    <table class="table-condensed">
                		<thead>
                        	<tr>
                            	<th>Appointment Date</th>
                            	<th>Description</th>
                        	</tr>
                       	</thead>
                        	<c:forEach var="appointment" items="${pet.appointments}">
                            	<tr>
                                	<td><petclinic:localDate date="${appointment.appointmentDate}" pattern="yyyy-MM-dd"/></td>
                                	<td><c:out value="${appointment.description}"/></td>
                            	</tr>
                        	</c:forEach>
                	</table>
            	</td>
            </tr>

        </c:forEach>
    </table>
</petclinic:layout>
