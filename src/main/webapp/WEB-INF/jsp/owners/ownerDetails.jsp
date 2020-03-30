<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="owners">

    <h2>Owner Information</h2>


    <table class="table table-striped">
        <tr>
            <th>Name</th>
            <td><b><c:out value="${owner.firstName} ${owner.lastName}"/></b></td>
        </tr>
        <tr>
            <th>Address</th>
            <td><c:out value="${owner.address}"/></td>
        </tr>
        <tr>
            <th>City</th>
            <td><c:out value="${owner.city}"/></td>
        </tr>
        <tr>
            <th>Telephone</th>
            <td><c:out value="${owner.telephone}"/></td>
        </tr>
    </table>

    <spring:url value="{ownerId}/edit" var="editUrl">
        <spring:param name="ownerId" value="${owner.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit Owner</a>

    <spring:url value="{ownerId}/pets/new" var="addUrl">
        <spring:param name="ownerId" value="${owner.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Add New Pet</a>

    <br/>
    <br/>
    <br/>
    <h2>Pets</h2>

    <table class="table table-striped">
        <c:forEach var="pet" items="${owner.pets}">

            <tr>
                <td valign="top">
                    <dl class="dl-horizontal">
                        <dt>Name</dt>
                        <dd><c:out value="${pet.name}"/></dd>
                        <dt>Birth Date</dt>
                        <dd><petclinic:localDate date="${pet.birthDate}" pattern="yyyy-MM-dd"/></dd>
                        <dt>Type</dt>
                        <dd><c:out value="${pet.type.name}"/></dd>
                        <br/>
                        <dt>
                        	<spring:url value="/owners/{ownerId}/pets/{petId}/treatments" var="treatmentsUrl">
       							<spring:param name="ownerId" value="${owner.id}"/>
       							<spring:param name="petId" value="${pet.id}"/>
    						</spring:url>
    						<a href="${fn:escapeXml(treatmentsUrl)}" class="btn btn-default">Treatments</a>
                        </dt>
                    </dl>
                </td>
                <td valign="top">
                    <table class="table-condensed">
                        <thead>
                        <tr>
                            <th>Visit Date</th>
                            <th>Description</th>
                        </tr>
                        </thead>
                        <c:forEach var="visit" items="${pet.visits}">
                            <tr>
                                <td><petclinic:localDate date="${visit.date}" pattern="yyyy-MM-dd"/></td>
                                <td><c:out value="${visit.description}"/></td>
                            </tr>
                        </c:forEach>
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
                                <td>
                                    <spring:url value="/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/edit" var="editAppointmentUrl">
                                        <spring:param name="ownerId" value="${owner.id}"/>
                                        <spring:param name="petId" value="${pet.id}"></spring:param>
                                        <spring:param name="appointmentId" value="${appointment.id}"></spring:param>
                                    </spring:url>
                                    <a href="${fn:escapeXml(editAppointmentUrl)}">Edit</a>
                                </td>
                            </tr>
                            <tr>
                                <td><petclinic:localDate date="${appointment.appointmentDate}" pattern="yyyy-MM-dd"/></td>
                                <td><c:out value="${appointment.description}"/></td>
                                <td>
                                	<spring:url value="/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/delete" var="deleteAppointmentUrl">
        								<spring:param name="ownerId" value="${owner.id}"/>
        								<spring:param name="petId" value="${pet.id}"></spring:param>
        								<spring:param name="appointmentId" value="${appointment.id}"></spring:param>
    								</spring:url>
    								<a href="${fn:escapeXml(deleteAppointmentUrl)}">Delete</a>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/edit" var="petUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(petUrl)}" class="btn btn-default">Edit Pet</a>
                            </td>
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/visits/new" var="visitUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(visitUrl)}" class="btn btn-default">Add Visit</a>
                            </td>
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/appointments/new" var="appointmentUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(appointmentUrl)}" class="btn btn-default">Add Appointment</a>
                            </td>
                        </tr>
                    </table>
					<span class="error-text"> <c:out value="${errors}"></c:out> </span>
                </td>
            </tr>

        </c:forEach>
    </table>
</petclinic:layout>
