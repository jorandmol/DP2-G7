<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>


<petclinic:layout pageName="stays">
	<jsp:attribute name="customScript">
        <script>
									$(function() {
										$("#registerDate").datepicker({
											dateFormat : 'yy/mm/dd'
										});
										$("#releaseDate").datepicker({
											dateFormat : 'yy/mm/dd'
										});
									});
								</script>
    </jsp:attribute>
	<jsp:body>
        <h2>
			<c:if test="${stay['new']}">New </c:if>Stay</h2>

        <b>Pet</b>
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
                <td><c:out value="${stay.pet.name}" /></td>
                <td><petclinic:localDate
						date="${stay.pet.birthDate}" pattern="yyyy/MM/dd" /></td>
                <td><c:out value="${stay.pet.type.name}" /></td>
                <td><c:out
						value="${stay.pet.owner.firstName} ${stay.pet.owner.lastName}" /></td>
            </tr>
        </table>
        <sec:authorize access="hasAuthority('owner')"> 	
        <form:form modelAttribute="stay" class="form-horizontal">
            <div class="form-group has-feedback">
                <petclinic:inputField label="Register date"
					name="registerDate" />
                <petclinic:inputField label="Release date"
					name="releaseDate" />
            </div>
            <div class="form-group">
                 <div class="col-sm-offset-2 col-sm-10">
                     <button class="btn btn-default" type="submit">
                   <c:choose>
                     <c:when test="${!edit}">Add</c:when>
                     <c:otherwise>Update</c:otherwise>
                   </c:choose> Stay</button>
                 </div>
           </div>     
        </form:form>
        </sec:authorize>   

        <br />
        <b>Older stays</b>
        <table class="table table-striped">
            <tr>
                <th>Register date</th>
                <th>Exit date</th>
            </tr>
            <c:forEach var="stay" items="${stay.pet.stays}">
                <c:if test="${!stay['new']}">
                    <tr>
                        <td><petclinic:localDate
								date="${stay.registerDate}" pattern="yyyy/MM/dd" /></td>
                        <td><petclinic:localDate
								date="${stay.releaseDate}" pattern="yyyy/MM/dd" /></td>
                    </tr>
                </c:if>
            </c:forEach>
        </table>
    </jsp:body>

</petclinic:layout>