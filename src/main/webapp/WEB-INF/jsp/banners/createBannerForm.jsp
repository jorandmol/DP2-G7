<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="banners">
    <jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#endColabDate").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>
    <jsp:body>
    <h2>
        New banner
    </h2>
    <form:form modelAttribute="banner" class="form-horizontal" id="add-owner-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Picture" name="picture"/>
            <petclinic:inputField label="Slogan" name="slogan"/>
            <petclinic:inputField label="Target url" name="targetUrl"/>
            <petclinic:inputField label="Organization name" name="organizationName"/>
            <petclinic:inputField label="End colaborate date" name="endColabDate"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
            	<button class="btn btn-default" type="submit">Add banner</button>
            </div>
        </div>
    </form:form>
   	</jsp:body>
</petclinic:layout>
