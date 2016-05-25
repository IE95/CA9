<%@ page import="java.util.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ page isELIgnored="false" %>    
<%@include file="header.jsp" %>

<form class="form-horizontal" action="depositReq" method="POST">

	<form class="form-horizontal">
		<div class="form-group">
			<div class="col-sm-2">
				<input type="text" name="id" class="form-control" id="exampleInputEmail3" placeholder="شناسه کاربری">
				<input type="text" name="amount" class="form-control" id="exampleInputPassword3" placeholder="میزان اعتبار">
			</div>
		</div>
		<button type="submit" class="btn btn-default">ثبت درخواست</button>
	</form>

</form>

<br/>
<br/>

<c:if test="${hasError == 0}">
<p><c:out value="${message}"/><p>
</c:if>

<c:if test="${hasError == 1}">
<p><c:out value="${message}"/><p>
</c:if>

<%@include file="footer.jsp" %>
