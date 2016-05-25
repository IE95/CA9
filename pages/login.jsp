<html>
	<head>
		<title>Login</title>
	</head>
	<body>
		Please enter your username and password to access the requested page: <p>
		<form name="loginForm" method="post" action="j_security_check">
			<table border=0>
				<tr>
					<td>Username:</td>
					<td><input name="j_username" type="text" size="20"/></td>
				</tr>
				<tr>
					<td>Password</td>
					<td><input name="j_password" type="password" size="20"/></td>
				</tr>
				<tr>
					<td colspan=2 align="center">
					<input name="Submit" type="submit" value='Login'/>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>

