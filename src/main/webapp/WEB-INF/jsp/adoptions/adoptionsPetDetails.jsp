<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<petclinic:layout pageName="adoptions">
	<jsp:body>
		<c:set var="n" value="0" scope="page"></c:set>
		<c:set var="n1" value="0" scope="page"></c:set>
		 
		<div class= "container">
			<div class="row">
			<div class="col-md-12">
				<div id="carousel-1" class="carousel slide" data-ride="carousel">
					<!-- indicadores -->
					
					<ol class="carousel-indicators">
						<c:forEach var="photo" items="${pet.photos}">
							<c:if test="${n == 0}">
								<li data-target="#carousel-1" data-slide-to= "0" class="active"></li>
							</c:if>
							<c:if test="${n!=0 && n<=numPhotos}">
								<li data-target="#carousel-1" data-slide-to= "${n}"></li>
							</c:if>
							 <c:set var="n" value="${n + 1}" scope="page"></c:set>
						</c:forEach>
					</ol>
					<!-- contenedor de los slide -->
					<div class="carousel-inner" role="listbox">
						<c:forEach var="photo" items="${pet.photos}">
							<c:if test="${n1==0}">
								<div class="item active">
									<a href="${pet.url}" class="thumbnail">
      									<img src="${pet.photos[0].full}" class="img-responsive" alt="" width="300" height="50">
      								</a>
								</div>
							</c:if>
							<c:if test="${n1!=0 && n1<=numPhotos}">
								<div class="item">
									<a href="${pet.url}" class="thumbnail">
      									<img src="${pet.photos[n1].full}" class="img-responsive" alt="" width="300" height="50">
      								</a>
								</div>
							</c:if>
							 <c:set var="n1" value="${n1 + 1}" scope="page"></c:set>
						</c:forEach>
					</div>
				</div>
			</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<p><c:out value="${error}"></c:out></p>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<h2>Who is <c:out value="${pet.name}"></c:out> ?</h2>
					<h3><c:out value="${pet.description}"></c:out></h3>
					<h2>Details:</h2>
					<ul>
						<li>Species: <c:out value="${pet.species}"></c:out>
                		<li>Size: <c:out value="${pet.size}"></c:out></li>
                		<li>Gender: <c:out value="${pet.gender}"></c:out></li>
                		<li>Age: <c:out value="${pet.age}"></c:out></li>
                	</ul>
				</div>
			</div>
			<div class="row">
				<div class="col-md-10">
					<h2>Do you need a friend?</h2>
					<h3>Friend is the one who in prosperity goes to be called and in adversity without being.</h3>
				</div>
				<div class="col-md-2">
	            	<form:form action="/adoptions/pet" method="post" class="form-horizontal">
						<div class="form-group has-feedback">
							<div class="form-group">
	                			<input type="hidden" name="name" value="${pet.name}">
								<input type="hidden" name="type" value="${pet.type}">
								<input type="hidden" name="age" value="${pet.age}">
	             				
	    							<button class="btn btn-default" style="float:right;" type="submit">Adopt</button>
							</div>
						</div>
					</form:form>
				</div>
			</div>
		</div>
  	</jsp:body>
</petclinic:layout>
