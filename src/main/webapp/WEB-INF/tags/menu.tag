<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!--  >%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%-->
<%@ attribute name="name" required="true" rtexprvalue="true"
	description="Name of the active menu: home, owners, vets, requests, banners, pets, appointments or error"%>

<nav class="navbar navbar-default" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand"
				href="<spring:url value="/" htmlEscape="true" />"><span></span></a>
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#main-navbar">
				<span class="sr-only"><os-p>Toggle navigation</os-p></span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
		</div>
		<div class="navbar-collapse collapse" id="main-navbar">
			<ul class="nav navbar-nav">

				<petclinic:menuItem active="${name eq 'home'}" url="/"
					title="home page">
					<span class="glyphicon glyphicon-home" aria-hidden="true"></span>
					<span>Home</span>
				</petclinic:menuItem>

				<sec:authorize access="hasAuthority('admin')">
					<petclinic:menuItem active="${name eq 'owners'}" url="/owners/find"
						title="find owners">
						<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
						<span>Find owners</span>
					</petclinic:menuItem>
				</sec:authorize>

				<sec:authorize access="hasAuthority('admin')">
					<petclinic:menuItem active="${name eq 'vets'}" url="/vets"
						title="veterinarians">
						<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
						<span>Veterinarians</span>
					</petclinic:menuItem>
				</sec:authorize>
				
				<sec:authorize access="hasAuthority('owner')">
				<petclinic:menuItem active="${name eq 'pets'}" url="/owner/pets"
					title="pets">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>My Pets</span>
				</petclinic:menuItem>
				</sec:authorize>
	
				<sec:authorize access="hasAuthority('owner')">
				<petclinic:menuItem active="${name eq 'requests'}" url="/owner/requests"
					title="requests">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>My Requests</span>
				</petclinic:menuItem>
				</sec:authorize>
	
				<sec:authorize access="hasAuthority('veterinarian')">
					<petclinic:menuItem active="${name eq 'appointments'}"
						url="/appointments" title="appointments">
						<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
						<span>Appointments</span>
					</petclinic:menuItem>
				</sec:authorize>

				<sec:authorize access="hasAuthority('veterinarian')">
				<petclinic:menuItem active="${name eq 'pets'}" url="/vets/pets"
					title="pets">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>Pets</span>
				</petclinic:menuItem>
				</sec:authorize>
				
				<sec:authorize access="hasAuthority('admin')">
					<petclinic:menuItem active="${name eq 'stays'}"
						url="/admin/stays" title="stays">
						<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
						<span>Stays</span>
					</petclinic:menuItem>
				</sec:authorize>
        
	<%-- 		<sec:authorize access="hasAuthority('admin')">
				<petclinic:menuItem active="${name eq 'error'}" url="/oups"
					title="trigger a RuntimeException to see how it is handled">
					<span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span>
					<span>Error</span>
				</petclinic:menuItem>
				</sec:authorize> --%>
        
			</ul>
			
			<ul class="nav navbar-nav">
				<sec:authorize access="hasAuthority('admin')">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"> <span class="glyphicon glyphicon-cog"></span>
							<strong><c:out value="Management"></c:out></strong> <span
							class="glyphicon glyphicon-chevron-down"></span>
					</a>
						<ul class="dropdown-menu">
							<li>
								<div class="navbar-login">
									<div class="row">
									<div class="col-lg-3"></div>
										<p class="text-left">
        									<strong><a href='<spring:url value="/banners" htmlEscape="true"/>'>Banners</a></strong>
										</p>		
										<div class="col-lg-3"></div>
										<p class="text-left">
											<strong><a href='<spring:url value="/medical-tests" htmlEscape="true"/>'>Medical tests</a></strong>
										</p>
										<div class="col-lg-3"></div>
										<p class="text-left">
											<strong><a href='<spring:url value="/medicines" htmlEscape="true"/>'>Medicines</a></strong>
										</p>
										<div class="col-lg-3"></div>
										<p class="text-left">
											<strong><a href='<spring:url value="/requests" htmlEscape="true"/>'>Pet requests</a></strong>
										</p>
										<div class="col-lg-3"></div>
										<p class="text-left">
        									<strong><a href='<spring:url value="/pet-type" htmlEscape="true"/>'>Pet types</a></strong>
										</p>
									</div>
								</div>
							</li>
						</ul></li>
				</sec:authorize>
			</ul>
		

			<ul class="nav navbar-nav navbar-right">
				<sec:authorize access="!isAuthenticated()">
					<li><a href="<c:url value="/login" />">Login</a></li>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"> <span class="glyphicon glyphicon-user"></span>
							<strong><sec:authentication property="name" /></strong> <span
							class="glyphicon glyphicon-chevron-down"></span>
					</a>
						<ul class="dropdown-menu">
							<li>
								<div class="navbar-login">
									<div class="row">
										<div class="col-lg-4">
											<p class="text-center">
												<span class="glyphicon glyphicon-user icon-size"></span>
											</p>
										</div>
										<div class="col-lg-8">
											<p class="text-left">
												<strong><sec:authentication property="name" /></strong>
											</p>
											<p class="text-left">
												<a href="<c:url value="/logout" />"
													class="btn btn-primary btn-block btn-sm">Logout</a>
											</p>
										</div>
									</div>
								</div>
							</li>
							<sec:authorize access="!hasAuthority('admin')">
							<li class="divider"></li>
							<li>
								<div class="navbar-login navbar-login-session">
									<div class="row">
										<div class="col-lg-12">
											<p>
												<spring:url value="/users/profile" var="profileUrl"></spring:url>
												<a href="${fn:escapeXml(profileUrl)}" class="btn btn-primary btn-block">My Profile</a>
											</p>
										</div>
									</div>
								</div>
							</li>
							</sec:authorize>
						</ul></li>
				</sec:authorize>
			</ul>
		</div>



	</div>
</nav>
