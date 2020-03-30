<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!--  >%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%-->

<petclinic:layout pageName="banners">

    <h2>Banners</h2>
    
	<table id="bannersTable" class="table table-striped">
        <thead>
        <tr>
        	<th style="width: 500px;">Slogan</th>
            <th style="width: 500px;">Name</th>
            <th style="width: 500px;">End colaboration date</th>
            <th>Function</th>
    	</tr>
    	</thead>
    	<tbody>
    	<c:forEach items="${banners}" var="banner">
    		<tr>
    			<td>
    				<c:out value="${banner.slogan}"/>
    			</td>
    			<td>
            		<c:out value="${banner.organizationName}"/>
           		</td>
           		<td>
            		<c:out value="${banner.endColabDate}"/>
           		</td>
           		<td>
           			<sec:authorize access="hasAuthority('admin')">
						<spring:url value="/banners/{bannerId}/delete" var="bannerUrl">
                        	<spring:param name="bannerId" value="${banner.id}"/>
                    	</spring:url>
                    <a href="${fn:escapeXml(bannerUrl)}" class="btn btn-default">Delete banner</a>
					</sec:authorize>
           		</td>
    		</tr>
    	</c:forEach>
    	</tbody>
    </table>
    <span class="error-text">
    <c:out value="${error}"></c:out>
    </span>
	<table class="table-buttons">
		<tr>
            <td>
    			<sec:authorize access="hasAuthority('admin')">
					<a class="btn btn-default" href='<spring:url value="/banners/new" htmlEscape="true"/>'>Create banner</a>
				</sec:authorize>
			</td>
		</tr>
	</table>
</petclinic:layout>
