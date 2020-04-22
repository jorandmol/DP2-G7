<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="treatments">
	<jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#timeLimit").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>
    
    <jsp:body>
    	<h2><c:if test="${!edit}">New </c:if>Treatment</h2>
    	
    	<form:form modelAttribute="treatment" class="form-horizontal">
    		<div class="form-group has-feedback">
    			<c:choose>
    				<c:when test="${!edit}">
                        <petclinic:inputField label="Name" name="name"/>
                        <petclinic:inputField label="Description" name="description"/>
    					<petclinic:inputField label="Limit Date" name="timeLimit"/>
    					<div class="form-group">
                            <label class="col-sm-2 control-label">Medicines</label>
                            <div class="col-sm-10">
                                <select name="medicines" class="form-control" multiple>
                                    <c:forEach items="${medicines}" var="medicine">
                                        <option value="${medicine.id}">${medicine.code} - ${medicine.name}</option>
                                    </c:forEach>
                                </select>
                                <c:out value="${treatmentError}" />
                            </div>
                        </div>
    				</c:when>
    				<c:otherwise>
    					<petclinic:inputField label="Name" name="name"/>
                        <petclinic:inputField label="Description" name="description"/>
    					<petclinic:inputField label="Limit Date" name="timeLimit"/>
    					<div class="form-group">
                            <label class="col-sm-2 control-label">Medicines</label>
                            <div class="col-sm-10">
                                <select name="medicines" class="form-control" multiple>
                                    <c:forEach items="${medicines}" var="medicine">
                                        <option value="${medicine.id}">${medicine.code} - ${medicine.name}</option>
                                    </c:forEach>
                                </select>
                                <c:out value="${treatmentError}" />
                            </div>
                        </div>
                        <input type="hidden" name="id" value="${treatment.id}">
    				</c:otherwise>
    			</c:choose>
    		</div>
    		
    		<div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button class="btn btn-default" type="submit">
                    <c:choose>
                        <c:when test="${!edit}">New</c:when>
                        <c:otherwise>Update</c:otherwise>
                    </c:choose>
                         Treatment</button>
                </div>
            </div>
    	</form:form>
    </jsp:body>
</petclinic:layout>
