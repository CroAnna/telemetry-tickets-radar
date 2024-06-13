<%@page
	import="edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci.Vozilo"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page
	import="java.util.List, java.util.Date, java.text.SimpleDateFormat, edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci.Kazna"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>REST MVC - Pregled pracenih voznji - RestKlijentVozila</title>
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
	<h1>REST MVC - Pregled pracenih voznji - RestKlijentVozila</h1>
	<ul>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/kazne/pocetak">Početna
				stranica</a></li>
	</ul>
	<br />
	<table>
		<tr>
			<th>id</th>
			<th>Broj
			<th>Brzina</th>
			<th>gpsBrzina</th>
			<th>gpsDuzina</th>
			<th>gpsSirina</th>
			<th>kapacitetBaterija</th>
			<th>naponBaterija</th>
			<th>postotakBaterija</th>
			<th>preostaloKm</th>
			<th>snaga</th>
			<th>struja</th>
			<th>tempBaterija</th>
			<th>tempVozila</th>
			<th>ukupnoKm</th>
			<th>visina</th>			
			<th>vrijeme</th>
		</tr>
		<%
		int i = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
		List<Vozilo> vozila = (List<Vozilo>) request.getAttribute("vozila");
		for (Vozilo k : vozila) {
		  i++;
		  //	  Date vrijeme = new Date(k.getVrijemeKraj() * 1000);
		%>
		<tr>
			<td class="desno"><%=k.getId()%></td>
			<td><%=k.getBroj()%></td>
			<td><%=k.getBrzina()%></td>
			<td><%=k.getGpsBrzina()%></td>
			<td><%=k.getGpsDuzina()%></td>
			<td><%=k.getGpsSirina()%></td>
			<td><%=k.getKapacitetBaterija()%></td>
			<td><%=k.getNaponBaterija()%></td>
			<td><%=k.getPostotakBaterija()%></td>
			<td><%=k.getPreostaloKm()%></td>
			<td><%=k.getSnaga()%></td>
			<td><%=k.getStruja()%></td>
			<td><%=k.getTempBaterija()%></td>
			<td><%=k.getTempVozila()%></td>
			<td><%=k.getUkupnoKm()%></td>
			<td><%=k.getVisina()%></td>
			<td><%=k.getVrijeme()%></td>
		</tr>
		<%
		}
		%>
	</table>
</body>
</html>
