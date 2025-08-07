
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class TerminerMarche extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		HTMLCode html = new HTMLCode("Inscription");
		HttpSession session = req.getSession(false);
		String id = "";
		if (session == null) res.sendRedirect(html.getIdentPath());
		else {
			id = (String)session.getAttribute("id");
			char role = ((String)session.getAttribute("role")).charAt(0);
			if (role != BourseInfo.MAKER_ROLE) res.sendRedirect(html.getIdentPath());
			else {
				html.setMenuContent(html.getMenuMaker());
				html.appendInfoContent(html.getPorteMonnaie(id, role));
			}
		}
		res.setContentType("text/html");
		html.appendPageContent("<div class=\"titre1\">Terminer des marchés</div><br /><br />");
		html.appendPageContent("<div class=\"texteCentre\">");
		try {
			String param = req.getParameter("term");
			int term = Integer.parseInt(param == null ? "0" : param);
			Connection con = BourseInfo.getInstance().getDBConnection();
			Statement stmt = con.createStatement();
			Statement stmt2 = con.createStatement();
			ResultSet rs;
			if (term == 1 || term == 2) {
				String alias = req.getParameter("al");
				boolean sens = true;
				if (term == 2) sens = false;
				rs = stmt.executeQuery("select proprietaire, quantite, positif from Bons where marche='" + alias + "'");
				while (rs.next()) {
					String user = rs.getString(1);
					int quantity = rs.getInt(2);
					boolean userSens = rs.getBoolean(3);
					if (userSens == sens) {
						ResultSet rs1 = stmt2.executeQuery("select solde, marches_gagnant, gain from Utilisateurs " +
													"where login='" + user + "'");
						rs1.next();
						int solde = rs1.getInt(1);
						int gagnant = rs1.getInt(2);
						int gain = rs1.getInt(3);
						stmt2.executeUpdate("update Utilisateurs set solde=" + (solde + quantity * 100)+"where login='" + user + "'" );
						stmt2.executeUpdate("update Utilisateurs set marches_gagnant=" + (gagnant + 1)+"where login='" + user + "'" );
						stmt2.executeUpdate("update Utilisateurs set gain=" + (gain + quantity * 100)+"where login='" + user + "'" );
					}
				}
				rs = stmt.executeQuery("select passeur, prix, quantite, positif from Offres where marche='" + alias + "'");
				while (rs.next()) {
					String user = rs.getString(1);
					int price = rs.getInt(2);
					int quantity = rs.getInt(3);
					boolean userSens = rs.getBoolean(4);
					if (userSens == sens) {
						ResultSet rs1 = stmt2.executeQuery("select solde from Utilisateurs where login='" + user + "'");
						int solde = rs1.getInt(1);
						stmt2.executeUpdate("update Utilisateurs where login='" + user + "' set solde=" + (solde + quantity * price));
					}
				}
				stmt.executeUpdate("delete from Bons where marche='" + alias + "'");
				stmt.executeUpdate("delete from Offres where marche='" + alias + "'");
				stmt.executeUpdate("delete from Marches where alias='" + alias + "'");
			}
			rs = stmt.executeQuery("select alias, libelle_positif, date_fin, createur from Marches where createur='" + id + "'");
			int idx = 0;
			while (rs.next()) {
				Calendar c = new GregorianCalendar(TimeZone.getTimeZone("GMT+1"));
				int day = c.get(Calendar.DAY_OF_MONTH );
				int month = c.get(Calendar.MONTH) + 1;
				int year = c.get(Calendar.YEAR);
				String curDate = year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);
				String alias = rs.getString(1);
				String libellePos = rs.getString(2);
				String date = rs.getString(3);
				String author = rs.getString(4);
				if (curDate.equals(date)) {
					String color = "fff";
					if (idx == 0) {
						html.appendPageContent("<table cellspacing=\"0\" class=\"texte\" style=\"width: 620px\">");
						html.appendPageContent("<tr class=\"texteGras\">");
						html.appendPageContent("<td style=\"width: 100px\">Alias</td>");
						html.appendPageContent("<td style=\"width: 180px\">Marché</td>");
						html.appendPageContent("<td style=\"width: 110px\">Date de fin</td>");
						html.appendPageContent("<td style=\"width: 100px\">Créateur</td>");
						html.appendPageContent("<td style=\"width: 130px\">Information</td>");
						html.appendPageContent("</tr>");
					}
					if (idx++ % 2 == 0) color = "eee";
					html.appendPageContent("<tr style=\"background : #" + color +";\">");
					html.appendPageContent("<td>" + alias + "</td>");
					html.appendPageContent("<td>" + libellePos + "</td>");
					html.appendPageContent("<td>" + date + "</td>");
					html.appendPageContent("<td>" + author + "</td>");
					html.appendPageContent("<td><a href=\"./TerminerMarche?al=" + alias + "&term=1\" target=\"_parent\" class=\"lien\">" + 
										"Vérifiée</a> | ");
					html.appendPageContent("<a href=\"./TerminerMarche?al=" + alias + "&term=2\" target=\"_parent\" class=\"lien\">" + 
										"Démentie</a></td>");
					html.appendPageContent("</tr>");
				}
			}
			if (idx == 0) html.appendPageContent("Aucun résultat trouvé");
			else html.appendPageContent("</table>");
			con.close();
		}
		catch (SQLException e) {
			html.appendPageContent("<div class=\"erreur\">Erreur : Impossible d'accéder à la base de données</div>");
			System.out.println(e.getMessage());
			e.printStackTrace();
			//e.printStackTrace(out);
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		html.appendPageContent("</div>");
		out.println(html.getAllHTMLCode());
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException { doGet(req, res); }

}
