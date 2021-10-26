<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="constants.ForwardConst" %>
<%@ page import="constants.AttributeConst" %>

<c:set var="actDia" value="${ForwardConst.ACT_DIA.getValue()}" />
<c:set var="commUpd" value="${ForwardConst.CMD_UPDATE.getValue()}" />
<c:set var="commDel" value="${ForwardConst.CMD_DESTROY.getValue()}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">

        <h2>日記 編集ページ</h2>
        <form method="POST" action="<c:url value='?action=${actDia}&command=${commUpd}' />">
            <c:import url="_form.jsp" />
        </form>

            <p><a href="#" onclick="confirmDestroy();">このメッセージを削除する</a></p>
        <form method="POST" action="<c:url value='?action=${actDia}&command=${commDel}' />">
        <input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
        <input type="hidden" name="${AttributeConst.DIA_ID.getValue()}" value="${diary.id}" />
        </form>
        <script>
         function confirmDestroy() {
            if(confirm("本当に削除してよろしいですか？")) {
                document.forms[1].submit();
            }
        }
        </script>
        <p>
            <a href="<c:url value='?action=Diary&command=index' />">一覧に戻る</a>
        </p>
    </c:param>
</c:import>