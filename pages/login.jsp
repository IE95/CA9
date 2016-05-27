<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ page isELIgnored="false" %>    
<html>
	<head>
		<title>Login</title>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
		<link rel="stylesheet" type="text/css" href="css/boors.css"/>
	</head>
	<body >
		<div class="container">
	        <div class="col-md-6 col-md-offset-3 vcenter">          
	            <form name="loginForm" class="form-signin" method="post" action="j_security_check">
	                <span class="reauth-email"></span>
	                <input name="j_username" type="text" class="form-control" placeholder="User id">
	                <input name="j_password" type="password" class="form-control" placeholder="Password">
	                <button class="btn btn-lg btn-primary btn-block btn-signin" type="submit">Sign in</button>
	            </form>
	            <a href="/boors/signup.html">SignUp</a>
	            <c:if test="${not empty param.hasError}">
					<p style="color:red">Unknown username or password<p>
				</c:if>
	        </div>
	    </div>
	</body>
</html>

