<%@ page import="java.util.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ page isELIgnored="false" %>    
<%@include file="header.jsp" %>

<form class="form-horizontal" action="tradeQueue" method="POST">

  <div class="form-group">
    <label for="inputStockId" class="col-sm-1 control-label">شناسه سهام</label>
    <div class="col-sm-2">
      <input type="text" name="instrument" class="form-control" id="inputId" placeholder="RENA1">
    </div>
    <button type="submit" class="btn btn-default">مشاهده صف ها</button>

  </div>

</form>

<br/>
<br/>

<c:if test="${hasError == 1}">
   <p><c:out value="${message}"/><p>
</c:if>

<c:if test="${hasError == 0}">
   <h1> صف خرید</h1>
   <table>
		<tr>
			<th>شناسه کاربر</th>
			<th>نام</th>
			<th>قیمت</th>
			<th>تعداد</th>
			<th>نوع عملیات</th>
		</tr>
	<c:forEach var="order" items="${buyQ}">
		<tr>
			<td><c:out value="${order.user.id}"/></td>
			<td><c:out value="${order.user.name}"/></td>
			<td><c:out value="${order.price}"/></td>
			<td><c:out value="${order.quantity}"/></td>
			<td><c:out value="${order.opType}"/></td>
        </tr>
    </c:forEach>
    </table>

   <h1> صف فروش</h1>
   <table>
		<tr>
			<th>شناسه کاربر</th>
			<th>نام</th>
			<th>قیمت</th>
			<th>تعداد</th>
			<th>نوع عملیات</th>
		</tr>
	<c:forEach var="order" items="${sellQ}">
		<tr>
			<td><c:out value="${order.user.id}"/></td>
			<td><c:out value="${order.user.name}"/></td>
			<td><c:out value="${order.price}"/></td>
			<td><c:out value="${order.quantity}"/></td>
			<td><c:out value="${order.opType}"/></td>
        </tr>
    </c:forEach>
    </table>

</c:if>

<%@include file="footer.jsp" %>
