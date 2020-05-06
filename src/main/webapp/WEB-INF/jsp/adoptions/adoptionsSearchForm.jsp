<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="adoptions">
	<jsp:body>
		<div class="row">
			<div class="col-md-12">
				<h2>Adoptions</h2>
				<p>Find your new pet!</p>
				<br>			
			</div>
		</div>
		<form:form action="/adoptions/find" method="get" class="form-horizontal">
			<div class="form-group has-feedback">
				<div class="form-group">
					<label class="col-sm-2 control-label">Type</label>
					<div class="col-sm-10">
						<input type="text" name="type" class="form-control"/>						
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">Size</label>
					<div class="col-sm-10">
						<select name="size" class="form-control">
                            <option value="small">Small</option>
                            <option value="medium">Medium</option>
                            <option value="large">Large</option>
                            <option value="xlarge">Extra large</option>
                        </select>						
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">Gender</label>
					<div class="col-sm-10">
						<select name="gender" class="form-control">
                            <option value="male">Male</option>
                            <option value="female">Female</option>
                        </select>						
					</div>
				</div>
			</div>
			<input class="btn btn-default" type="submit" value="Search pets"/>
		</form:form>
	</jsp:body>
</petclinic:layout>