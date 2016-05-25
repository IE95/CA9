<%@ page import="java.util.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ page isELIgnored="false" %>    
<%@include file="header.jsp" %>

<form class="form-horizontal" action="trade" method="POST">

  <div class="form-group">
    <label for="inputId" class="col-sm-2 control-label">شناسه کاربری</label>
    <div class="col-sm-3">
      <input type="text" name="id" class="form-control" id="inputId" placeholder="1">
    </div>
  </div>

  <div class="form-group">
    <label for="inputStockSymbole" class="col-sm-2 control-label">نماد سهام</label>
    <div class="col-sm-3">
      <input type="text" name="instrument" class="form-control" id="inputStockSymbole" placeholder="RENA1">
    </div>
  </div>

  <div class="form-group">
    <label for="inputPrice" class="col-sm-2 control-label">قیمت</label>
    <div class="col-sm-3">
      <input type="text" name="price" class="form-control" id="inputPrice" placeholder="100">
    </div>
  </div>

  <div class="form-group">
    <label for="inputQuantity" class="col-sm-2 control-label">تعداد</label>
    <div class="col-sm-3">
      <input type="text" name="quantity" class="form-control" id="inputQuantity" placeholder="100">
    </div>
  </div>

  <div class="form-group">
    <label for="inputType" class="col-sm-2 control-label">نوع انجام عملیات</label>
    <div class="col-sm-3">
        <select name="opType" class="form-control" id="inputType">
            <option value="GTC">GTC</option>
            <option value="IOC">IOC</option>
            <option value="MPO">MPO</option>
        </select>
    </div>
  </div>

  <div class="form-group">
    <div class="col-sm-offset-2 col-sm-2">
      <button type="submit" name="tradeType" value="sell"class="btn btn-primary">فروش</button>
      <button type="submit" name="tradeType" value="buy"class="btn btn-primary">خرید</button>
    </div>
  </div>
</form>

<c:if test="${hasError == 0}">
  <c:forEach var="message" items="${messages}">
    <li>
      <p><c:out value="${message}"/><p>
    </li>
  </c:forEach>  
</c:if>

<c:if test="${hasError == 1}">
  <c:forEach var="message" items="${messages}">
    <li>
      <p><c:out value="${message}"/><p>
    </li>
  </c:forEach>  
</c:if>

<%@include file="footer.jsp" %>
