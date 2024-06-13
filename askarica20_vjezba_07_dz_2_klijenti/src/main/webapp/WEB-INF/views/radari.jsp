<%@page import="edu.unizg.foi.nwtis.askarica20.vjezba_08_dz_3.podaci.Radar"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page
	import="java.util.List,java.util.Date,java.text.SimpleDateFormat,edu.unizg.foi.nwtis.askarica20.vjezba_08_dz_3.podaci.Radar"%>
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
	<h1>REST MVC - Pregled radara</h1>
	<ul>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/kazne/pocetak">Početna
				stranica</a></li>
	</ul>
	<br />
	<table>
		<tr>
			<th>Id
			<th>AdresaRadara</th>
			<th>mreznaVrataRadara</th>
			<th>gpsSirina</th>
			<th>gpsDuzina</th>
			<th>maksUdaljenost</th>
		</tr>
		<%
		int i = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
		List<Radar> radari = (List<Radar>) request.getAttribute("radari");
		for (Radar k : radari) {
		  i++;
		 // Date vrijeme = new Date(k.getVrijemeKraj() * 1000);
		%>
		<tr>
			<td><a
				href="${pageContext.servletContext.contextPath}/mvc/radari/ispisRadaraPoId?id=<%= k.getId() %>"><%=k.getId()%></a></td>
			<td><%=k.getAdresaRadara()%></td>
			<td><%=k.getMreznaVrataRadara()%></td>
			<td><%=k.getGpsSirina()%></td>
			<td><%=k.getGpsDuzina()%></td>
			<td><%=k.getMaksUdaljenost()%></td>
		</tr>
		<%
		}
		%>
	</table>
</body>
</html>
