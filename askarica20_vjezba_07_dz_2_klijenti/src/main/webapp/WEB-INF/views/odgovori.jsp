<%@page import="edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci.Radar"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page
	import="java.util.List, java.util.Date, java.text.SimpleDateFormat, edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci.Radar"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>REST MVC - Pregled radara</title>
<style type="text/css">
table, th, td {
	border: 1px solid;
}

th {
	text-align: center;
	font-weight: bold;
}

.desno {
	text-align: right;
}
</style>
</head>
<body>
	<h1>REST MVC - Pregled odgovora</h1>
	<ul>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/kazne/pocetak">Početna
				stranica</a></li>
	</ul>
	<br />
	  <div>
    <%
      String jsonString = (String) request.getAttribute("odg");
      if (jsonString != null && !jsonString.isEmpty()) {
    %>
      <p><%= jsonString %></p>
    <%
      } else {
    %>
      <p>Nema JSON poruke.</p>
    <%
      }
    %>
  </div>
</body>
</html>
