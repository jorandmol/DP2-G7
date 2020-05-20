<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<petclinic:layout pageName="adoptions">
	<jsp:body>
		<div class="row">
			<div class="col-md-10">
				<h2><c:out value="${pet.name}"></c:out></h2>
				<c:out value="${pet.type}"></c:out>
				<c:out value="${pet.age}"></c:out>
			</div>
			<div class="col-md-2">

			<%-- 	<spring:url value="/adoptions/pet" var="petAdoptionUrl">
					<input type="hidden" name="name" value="${pet.name}">
					<input type="hidden" name="type" value="${pet.type}">
					<input type="hidden" name="age" value="${pet.age}">
               	</spring:url>
                <p><a href="${fn:escapeXml(petAdoptionUrl)}" class="btn btn-default" role="button">Adopt</a></p>
                 --%>
                 <form:form action="/adoptions/pet" method="post" class="form-horizontal">
					<div class="form-group has-feedback">
						<div class="form-group">
                			<input type="hidden" name="name" value="${pet.name}">
							<input type="hidden" name="type" value="${pet.type}">
							<input type="hidden" name="age" value="${pet.age}">
             
    						<button class="btn btn-default" type="submit">Adopt</button>	
						</div>
					</div>
				</form:form>
			</div>
		</div>
		<br>
		<c:if test="${pet.photos[0].full != null}">
			<div class="row">
				<c:forEach var="photo" items="${pet.photos}">
  					<div class="col-xs-6 col-md-3">
    					<a href="${pet.url}" class="thumbnail">
      						<img src="${photo.full}" alt="">
    					</a>
  					</div>
  				</c:forEach>
			</div>
		</c:if>	
		<br>
		<div class= "container">
			<br>

			<div class="col-md-12">
				<div id="carousel-1" class="carousel slide" data-ride="carousel">
					<!-- indicadores -->
					<ol class="carousel-indicators">
						<li data-target="#carousel-1" data-slide-to= "0" class="active"></li>
						<li data-target="#carousel-1" data-slide-to= "1"></li>
					</ol>
					<!-- contenedor de los slide -->
					<div class="carousel-inner" role="listbox">
						<div class="item active">
      							<img src="${pet.photos[0].full}" class="img-responsive" alt="">
						</div>
						<div class="item">
      							<img src="${pet.photos[1].full}" class="img-responsive" alt="">
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<%-- <c:forEach var="numPhoto" items="${numberOfPhotos}">
							<c:if test="${numPhoto==0}">
								<li data-target="#carousel-1" data-slide-to= "0" class="active"></li>
							</c:if>
							<c:if test="${numPhoto!=0}">
								<li data-target="#carousel-1" data-slide-to= "${entero+1}"></li>
							</c:if>
						</c:forEach> --%>
  	</jsp:body>
</petclinic:layout>
