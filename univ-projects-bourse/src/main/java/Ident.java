
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Ident extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException {
		String id = req.getParameter("id");
		String pwd = req.getParameter("pwd");
		PrintWriter out = res.getWriter();
		HTMLCode html = new HTMLCode("Identification");
		res.setContentType("text/html");
		html.setMenuContent(html.getMainMenu());
		html.appendPageContent("<div class=\"titre1\">Bienvenue !</div><br /><br />");
		html.appendPageContent("Le site " + html.getSiteTitle() + " est un site de bourse d'informations.<br />");
		html.appendPageContent("Grâce au site " + html.getSiteTitle() + " pariez sur des marchés divers et variés afin de bien remplir"
							+ " vos bourses et devenez riche.<br /><br />");;
		html.appendInfoContent("<div class=\"titre2\">Identification</div><br />");
		if (id != null && pwd != null) {
			try {
				Connection con = BourseInfo.getInstance().getDBConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("select login, mdp from Utilisateurs where login='" + id + "'");
				rs.next();
				if (id.equals(rs.getString(1)) && pwd.equals(rs.getString(2))) {
					rs = stmt.executeQuery("select role from Roles where login='" + id + "'");
					rs.next();
					char c = rs.getString(1).charAt(0);
					HttpSession session = req.getSession(true);
					session.setAttribute("id", "" + id);
					session.setAttribute("role", "" + c);
					session.setMaxInactiveInterval(60 * 10);
					if (c == 'm') res.sendRedirect("./ListeMarche");
					else if (c == 'u') res.sendRedirect("./ListeMarche"); // ***
				}
				else throw new Exception();
				stmt.close();
				con.close();
			}
			catch (SQLException e) { html.appendInfoContent("<div class=\"erreur\">Erreur : identifiant incorrect !</div><br />"); }
			catch(Exception e) { html.appendInfoContent("<div class=\"erreur\">Erreur : mot de passe incorrect !</div><br />"); }
		}
		html.appendInfoContent(html.getIdent());
		out.println(html.getAllHTMLCode());
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException { doGet(req, res); }

}
