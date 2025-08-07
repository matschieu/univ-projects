import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;



public class AchatVente extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		if (session == null) { res.sendRedirect(HTMLCode.getIdentPath()); return; }
		String id = (String)session.getAttribute("id");
		String role_ = (String)session.getAttribute("role");
		if (session == null || role_ == null || "".equals(role_)) { res.sendRedirect(HTMLCode.getIdentPath()); return; }
		char role = role_.charAt(0);

		try {
			HTMLCode html = doMkPage(req.getParameter("market"));
			if(html == null) return;
			html.setMenuContent( role=='m'? html.getMenuMaker() : html.getMenuUser());
			html.appendInfoContent(html.getPorteMonnaie(id,role));
			res.setContentType("text/html");
			res.getWriter().println( html.getAllHTMLCode() );
		} catch(InvalidMarketException e) {
			res.sendRedirect("./ListeMarche");
			return;
		}

	}

	class InvalidMarketException extends Exception {}


	protected HTMLCode doMkPage(String market) throws InvalidMarketException {
		HTMLCode html = new HTMLCode("Passez-commande !");
		if(market == null || "".equals(market))
			throw new InvalidMarketException();

		String libplus, libmoins, createur;
		java.sql.Date date_fin;
		{ Connection con = null;
			try {
				con = BourseInfo.getInstance().getDBConnection();
				Statement stmt = con.createStatement();
				ResultSet rs   = stmt.executeQuery("SELECT * FROM Marches WHERE alias = '"+market+"'");
				if(!rs.next())
					throw new InvalidMarketException();
				libplus  = rs.getString("libelle_positif");
				libmoins = rs.getString("libelle_negatif");
				createur = rs.getString("createur");
				date_fin = rs.getDate("date_fin");

				rs = stmt.executeQuery("select * from offres where marche = '"+market+"' order by prix");
				String achatsPlus = "";
				String ventesPlus = "";
				String achatsMoins = "";
				String ventesMoins = "";
				while(rs.next()) {
					int quantite = rs.getInt("quantite");
					int prix = rs.getInt("prix");
					boolean achat = rs.getBoolean("achat");
					boolean positif = rs.getBoolean("positif");
					if (achat && positif) achatsPlus += "" + quantite + " bons à " + prix + "<br />";
					else if (!achat && positif) ventesPlus += "" + quantite + " bons à " + prix + "<br />";
					else if (achat && !positif) achatsMoins += "" + quantite + " bons à " + prix + "<br />";
					else if (!achat && !positif) ventesMoins += "" + quantite + " bons à " + prix + "<br />";
				}
				
				html.appendPageContent("<table border=\"1\" align=\"center\"><tr><td valign=\"top\">");

				html.appendPageContent("<table border=\"0\"><th colspan=\"2\">"+libplus+"</th><tr>");
				html.appendPageContent("<td>Achats</td><td>Ventes</td>");
				html.appendPageContent("</tr><tr>");
				html.appendPageContent("<td>" + achatsPlus + "</td>");
				html.appendPageContent("<td>" + ventesPlus + "</td>");
				html.appendPageContent("</tr></table>");

				html.appendPageContent("</td><td valign=\"top\">");

				html.appendPageContent("<table border=\"0\"><th colspan=\"2\">"+libmoins+"</th><tr>");
				html.appendPageContent("<td>Achats</td><td>Ventes</td>");
				html.appendPageContent("</tr><tr>");
				html.appendPageContent("<td>" + achatsMoins + "</td>");
				html.appendPageContent("<td>" + ventesMoins + "</td>");
				html.appendPageContent("</tr></table>");

				html.appendPageContent("</td></tr></table><br /><br/>");

				html.appendPageContent("<form action=\"./PasserOrdre?marche=" + market + "\" method=\"post\">");
				html.appendPageContent("<input name=\"prix\" class=\"form\" size=\"4\" /> Crédits<br/>");
				html.appendPageContent("<input name=\"quantite\" class=\"form\" size=\"4\" /> Bons<br/><br/>");
		
				html.appendPageContent("<input type=\"radio\" name=\"type\" value=\"a\">Acheter<br />");
				html.appendPageContent("<input type=\"radio\" name=\"type\" value=\"v\">Vendre<br /><br/>");
		
				html.appendPageContent("<input type=\"radio\" name=\"signe\" value=\"+\">" + libplus + "<br />");
				html.appendPageContent("<input type=\"radio\" name=\"signe\" value=\"-\">" + libmoins + "<br /><br/>");
		
				html.appendPageContent("<input type=\"submit\" /><br/>");
				html.appendPageContent("</form>");

			} catch(SQLException e) {
				System.err.println(e);
				return null;
			} finally {			try { con.close(); } catch(Exception e) {} }
		}
		return html;
	}

}
