
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class AjouterMaker extends HttpServlet {

	private static final int ID_MAX_SIZE = 20;
	private static final int MDP_MAX_SIZE = 36;
	private static final int NOM_MAX_SIZE = 30;
	private static final int PRENOM_MAX_SIZE = 30;

	public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		HTMLCode html = new HTMLCode("Inscription");
		res.setContentType("text/html");
		String id = "";
		HttpSession session = req.getSession(false);
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
		boolean display = true;
		boolean displayValue = false;
		String login = "";
		html.appendPageContent("<div class=\"titre1\">Ajout d'un market-maker</div><br /><br />");
		if (req.getParameter("liste") != null) {
			try {
				Connection con = BourseInfo.getInstance().getDBConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("select login from Roles where role='u'");
				html.appendPageContent("<div class=\"texteCentre\">");
				html.appendPageContent("<div class=\"texteGras\">Liste des utilisateurs : </div>");
				while(rs.next()) html.appendPageContent(rs.getString(1) + "<br />");
				html.appendPageContent("</div><br /><br />");
			}
			catch (SQLException e) {
				html.appendPageContent("<div class=\"erreur\">Erreur : Impossible d'accéder à la base de données</div>");
				System.out.println(e.getMessage());
			}
			catch(Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		}
		if (req.getParameter("ok") != null) {
			try {
				login = req.getParameter("id");
				Connection con = BourseInfo.getInstance().getDBConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("select role from Roles where login='" + login + "'");
				if (rs.next()) {
					if (!rs.getString(1).equals("m")) {
						stmt.executeUpdate("update Roles set role='m' where login='" + login + "'");
						html.appendPageContent("<div class=\"texteCentre\">");
						html.appendPageContent("Modification du statut du membre correctement effectuée !");
						html.appendPageContent("</div>");
						display = false;
					}
					else {
						html.appendPageContent("<div class=\"erreur\">");
						html.appendPageContent("Ce membre est déjà market-maker");
						html.appendPageContent("</div>");						
					}
				}
				else {
					html.appendPageContent("<div class=\"erreur\">");
					html.appendPageContent("Aucun utilisateur correspondant n'a été trouvé");
					html.appendPageContent("</div>");
				}
			}
			catch (SQLException e) {
				html.appendPageContent("<div class=\"erreur\">Erreur : Impossible d'accéder à la base de données</div>");
				System.out.println(e.getMessage());
			}
			catch(Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
			html.appendPageContent("<br />");
		}
		if (display) {
			html.appendPageContent("Pour ajouter un market-maker au site " + html.getSiteTitle()
								+ ", veuillez saisir l'identifiant d'un utilisateur (il doit être déjà inscrit) :<br />");
			html.appendPageContent("<div class=\"texteCentre\">");
			html.appendPageContent("<form method=\"post\" action=\"./AjouterMaker\">");
			html.appendPageContent("<input type=\"hidden\" name=\"ok\" value=\"1\" />");
			html.appendPageContent("Identifiant : ");
			html.appendPageContent("<input type=\"text\" name=\"id\" " 
								+ (displayValue ? "value=\"" + login + "\"": "")
								+ " class=\"form\" />");
			html.appendPageContent("<input type=\"submit\" name=\"button\" value=\"Ajouter\" class=\"form\" />");
			html.appendPageContent("</form>");
			html.appendPageContent("<a href=\"./AjouterMaker?liste=1\" class=\"lien\" target=\"_parent\">Liste des utilisateurs</a>");
			html.appendPageContent("</div>");
		}
		out.println(html.getAllHTMLCode());
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException { doGet(req, res); }

}
