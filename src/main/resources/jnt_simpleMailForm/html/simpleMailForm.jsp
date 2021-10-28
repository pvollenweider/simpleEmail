<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="ui" uri="http://www.jahia.org/tags/uiComponentsLib" %>
<%@ taglib prefix="functions" uri="http://www.jahia.org/tags/functions" %>
<%@ taglib prefix="query" uri="http://www.jahia.org/tags/queryLib" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="s" uri="http://www.jahia.org/tags/search" %>
<%@ taglib prefix="sm" uri="http://jahia.com/sm/taglibs" %>

<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>


<c:set var="errorMsg" value="${sessionScope['j_error']}"/>
<c:set var="mailSent" value="false"/>
<c:if test="${not empty errorMsg}">
    <c:choose>
        <c:when test="${errorMsg eq 'email'}">
            <div class="alert alert-warning alert-dismissible fade show" role="alert">
                <strong>Email format not valid</strong>
                <br/><br/>Please enter a valid Email adress</a>.
            </div>
        </c:when>
        <c:when test="${errorMsg eq 'message'}">
            <div class="alert alert-warning alert-dismissible fade show" role="alert">
                <strong>No message</strong>
                <br/><br/>Please type a message</a>.
            </div>
        </c:when>
        <c:when test="${errorMsg eq 'sendMessage'}">
            <div class="alert alert-warning alert-dismissible fade show" role="alert">
                <strong>Sorry! we are unable to send you an email.</strong>
                <br/><br/>Please try again ot the contact the <a href="mailto:support@jahia.com">support team</a>.
            </div>
        </c:when>

        <c:when test="${errorMsg eq 'mailSent'}">
            <c:set var="mailSent" value="true"/>
            Your mail has been sent!
        </c:when>
    </c:choose>
</c:if>

<c:if test="${! mailSent}">
    <c:url value="${url.base}${currentNode.path}.sendEmail.do" var="sendEmailURL"/>
    <form class="form-horizontal" action="${sendEmailURL}" method="post">
        <div class="mb-3">
            <label for="emailAddress" class="form-label">Email address</label>
            <input type="email" class="form-control" id="emailAddress" name="emailAddress" aria-describedby="emailHelp">
            <div id="emailHelp" class="form-text">We'll never share your email with anyone else.</div>
        </div>
        <div class="mb-3">
            <label for="message" class="form-label">Example textarea</label>
            <textarea class="form-control" id="message" name="message" rows="3"></textarea>
        </div>
        <button type="submit" class="btn btn-primary">Submit</button>

    </form>
</c:if>
${sm:removeSessionAttribute(renderContext.request, 'j_error')}
