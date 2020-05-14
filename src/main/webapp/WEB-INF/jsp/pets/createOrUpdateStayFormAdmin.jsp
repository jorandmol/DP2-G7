<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>


<petclinic:layout pageName="stays">
	<jsp:body>
        <h2>Stay</h2>

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
        <sec:authorize access="hasAuthority('admin')"> 	
        <form:form modelAttribute="stay" class="form-horizontal">
            <div class="form-group has-feedback">
            
                <petclinic:inputField  label="Register date"
					name="registerDate" readonly="true"/>
                <petclinic:inputField label="Release date"
					name="releaseDate" readonly="true"/>          
                <petclinic:selectField  label="Status" name="status"
						names="${status}" size="1"></petclinic:selectField>
            </div>
            <div class="form-group">
                 <div class="col-sm-offset-2 col-sm-10">
                     <button class="btn btn-default" type="submit"> Change Status</button>
                 </div>
           </div>     
        </form:form>
        </sec:authorize>   

        <br />
    </jsp:body>

</petclinic:layout>