
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class HallOfFame extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		HTMLCode html = new HTMLCode("Inscription");
		HttpSession session = req.getSession(false);
		if (session != null) {
			String id = (String)session.getAttribute("id");
			char role = ((String)session.getAttribute("role")).charAt(0);
			if (role == BourseInfo.MAKER_ROLE) html.setMenuContent(html.getMenuMaker());
			else html.setMenuContent(html.getMenuUser());
			html.appendInfoContent(html.getPorteMonnaie(id, role));
		}
		else {
			html.setMenuContent(html.getMainMenu());
			html.appendInfoContent(html.getIdent());
		}
		res.setContentType("text/html");
		html.appendPageContent("<div class=\"titre1\">Hall Of Fame<br />(selon le gain)</div><br /><br />");
		html.appendPageContent("<table cellspacing=\"0\" class=\"texte\" style=\"width: 620px\">");
		html.appendPageContent("<tr class=\"texteGras\">");
		html.appendPageContent("<td style=\"width: 160px\">Pseudo</td>");
		html.appendPageContent("<td style=\"width: 110px\">Solde</td>");
		html.appendPageContent("<td style=\"width: 110px\">Marchés</td>");
		html.appendPageContent("<td style=\"width: 140px\">Marchés gagnants</td>");
		html.appendPageContent("<td style=\"width: 110px\">Gain</td>");
		html.appendPageContent("</tr>");
		try {
			Connection con = BourseInfo.getInstance().getDBConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select login, solde, total_marches, marches_gagnant, gain from Utilisateurs order by gain desc");
			int idx = 0;
			while (rs.next()) {
				String login = rs.getString(1);
				int solde = rs.getInt(2);
				int total_marches = rs.getInt(3);
				int marches_gagnant = rs.getInt(4);
				int gain = rs.getInt(5);
				String color = "fff";
				if (idx++ % 2 == 0) color = "eee";
				html.appendPageContent("<tr style=\"background : #" + color +";\">");
				html.appendPageContent("<td>" + login + "</td>");
				html.appendPageContent("<td>" + solde + "</td>");
				html.appendPageContent("<td>" + total_marches + "</td>");
				html.appendPageContent("<td>" + marches_gagnant + "</td>");
				html.appendPageContent("<td>" + gain + "</td>");
				html.appendPageContent("</tr>");
			}
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
		html.appendPageContent("</table>");
		out.println(html.getAllHTMLCode());
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException { doGet(req, res); }

}
