<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

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
                    <img src="${animal.photos[0].full}" alt="">
                    <div class="caption">
                        <h3><c:out value="${animal.name}"></c:out></h3>
                        <ul>
                            <li>Size: <c:out value="${animal.size}"></c:out></li>
                            <li>Gender: <c:out value="${animal.gender}"></c:out></li>
                            <li>Age: <c:out value="${animal.age}"></c:out></li>
                        </ul>
                        <p><c:out value="${animal.description}"></c:out></p>
                        <br>
                        <p><a href="#" class="btn btn-default" role="button">Adopt</a></p>
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
