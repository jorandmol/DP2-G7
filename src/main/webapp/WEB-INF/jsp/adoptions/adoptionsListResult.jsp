<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="adoptions">
	<jsp:body>
		<h2>Results</h2>
		<p><c:out value="${notFound}"></c:out></p>
        <c:set var="cards" value="0" scope="page"></c:set>
        <c:forEach var="animal" items="${animals}">
        <c:if test="${cards == 0}">
            <div class="row">
        </c:if>
            <div class="col-md-4">
                <div class="thumbnail">
                	<c:if test="${animal.photos[0].full != null}">
	                    <img src="${animal.photos[0].full}" alt="">
                	</c:if>
                	<c:if test="${animal.photos[0].full == null}">
	                    <img src="/resources/images/adoptions.png" alt="">
                	</c:if>
                    <div class="caption">
                        <h3><c:out value="${animal.name}"></c:out></h3>
                        <sec:authorize access="hasAuthority('owner')">
                        <spring:url value="/adoptions/pet/{petId}" var="petAdoptionUrl">
                        	<spring:param name="petId" value="${animal.id}"/>
                   	 	</spring:url>
                   	 	</sec:authorize>
                   	 	<sec:authorize access="hasAuthority('admin')">
                        <spring:url value="/adoptions/owner/{ownerId}/pet/{petId}" var="petAdoptionUrl">
                        	<spring:param name="ownerId" value="${ownerId}"/>
                        	<spring:param name="petId" value="${animal.id}"/>
                   	 	</spring:url>
                   	 	</sec:authorize>
                    	<p><a href="${fn:escapeXml(petAdoptionUrl)}" class="btn btn-default" role="button">More info</a></p>
                    </div>
                </div>
            </div>
            <c:set var="cards" value="${cards + 1}" scope="page"></c:set>
            <c:if test="${cards == 3}">
                <c:set var="cards" value="0" scope="page"></c:set>
            </c:if>
        <c:if test="${cards == 0}">
            </div>
        </c:if>
        </c:forEach>
    <c:if test="${cards > 0 && cards < 3}">
        </div>
    </c:if>
	</jsp:body>
</petclinic:layout>
