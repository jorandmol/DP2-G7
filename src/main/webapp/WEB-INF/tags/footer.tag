<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- Placed at the end of the document so the pages load faster --%>
<spring:url value="/webjars/jquery/2.2.4/jquery.min.js" var="jQuery"/>
<script src="${jQuery}"></script>

<%-- jquery-ui.js file is really big so we only load what we need instead of loading everything --%>
<spring:url value="/webjars/jquery-ui/1.11.4/jquery-ui.min.js" var="jQueryUiCore"/>
<script src="${jQueryUiCore}"></script>

<%-- Bootstrap --%>
<spring:url value="/webjars/bootstrap/3.3.6/js/bootstrap.min.js" var="bootstrapJs"/>
<script src="${bootstrapJs}"></script>

<jstl:if test="${banner != null}">
	<div class="text-center pt-3">
		<a href="${banner.targetUrl}" target="_blank"><img src="${banner.picture}" /></a>
	</div>
</jstl:if>
