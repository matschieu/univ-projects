
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class CreerMarche extends HttpServlet {

	private static final int LIB_MAX_SIZE = 255;
	private static final int ALIAS_MAX_SIZE = 3;

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
		String libellePos = "";
		String libelleNeg = "";
		String alias = "";
		int day = 0;
		int month = 0;
		int year = 0;
		html.appendPageContent("<div class=\"titre1\">Creation d'un marché</div><br /><br />");
		if (req.getParameter("ok") != null) {
			int valid = 0;
			displayValue = true;
			libellePos = req.getParameter("libpos");
			libelleNeg = req.getParameter("libneg");
			alias = req.getParameter("al");
			if (id == null || alias.equals("")) {
				html.appendPageContent("<div class=\"erreur\">Erreur : vous n'avez pas entré d'alias pour le marché</div>");
				alias = "";
			}
			else if (alias.length() > ALIAS_MAX_SIZE) {
				html.appendPageContent("<div class=\"erreur\">Erreur : votre alias est trop long</div>");
				alias = "";
			}
			else valid++;
			if (id == null || libellePos.equals("")) {
				html.appendPageContent("<div class=\"erreur\">Erreur : vous n'avez pas entré d'intitulé pour le marché</div>");
				libellePos = "";
			}
			else if (libellePos.length() > LIB_MAX_SIZE) {
				html.appendPageContent("<div class=\"erreur\">Erreur : votre intitulé de marché est trop long</div>");
				libellePos = "";
			}
			else valid++;
			if (id == null || libelleNeg.equals("")) {
				html.appendPageContent("<div class=\"erreur\">Erreur : vous n'avez pas entré d'intitulé contraire pour le marché</div>");
				libelleNeg = "";
			}
			else if (libelleNeg.length() > LIB_MAX_SIZE) {
				html.appendPageContent("<div class=\"erreur\">Erreur : votre intitulé contraire de marché est trop long</div>");
				libelleNeg = "";
			}
			else valid++;
			try {
				Calendar c = new GregorianCalendar(TimeZone.getTimeZone("GMT+1"));
				day = Integer.parseInt(req.getParameter("day"));
				month = Integer.parseInt(req.getParameter("month"));
				year = Integer.parseInt(req.getParameter("year"));
				if (year < c.get(Calendar.YEAR)) throw new NumberFormatException();
				if (day < 1 || day > 31) throw new NumberFormatException();
				if (month < 1 || month > 12) throw new NumberFormatException();
				valid++;
			}
			catch(NumberFormatException e) {
				html.appendPageContent("<div class=\"erreur\">Erreur : date invalide</div>");
			}
			if (valid == 4) {
				try {
					Connection con = BourseInfo.getInstance().getDBConnection();
					Statement stmt = con.createStatement();
					java.sql.Date date = java.sql.Date.valueOf(year + "-" + month + "-" + day);
					ResultSet rs = stmt.executeQuery("select * from Marches where alias='" + alias + "'");
					if (rs.next()) throw new IllegalArgumentException();
					libellePos = libellePos.replace("'", "\\'");
					libelleNeg = libelleNeg.replace("'", "\\'");
					stmt.executeUpdate("insert into Marches values('" + alias + "', '" + libellePos + "', '" 
									+ libelleNeg + "', '" + date + "', '" + id + "')");
					con.close();
					html.appendPageContent("<div class=\"texteCentre\">");
					html.appendPageContent("Enregistrement du marché correctement terminé !<br /><br />");
					html.appendPageContent("</div>");
					display = false;
				}
				catch (SQLException e) {
					html.appendPageContent("<div class=\"erreur\">Erreur : Impossible d'accéder à la base de données</div>");
					System.out.println(e.getMessage());
				}
				catch(IllegalArgumentException e) {
					html.appendPageContent("<div class=\"erreur\">Erreur : cet alias existe déjà</div>");
					alias = "";
				}
				catch(Exception e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
				}
			}
			html.appendPageContent("<br />");
		}
		if (display) {
			html.appendPageContent("Pour vous inscrire au site " + html.getSiteTitle()
								+ ", veuillez remplir le fomulaire suivant :<br />");
			html.appendPageContent("<div class=\"texteCentre\">");
			html.appendPageContent("<form method=\"post\" action=\"./CreerMarche\">");
			html.appendPageContent("<input type=\"hidden\" name=\"ok\" value=\"1\" />");
			html.appendPageContent("<table>");
			html.appendPageContent("<tr>");
			html.appendPageContent("<td class=\"texteDroite\">Alias du marché (3 lettres) : </td>");
			html.appendPageContent("<td class=\"texteGauche\"><input type=\"text\" name=\"al\" size=\"3\" "
								+ (displayValue ? "value=\"" + alias + "\"": "")
								+ " class=\"form\" /></td>");
			html.appendPageContent("</tr>");
			html.appendPageContent("<tr>");
			html.appendPageContent("<td class=\"texteDroite\">Intitulé du marché : </td>");
			html.appendPageContent("<td class=\"texteGauche\"><input type=\"text\" name=\"libpos\" size=\"30\" "
								+ (displayValue ? "value=\"" + libellePos + "\"": "")
								+ " class=\"form\" /></td>");
			html.appendPageContent("</tr>");
			html.appendPageContent("<tr>");
			html.appendPageContent("<td class=\"texteDroite\">Intitulé contraire du marché : </td>");
			html.appendPageContent("<td class=\"texteGauche\"><input type=\"text\" name=\"libneg\" size=\"30\" "
								+ (displayValue ? "value=\"" + libelleNeg + "\"": "")
								+ " class=\"form\" /></td>");
			html.appendPageContent("</tr>");
			html.appendPageContent("<tr>");
			html.appendPageContent("<td class=\"texteDroite\">Date de fin : </td>");
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
			html.appendPageContent("<input type=\"submit\" name=\"button\" value=\"Créer\" class=\"form\" />");
			html.appendPageContent("</form>");
			html.appendPageContent("</div>");
		}
		out.println(html.getAllHTMLCode());
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException { doGet(req, res); }

}
