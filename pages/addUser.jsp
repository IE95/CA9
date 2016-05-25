<%@ page import="java.util.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ page isELIgnored="false" %>    
<%@include file="header.jsp" %>

<form class="form-horizontal" action="addUser" method="POST">

  <div class="form-group">
    <label for="inputId" class="col-sm-2 control-label">شناسه کاربری</label>
    <div class="col-sm-3">
      <input type="text" name="id" class="form-control" id="inputId" placeholder="1">
    </div>
  </div>

  <div class="form-group">
    <label for="inputName" class="col-sm-2 control-label">نام</label>
    <div class="col-sm-3">
      <input type="text" name="name" class="form-control" id="inputStockSymbole" placeholder="اسدالله">
    </div>
  </div>

  <div class="form-group">
    <label for="inputFamily" class="col-sm-2 control-label">نام خانوادگی</label>
    <div class="col-sm-3">
      <input type="text" name="family" class="form-control" id="inputPrice" placeholder="صافکار">
    </div>
  </div>

  <div class="form-group">
    <div class="col-sm-offset-2 col-sm-2">
      <input type="submit" value="ثبت نام" class="btn btn-primary">
    </div>
  </div>
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
