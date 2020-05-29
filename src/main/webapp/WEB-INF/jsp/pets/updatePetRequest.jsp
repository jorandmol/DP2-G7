<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="requests">
    <jsp:body>
    
    <h2>Pet Request</h2>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Name</th>
                <th>Birth Date</th>
                <th>Type</th>
                <th>Status</th>
                <th>Owner</th>
            </tr>
            </thead>
            <tr>
                <td><c:out value="${petRequest.name}"/></td>
                <td><petclinic:localDate date="${petRequest.birthDate}" pattern="yyyy/MM/dd"/></td>
                <td><c:out value="${petRequest.type.name}"/></td>
                <td><c:out value="${petRequest.status}"></c:out>
                <td><c:out value="${petRequest.owner.firstName} ${petRequest.owner.lastName}"/></td>
            </tr>
        </table>
        
   	<sec:authorize access="hasAuthority('owner')">
	    <c:if test="${readonly}">
	   		<br>
	   		<br>
	   		<h3>Justification:</h3>
	   		<tr>
	   		<td><c:out value="${petRequest.justification}"></c:out><td/>
	   		<tr>
	    </c:if>
   	</sec:authorize>
        
	<sec:authorize access="hasAuthority('admin')">
         <form:form modelAttribute="pet" class="form-horizontal">
            <div class="form-group has-feedback">
            	<div class="form-group">
	                <c:choose>	
                   		<c:when test="${!readonly}">
		                    <label class="col-sm-2 control-label">Status</label>
		                   	<div class="col-sm-10">
		                   		<select name="status" class="form-control">
		            				<option value="REJECTED">REJECTED</option>
		            				<option value="ACCEPTED">ACCEPTED</option>
		            			</select>
		            		</div>
                   		</c:when>
                   		<c:otherwise>
                   			<petclinic:inputField label="Status" name="status" readonly="true" />
                   		</c:otherwise>
	            	</c:choose>
            	</div>
            	<petclinic:inputField label="Justification" name="justification" readonly="${readonly}"/>
            </div>
            
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button class="btn btn-default" type="submit">
                    	<c:if test="${!readonly}">Answer Request</c:if>
                    	<c:if test="${readonly}">Return</c:if>
                    </button>
                </div>
           	</div>
         </form:form>
    </sec:authorize>
    
    </jsp:body>
</petclinic:layout>
