<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="actRep" value="${ForwardConst.ACT_DIA.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}" />
<c:set var="commNew" value="${ForwardConst.CMD_NEW.getValue()}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <h2>日記 一覧</h2>
        <table id="diary_list">
            <tbody>
                <tr>
                    <th class="diary_name">氏名</th>
                    <th class="diary_date">日付</th>
                    <th class="diary_title">タイトル</th>
                    <th class="dairy_action">操作</th>
                </tr>
                <c:forEach var="diary" items="${diaries}" varStatus="status">
                    <fmt:parseDate value="${diary.diaryDate}" pattern="yyyy-MM-dd" var="diaryDay" type="date" />

                    <tr class="row${status.count % 2}">
                        <td class="diaries_name"><c:out value="${diary.name}" /></td>
                        <td class="diaries_date"><fmt:formatDate value='${diaryDay}' pattern='yyyy-MM-dd' /></td>
                        <td class="diaries_title">${diary.title}</td>
                        <td class="diaries_action"><a href="<c:url value='?action=${actRep}&command=${commShow}&id=${diary.id}' />">詳細を見る</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            （全 ${diaries_count} 件）<br />
            <c:forEach var="i" begin="1" end="${((diaries_count - 1) / maxRow) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='?action=${actRep}&command=${commIdx}&page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p><a href="<c:url value='?action=${actRep}&command=${commNew}' />">新規日報の登録</a></p>

    </c:param>
</c:import>