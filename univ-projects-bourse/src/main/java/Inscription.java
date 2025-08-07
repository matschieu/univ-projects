
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Inscription extends HttpServlet {

	private static final int ID_MAX_SIZE = 20;
	private static final int MDP_MAX_SIZE = 36;
	private static final int NOM_MAX_SIZE = 30;
	private static final int PRENOM_MAX_SIZE = 30;

	public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		HTMLCode html = new HTMLCode("Inscription");
		res.setContentType("text/html");
		boolean display = true;
		boolean displayValue = false;
		String id = "";
		String pwd = "";
		String name = "";
		String firstname = "";
		int day = 0;
		int month = 0;
		int year = 0;
		html.setMenuContent(html.getMainMenu());
		html.appendPageContent("<div class=\"titre1\">Inscription</div><br /><br />");
		if (req.getParameter("ok") != null) {
			int valid = 0;
			displayValue = true;
			id = req.getParameter("id");
			pwd = req.getParameter("pwd");
			name = req.getParameter("name");
			firstname = req.getParameter("firstname");
			if (id == null || id.equals("")) {
				html.appendPageContent("<div class=\"erreur\">Erreur : vous n'avez pas entré d'identifiant</div>");
				id = "";
			}
			else if (id.length() > ID_MAX_SIZE) {
				html.appendPageContent("<div class=\"erreur\">Erreur : votre identifiant est trop long</div>");
				id = "";
			}
			else valid++;
			if (pwd == null || pwd.equals("")) {
				html.appendPageContent("<div class=\"erreur\">Erreur : vous n'avez pas entré de mot de passe</div>");
				pwd = "";
			}
			else if (pwd.length() > MDP_MAX_SIZE) {
				html.appendPageContent("<div class=\"erreur\">Erreur : votre mot de passe est trop long</div>");
				pwd = "";
			}
			else valid++;
			if (name == null || name.equals("")) {
				html.appendPageContent("<div class=\"erreur\">Erreur : vous n'avez pas entré de nom</div>");
				name = "";
			}
			else if (name.length() > MDP_MAX_SIZE) {
				html.appendPageContent("<div class=\"erreur\">Erreur : votre nom est trop long</div>");
				name = "";
			}
			else valid++;
			if (firstname == null || firstname.equals("")) {
				html.appendPageContent("<div class=\"erreur\">Erreur : vous n'avez pas entré de prénom</div>");
				firstname = "";
			}
			else if (firstname.length() > MDP_MAX_SIZE) {
				html.appendPageContent("<div class=\"erreur\">Erreur : votre prénom est trop long</div>");
				firstname = "";
			}
			else valid++;
			try {
				Calendar c = new GregorianCalendar(TimeZone.getTimeZone("GMT+1"));
				day = Integer.parseInt(req.getParameter("day"));
				month = Integer.parseInt(req.getParameter("month"));
				year = Integer.parseInt(req.getParameter("year"));
				if (day < 1 || day > 31) throw new NumberFormatException();
				if (month < 1 || month > 12) throw new NumberFormatException();
				if (year < 1900 || year > c.get(Calendar.YEAR) - 18) throw new NumberFormatException();
				valid++;
			}
			catch(NumberFormatException e) {
				html.appendPageContent("<div class=\"erreur\">Erreur : date invalide</div>");
			}
			if (valid == 5) {
				try {
					Connection con = BourseInfo.getInstance().getDBConnection();
					Statement stmt = con.createStatement();
					java.sql.Date date = java.sql.Date.valueOf(year + "-" + month + "-" + day);
					ResultSet rs = stmt.executeQuery("select * from Utilisateurs where login='" + id + "'");
					if (rs.next()) throw new IllegalArgumentException();
					stmt.executeUpdate("insert into Utilisateurs values('" + id + "', '" + pwd +
						"', '" + name + "', '" + firstname + "', '" + date + "', 2000, 0, 0, 0)");
					stmt.executeUpdate("insert into Roles values('u', '" + id + "')");
					con.close();
					html.appendPageContent("<div class=\"texteCentre\">");
					html.appendPageContent("Enregistrement correctement terminé !<br /><br />");
					html.appendPageContent("<a href=\"" + html.getIdentPath() + 
						"\" class=\"lien\" target=\"_parent\">retour à l'accueil</a>");
					html.appendPageContent("</div>");
					display = false;
					res.sendRedirect("./Ident?id="+id+"&pwd="+pwd);
					return;
				}
				catch (SQLException e) {
					html.appendPageContent("<div class=\"erreur\">Erreur : Impossible d'accéder à la base de données</div>");
					System.out.println(e.getMessage());
					pwd = "";
				}
				catch(IllegalArgumentException e) {
					html.appendPageContent("<div class=\"erreur\">Erreur : cet identifiant est déjà utilisé</div>");
					id = pwd = "";
				}
				catch(Exception e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
					pwd = "";
				}
			}
			else pwd = "";
			html.appendPageContent("<br />");
		}
		if (display) {
			html.appendPageContent("Pour vous inscrire au site " + html.getSiteTitle()
								+ ", veuillez remplir le fomulaire suivant :<br />");
			html.appendPageContent("<div class=\"texteCentre\">");
			html.appendPageContent("<form method=\"post\" action=\"./Inscription\">");
			html.appendPageContent("<input type=\"hidden\" name=\"ok\" value=\"1\" />");
			html.appendPageContent("<table>");
			html.appendPageContent("<tr>");
			
			html.appendPageContent("<td class=\"texteDroite\">Identifiant* : </td>");
			html.appendPageContent("<td class=\"texteGauche\"><input type=\"text\" name=\"id\" " 
								+ (displayValue ? "value=\"" + id + "\"": "")
								+ " class=\"form\" /></td>");
			html.appendPageContent("</tr>");
			html.appendPageContent("<tr>");
			html.appendPageContent("<td class=\"texteDroite\">Mot de passe* : </td>");
			html.appendPageContent("<td class=\"texteGauche\"><input type=\"password\" name=\"pwd\" " 
								+ (displayValue ? "value=\"" + pwd + "\"": "")
								+ " class=\"form\" /></td>");
			html.appendPageContent("</tr>");
			html.appendPageContent("<tr>");
			html.appendPageContent("<td class=\"texteDroite\">Nom* : ");
			html.appendPageContent("<td class=\"texteGauche\"><input type=\"text\" name=\"name\" " 
								+ (displayValue ? "value=\"" + name + "\"": "")
								+ " class=\"form\" /></td>");
			html.appendPageContent("</tr>");
			html.appendPageContent("<tr>");
			html.appendPageContent("<td class=\"texteDroite\">Prénom* : </td>");
			html.appendPageContent("<td class=\"texteGauche\"><input type=\"text\" name=\"firstname\" " 
								+ (displayValue ? "value=\"" + firstname + "\"": "")
								+ " class=\"form\" /></td>");
			html.appendPageContent("</tr>");
			html.appendPageContent("<tr>");
			html.appendPageContent("<td class=\"texteDroite\">Date de naissance* : </td>");
			html.appendPageContent("<td class=\"texteGauche\"><input type=\"text\" name=\"day\" size=\"1\" " 
								+ (displayValue && day != 0 ? "value=\"" + day + "\"": "")
								+ " class=\"form\" /> / ");
			html.appendPageContent("<input type=\"text\" name=\"month\" size=\"1\" " 
								+ (displayValue && month != 0 ? "value=\"" + month + "\"": "")
								+ " class=\"form\" /> / ");
			html.appendPageContent("<input type=\"text\" name=\"year\" size=\"4\" " 
								+ (displayValue && year != 0 ? "value=\"" + year + "\"": "")
								+ " class=\"form\" /></td>");
			html.appendPageContent("</tr>");
			html.appendPageContent("</table><br />");
			html.appendPageContent("<input type=\"submit\" name=\"button\" value=\"S'inscrire\" class=\"form\" />");
			html.appendPageContent("</form>");
			html.appendPageContent("</div>");
		}
		html.appendInfoContent("<div class=\"titre2\">Attention !</div>");
		html.appendInfoContent("<div class=\"texteCentre\">");
		html.appendInfoContent("</div>");
		html.appendInfoContent("<ul>");
		html.appendInfoContent("<li>Les champs marqués d'une * sont obligatoires</li>");
		html.appendInfoContent("<li>Vous devez avoir plus de 18 ans pour vous inscrire</li>");
		html.appendInfoContent("<li>Les informations donnés sont confidentielles et ne seront pas divulguées</li>");
		html.appendInfoContent("</ul>");
		out.println(html.getAllHTMLCode());
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException { doGet(req, res); }

}
