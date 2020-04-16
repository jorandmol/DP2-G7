<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="vets">
    <c:choose>
    	<c:when test="${!edit}">
    		<h2> New Vet </h2>
        </c:when>
        <c:otherwise>
            <input type="hidden" id="username" name="username" value="${username}">
            <h2><c:out value="${username}"></c:out></h2>
       	</c:otherwise>
	</c:choose> 
    <form:form modelAttribute="vet" class="form-horizontal" id="add-vet-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="First Name" name="firstName"/>
            <petclinic:inputField label="Last Name" name="lastName"/>
            <petclinic:inputField label="Address" name="address"/>
            <petclinic:inputField label="City" name="city"/>
            <petclinic:inputField label="Telephone" name="telephone"/>
            <petclinic:selectField label="Specialties" name="specialties" names="${specialties}" size="3"/>
            <c:if test="${!edit}">
            	<petclinic:inputField label="Username" name="user.username"/>
            </c:if>
            <petclinic:inputField label="Password" name="user.password"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
            	<c:choose>
                	<c:when test="${!edit}">
                    	<button class="btn btn-default" type="submit">Add Vet</button>
                    </c:when>
                    <c:otherwise>
                    	<button class="btn btn-default" type="submit">Update Vet</button>
                    </c:otherwise>
                </c:choose> 
            </div>
        </div>
    </form:form>
</petclinic:layout>