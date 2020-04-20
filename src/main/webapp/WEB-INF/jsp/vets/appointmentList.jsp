<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!--  >%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%-->

<petclinic:layout pageName="appointments">
    
    <h2>Appointments for today</h2>
    
	<table id="AppointmentsTodayTable" class="table table-striped">
        <thead>
        <tr>
        	<th style="width: 300px;">Appointment date</th>
        	<th style="width: 200px;">Owner</th>
            <th style="width: 200px;">Pet</th>
            <th style="width: 500px;">Description</th>
            <th style="width: 300px;">Visit</th>
    	</tr>
    	</thead>
    	<tbody>
    	<c:forEach items="${appointmentsToday}" var="appointment">
    		<tr>
    			<td>
    				<c:out value="${appointment.appointmentDate}"/>
    			</td>
    			<td>
            		<c:out value="${appointment.owner.firstName} "/>
            		<c:out value="${appointment.owner.lastName}"/>
           		</td>
    			<td>
            		<c:out value="${appointment.pet.name}"/>
           		</td>
           		<td>
            		<c:out value="${appointment.description}"/>
           		</td>
           		<td>
                	<spring:url value="/owners/{ownerId}/pets/{petId}/visits/new" var="visitUrl">
                    	<spring:param name="ownerId" value="${appointment.owner.id}"/>
                        <spring:param name="petId" value="${appointment.pet.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(visitUrl)}" class="btn btn-default">Add Visit</a>
                </td>
    		</tr>
    	</c:forEach>
    	</tbody>
    </table>
    
    <h2>Upcoming appointments</h2>
    
    <table id="AppointmentsTable" class="table table-striped">
        <thead>
        <tr>
        	<th style="width: 300px;">Appointment date</th>
        	<th style="width: 200px;">Owner</th>
            <th style="width: 200px;">Pet</th>
            <th style="width: 500px;">Description</th>
    	</tr>
    	</thead>
    	<tbody>
    	<c:forEach items="${nextAppointments}" var="appointment">
    		<tr>
    			<td>
    				<c:out value="${appointment.appointmentDate}"/>
    			</td>
    			<td>
            		<c:out value="${appointment.owner.firstName} "/>
            		<c:out value="${appointment.owner.lastName}"/>
           		</td>
    			<td>
            		<c:out value="${appointment.pet.name}"/>
           		</td>
           		<td>
            		<c:out value="${appointment.description}"/>
           		</td>
    		</tr>
    	</c:forEach>
    	</tbody>
    </table>
</petclinic:layout>
