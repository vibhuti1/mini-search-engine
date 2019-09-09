<html>
<body>
<h2>Here are your results!</h2>
<!--<%
String myText = request.getParameter("query");
%>
<b>myText is <%= myText %></b> -->
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="output.doPost" %>  <!-- //importing java class -->
<% 
doPost dp = new doPost();  /*  creating reference for my java class */
String inputvalue = request.getParameter("query"); 

HashMap<Object,Object> output = dp.getQueryResult(inputvalue);  /* calling java method */

Set set = output.entrySet();
Iterator iterator = set.iterator();
int id =1;
while (iterator.hasNext()) {
Map.Entry mentry = (Map.Entry) iterator.next();
%>
<br />
<br />
<li style ="margin-left:50px;">
<a href="<%=mentry.getKey()%>/people"><%=mentry.getKey()%></a></li>
<br />
<li style = "list-style: none; margin-left:70px">
<%
out.println(mentry.getValue()); 
}
%>
</li>
</body>
</html>
