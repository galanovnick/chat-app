<%--
  Created by IntelliJ IDEA.
  User: nick
  Date: 25.07.16
  Time: 14:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>
  <form action="/register" method="post">
    <input type="text" name="username" placeholder="Username"/>
    <input type="password" name="password" placeholder="Password"/>
    <input type="password" name="passwordConfirm" placeholder="Password confirm"/>
    <input type="submit"/>
  </form>
  </body>
</html>
