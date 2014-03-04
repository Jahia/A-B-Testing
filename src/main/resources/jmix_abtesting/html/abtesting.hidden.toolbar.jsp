<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="workflow" uri="http://www.jahia.org/tags/workflow" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="functions" uri="http://www.jahia.org/tags/functions" %>

<template:addResources type="css" resources="abtesting.css"/>

<div id="abtestingtoolbar">
    <div class="abtestingseparator">
        <c:forEach var="i" begin="0" end="${currentNode.properties['j:numberOfVersions'].long - 1}" step="1" varStatus="status">
            <c:if test="${i eq 0}">
                <a href="<c:url value='${url.base}${currentNode.path}.html'/>" <c:if test="${empty param['alt']}">class="selected"</c:if>>Base version</a>
            </c:if>
            <c:if test="${i ne 0}">
                <a href="<c:url value='${url.base}${currentNode.path}.html?alt=${i}'/>" <c:if test="${param['alt'] eq i}">class="selected"</c:if>>Alternative ${i}</a>
            </c:if>

        </c:forEach>
    </div>
</div>