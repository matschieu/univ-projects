
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ListeMarche extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		HTMLCode html = new HTMLCode("Inscription");
		res.setContentType("text/html");
		HttpSession session = req.getSession(false);
		String id = "";
		char role = ' ';
		if (session == null) res.sendRedirect(html.getIdentPath());
		else {
			id = (String)session.getAttribute("id");
			role = ((String)session.getAttribute("role")).charAt(0);
			if (role == BourseInfo.MAKER_ROLE) html.setMenuContent(html.getMenuMaker());
			else html.setMenuContent(html.getMenuUser());
			html.appendInfoContent(html.getPorteMonnaie(id, role));
		}
		html.appendPageContent("<div class=\"titre1\">Liste des marchés</div><br /><br />");
		html.appendPageContent("<div class=\"texteCentre\">");
		html.appendPageContent("<form method=\"post\" action=\"./ListeMarche\">");
		html.appendPageContent("Saisir l'alias du marché à afficher");
		html.appendPageContent("<input type=\"text\" name=\"al\" class=\"form\" size=\"3\" />");
		html.appendPageContent("(* affiche tout)");
		html.appendPageContent("</form>");
		if(role == 'm')
			html.appendPageContent("<div class=\"texteDroite texteGras\" style=\"padding-bottom:20px\"><a href=\"./ListeMarche?aut="+id+"\">Mes march&eacute;s</a></div>");
		try {
			Connection con = BourseInfo.getInstance().getDBConnection();
			Statement stmt = con.createStatement();
			ResultSet rs;
			String author = req.getParameter("aut");
			String alias = req.getParameter("al");
			if (alias != null && alias.equals("*")) alias = null;
			if (alias != null)
				rs = stmt.executeQuery("select alias, libelle_positif, date_fin, createur from Marches where alias='" + alias + "'");
			else if (author != null)
				rs = stmt.executeQuery("select alias, libelle_positif, date_fin, createur from Marches where createur='" +
										author + "' order by date_fin");
			else
				rs = stmt.executeQuery("select alias, libelle_positif, date_fin, createur from Marches order by date_fin");
			int idx = 0;
			while (rs.next()) {
				Calendar c = new GregorianCalendar(TimeZone.getTimeZone("Europe/Paris"));
				int day = c.get(Calendar.DAY_OF_MONTH );
				int month = c.get(Calendar.MONTH) + 1;
				int year = c.get(Calendar.YEAR);
				String curDate = year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);
				alias = rs.getString(1);
				String libellePos = rs.getString(2);
				String date = rs.getString(3);
				author = rs.getString(4);
				if (!curDate.equals(date)) {
					if (idx == 0) {
						html.appendPageContent("<table cellspacing=\"0\" class=\"texte\" style=\"width: 620px\">");
						html.appendPageContent("<tr class=\"texteGras\">");
						html.appendPageContent("<td style=\"width: 110px\">Alias</td>");
						html.appendPageContent("<td style=\"width: 290px\">Marché</td>");
						html.appendPageContent("<td style=\"width: 110px\">Date de fin</td>");
						html.appendPageContent("<td style=\"width: 110px\">Créateur</td>");
						html.appendPageContent("</tr>");
					}
					String color = "fff";
					if (idx++ % 2 == 0) color = "eee";
					html.appendPageContent("<tr style=\"background : #" + color +";\">");
					html.appendPageContent("<td><a href=\"./AchatVente?market=" + alias + "\" target=\"_parent\" class=\"lien\">" + 
											alias + "</a></td>");
					html.appendPageContent("<td><a href=\"./AchatVente?market=" + alias + "\" target=\"_parent\" class=\"lien\">" + 
											libellePos + "</a></td>");
					html.appendPageContent("<td>" + date + "</td>");
					html.appendPageContent("<td><a href=\"./ListeMarche?aut=" + author + "\" target=\"_parent\" class=\"lien\">" + 
											author + "</a></td>");
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
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		html.appendPageContent("</div>");
		out.println(html.getAllHTMLCode());
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException {
			String alias = req.getParameter("al");
			res.sendRedirect("./AchatVente?market="+alias);
	}

}
