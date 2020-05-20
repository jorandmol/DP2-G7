<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="petTypes">
    <jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#date").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>
    <jsp:body>
    <c:choose>
    	<c:when test="${!edit}">
    		<h2> Pet Type </h2>
        </c:when>
        <c:otherwise>
            <input type="hidden" id="name" name="name" value="${name}">
            <h2><c:out value="${name}"></c:out></h2>
       	</c:otherwise>
	</c:choose> 
       <form:form modelAttribute="petType" class="form-horizontal" id="add-petType-form">
            <div class="form-group has-feedback">
                <petclinic:inputField label="Enter name" name="name"/>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <c:choose>
                    <c:when test="${!edit}">
                     <button class="btn btn-default" type="submit">Add pet type</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Update Pet Type</button>
                    </c:otherwise>
                </c:choose> 
                </div>
            </div>
        </form:form>
    </jsp:body>

</petclinic:layout>
