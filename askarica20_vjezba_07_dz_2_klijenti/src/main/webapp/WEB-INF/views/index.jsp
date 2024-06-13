<%@page import="java.util.Map"%>
<%@page import="java.util.Enumeration"%>
<%@page
	import="edu.unizg.foi.nwtis.askarica20.vjezba_08_dz_3.kontroler.SlusacKonteksta"%>
<%@page import="jakarta.servlet.ServletContext"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>REST MVC - Početna stranica</title>
</head>
<body>		
	<h1>REST MVC - Početna stranica</h1>
	
	<h2>Meni simulacije:</h2>
	<%
	ServletContext context = application;
	String imeDatUpdateano = (String) context.getAttribute("datoteka");

	%>
	<p>
		Posljednje odabrana datoteka za simulaciju: 
		<%=imeDatUpdateano%>
	</p>
	<ul>
		<li>
			<h2>Pokretanje simulacije</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/simulacije/pokreniSimulaciju">
				<table>
					<tr>
						<td>Naziv datoteke (NWTiS_DZ1_V1.csv, NWTiS_DZ1_V2.csv ili NWTiS_DZ1_V3.csv):</td>
						<td><input name="nazivDatoteke" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Id vozila:</td>
						<td><input name="idVozila" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Trajanje sekunde:</td>
						<td><input name="trajanjeSek" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Trajanje pauze:</td>
						<td><input name="trajanjePauze" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value="Pokreni simulaciju"></td>
					</tr>
				</table>
			</form>
		</li>
		<li>
			<h2>Pretraživanje svih simulacija/voznji</h2> <a
			href="${pageContext.servletContext.contextPath}/mvc/simulacije/ispisSvihSimulacija">Ispis
				svih simulacija/voznji</a>
		</li>
		<li>
			<h2>Pretraživanje simulacija/voznji u intervalu</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/simulacije/ispisSimulacijaUIntervalu">
				<table>
					<tr>
						<td>Od vremena:</td>
						<td><input name="odVremena" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Do vremena:</td>
						<td><input name="doVremena" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value=" Dohvati simulacije/voznje"></td>
					</tr>
				</table>
			</form>
		</li>
		<li>
			<h2>Pretraživanje simulacija/voznji po id-u vozila</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/simulacije/ispisSimulacijaPoIdVozila">
				<table>
					<tr>
						<td>Id vozila:</td>
						<td><input name="idVozila" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit"
							value=" Dohvati simulacije/voznje za ovo vozilo "></td>
					</tr>
				</table>
			</form>
		</li>
		<li>
			<h2>Pretraživanje simulacija/voznji po id-u vozila i u intervalu</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/simulacije/ispisSimulacijaPoIdVozilaInterval">
				<table>
					<tr>
						<td>Id vozila:</td>
						<td><input name="idVozila" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Od vremena:</td>
						<td><input name="odVremena" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Do vremena:</td>
						<td><input name="doVremena" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit"
							value="Dohvati simulacije/voznje po id-u u intervalu"></td>
					</tr>
				</table>
			</form>
		</li>

		<li>
			<h2>Dodavanje simulacije/vožnje</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/simulacije/dodavanjeSimulacije">
				<table>
					<tr>
						<td>Id:</td>
						<td><input name="id" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Broj:</td>
						<td><input name="broj" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Vrijeme:</td>
						<td><input name="vrijeme" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Brzina:</td>
						<td><input name="brzina" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Snaga:</td>
						<td><input name="snaga" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Struja:</td>
						<td><input name="struja" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Visina:</td>
						<td><input name="visina" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>GPS Brzina:</td>
						<td><input name="gpsBrzina" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Temperatura vozila:</td>
						<td><input name="tempVozila" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Postotak baterije:</td>
						<td><input name="postotakBaterija" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Napon baterije:</td>
						<td><input name="naponBaterija" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Kapacitet baterije:</td>
						<td><input name="kapacitetBaterija" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Temperatura baterije:</td>
						<td><input name="tempBaterija" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Preostali kilometri:</td>
						<td><input name="preostaloKm" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Ukupni kilometri:</td>
						<td><input name="ukupnoKm" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>GPS širina:</td>
						<td><input name="gpsSirina" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>GPS dužina:</td>
						<td><input name="gpsDuzina" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value="Dodaj u bazu"></td>
					</tr>
				</table>
			</form>
		</li>

	</ul>

	<h2>Meni radari:</h2>
	<ul>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/radari/ispisRadara">Ispis
				svih radara </a></li>
		<li>
			<h2>Pretraživanje radara po id-u</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/radari/ispisRadaraPoIdu">
				<table>
					<tr>
						<td>Id radara:</td>
						<td><input name="idRadara" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value="Dohvati radar"></td>
					</tr>
				</table>
			</form>
		</li>
		<li>
			<h2>Brisanje radara po id-u</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/radari/brisanjeRadaraPoId">
				<table>
					<tr>
						<td>Id radara:</td>
						<td><input name="idRadara" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value="Obrisi radar po id-u"></td>
					</tr>
				</table>
			</form>
		</li>
		<li>
			<h2>Brisanje svih radara</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/radari/brisanjeSvihRadara">
				<table>

					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value="Obrisi sve radare"></td>
					</tr>
				</table>
			</form>
		</li>
		<li>
			<h2>Resetiranje radara</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/radari/resetiranjeRadara">
				<table>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value="Resetiraj radare"></td>
					</tr>
				</table>
			</form>
		</li>
		<li>
			<h2>Provjera radara po id-u</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/radari/provjeravanjeRadara">
				<table>
					<tr>
						<td>Id radara:</td>
						<td><input name="idRadara" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value="Provjeri radar po id-u"></td>
					</tr>
				</table>
			</form>
		</li>
	</ul>
	
	<h2>Meni pracene voznje:</h2>
	<ul>
		<li>
			<h2>Pretraživanje svih vozila/pracenih voznji</h2> <a
			href="${pageContext.servletContext.contextPath}/mvc/vozila/ispisPraceneVoznje">Ispis
				sve pracene voznje </a>
		</li>
		<li>
			<h2>Pretraživanje vozila/pracene voznje u intervalu</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/vozila/ispisPraceneVoznjeUIntervalu">
				<table>
					<tr>
						<td>Od vremena:</td>
						<td><input name="odVremena" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Do vremena:</td>
						<td><input name="doVremena" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit"
							value=" Dohvati vozila/pracene voznje "></td>
					</tr>
				</table>
			</form>
		</li>
		<li>
			<h2>Pretraživanje pracenih voznji po id-u vozila</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/vozila/ispisPracenihVoznjiPoIdVozila">
				<table>
					<tr>
						<td>Id vozila:</td>
						<td><input name="idVozila" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit"
							value=" Dohvati pracene voznje za ovo vozilo "></td>
					</tr>
				</table>
			</form>
		</li>
		<li>
			<h2>Pretraživanje pracenih voznji po id-u vozila i u intervalu</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/vozila/ispisPracenihVoznjiPoIdVozilaInterval">
				<table>
					<tr>
						<td>Id vozila:</td>
						<td><input name="idVozila" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Od vremena:</td>
						<td><input name="odVremena" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Do vremena:</td>
						<td><input name="doVremena" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit"
							value="Dohvati pracene voznje u intervalu"></td>
					</tr>
				</table>
			</form>
		</li>
		<li>
			<h2>Startanje vozila po id-u</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/vozila/startanjeVozila">
				<table>
					<tr>
						<td>Id vozila:</td>
						<td><input name="idVozila" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value="Startaj vozilo po id-u"></td>
					</tr>
				</table>
			</form>
		</li>
		<li>
			<h2>Stopiranje vozila po id-u</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/vozila/stopanjeVozila">
				<table>
					<tr>
						<td>Id vozila:</td>
						<td><input name="idVozila" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value="Stopiraj vozilo po id-u"></td>
					</tr>
				</table>
			</form>
		</li>

		<li>
			<h2>Dodavanje pracene voznje</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/vozila/dodavanjeVoznje">
				<table>
					<tr>
						<td>Id:</td>
						<td><input name="id" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Broj:</td>
						<td><input name="broj" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Vrijeme:</td>
						<td><input name="vrijeme" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Brzina:</td>
						<td><input name="brzina" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Snaga:</td>
						<td><input name="snaga" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Struja:</td>
						<td><input name="struja" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Visina:</td>
						<td><input name="visina" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>GPS Brzina:</td>
						<td><input name="gpsBrzina" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Temperatura vozila:</td>
						<td><input name="tempVozila" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Postotak baterije:</td>
						<td><input name="postotakBaterija" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Napon baterije:</td>
						<td><input name="naponBaterija" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Kapacitet baterije:</td>
						<td><input name="kapacitetBaterija" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Temperatura baterije:</td>
						<td><input name="tempBaterija" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Preostali kilometri:</td>
						<td><input name="preostaloKm" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Ukupni kilometri:</td>
						<td><input name="ukupnoKm" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>GPS širina:</td>
						<td><input name="gpsSirina" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>GPS dužina:</td>
						<td><input name="gpsDuzina" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value="Dodaj u bazu"></td>
					</tr>
				</table>
			</form>
		</li>
	</ul>

	<h2>Meni kazne:</h2>
	<ul>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/kazne/ispisKazni">Ispis
				svih kazni i kazni prema vozilu</a></li>

		<li>
			<h2>Pretraživanje kazne po rednom broju kazne</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/kazne/pretrazivanjeKaznePoIdu">
				<table>
					<tr>
						<td>Redni broj kazne:</td>
						<td><input name="idKazne" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value=" Dohvati kaznu "></td>
					</tr>
				</table>
			</form>
		</li>

		<li>
			<h2>Pretraživanje kazni u intervalu</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/kazne/pretrazivanjeKazni">
				<table>
					<tr>
						<td>Od vremena:</td>
						<td><input name="odVremena" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Do vremena:</td>
						<td><input name="doVremena" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value=" Dohvati kazne "></td>
					</tr>
				</table>
			</form>
		</li>
		<li>
			<h2>Pretraživanje kazne po id-u vozila</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/kazne/ispisKazniPoIdVozila">
				<table>
					<tr>
						<td>Id vozila:</td>
						<td><input name="idVozila" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit"
							value=" Dohvati kazne za ovo vozilo "></td>
					</tr>
				</table>
			</form>
		</li>

		<li>
			<h2>Pretraživanje kazne po id-u vozila i u intervalu</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/kazne/ispisKazniPoIdVozilaInterval">
				<table>
					<tr>
						<td>Id vozila:</td>
						<td><input name="idVozila" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Od vremena:</td>
						<td><input name="odVremena" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Do vremena:</td>
						<td><input name="doVremena" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value="Dohvati kazne"></td>
					</tr>
				</table>
			</form>
		</li>
	</ul>
</body>
</html>