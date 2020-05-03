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
        
    <c:if test="${rejected}">
   	<sec:authorize access="hasAuthority('owner')">
   		<br>
   		<br>
   		<h3>Justification:</h3>
   		<tr>
   		<td><c:out value="${petRequest.justification}"></c:out><td/>
   		<tr>
   	</sec:authorize>
    </c:if>
        
	<sec:authorize access="hasAuthority('admin')">
         <form:form modelAttribute="pet" class="form-horizontal">
            <div class="form-group has-feedback">
            	<div class="form-group">
                    <label class="col-sm-2 control-label">Status</label>
                   	<div class="col-sm-10">
            			<select name="status" class="form-control">
            				<c:forEach items="${status}" var="status">
                            	<option value="${status}">${status}</option>
                            </c:forEach>
            			</select>
            		</div>
                </div>
            	<petclinic:inputField label="Justification" name="justification"/>
            </div>
            
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button class="btn btn-default" type="submit">
                    Answer Request
                    </button>
                </div>
           	</div>
         </form:form>
    </sec:authorize>
    
    </jsp:body>
</petclinic:layout>
