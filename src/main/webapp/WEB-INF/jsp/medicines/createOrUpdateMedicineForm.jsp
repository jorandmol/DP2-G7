<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="medicines">
	<jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#expirationDate").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>
    <jsp:body>
    <h2>
        <c:if test="${!edit}">">New </c:if> Medicine
    </h2>
    <form:form modelAttribute="medicine" class="form-horizontal" id="add-medicine-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Name" name="name"/>
            <petclinic:inputField label="Code" name="code"/>
            <petclinic:inputField label="Expiration Date" name="expirationDate"/>
            <petclinic:inputField label="Description" name="description"/>
        </div>
        <div>
	        <c:forEach items="${messages}" var="message">
	        	 <p style="color:red"><c:out value="${message}"></c:out></p>
	        </c:forEach>
	    </div>
        <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button class="btn btn-default" type="submit">
                    <c:choose>
                        <c:when test="${!edit}">Add </c:when>
                        <c:otherwise>Update </c:otherwise>
                    </c:choose>Medicine</button>
                </div>
            </div>
        <div>
			<a class="btn btn-default" href='<spring:url value="/medicines" htmlEscape="true"/>'>Return</a>
    </div>
    </form:form>
    </jsp:body>
</petclinic:layout>
