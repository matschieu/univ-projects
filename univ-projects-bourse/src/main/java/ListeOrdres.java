import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;



public class ListeOrdres extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		if (session == null) { res.sendRedirect(HTMLCode.getIdentPath()); return; }
		String id = (String)session.getAttribute("id");
		String role_ = (String)session.getAttribute("role");
		if (session == null || role_ == null || "".equals(role_)) { res.sendRedirect(HTMLCode.getIdentPath()); return; }
		char role = role_.charAt(0);

		String idO = req.getParameter("del-offre");
		Connection con = null;
		if(idO != null) // -> returns
			try {
				con = BourseInfo.getInstance().getDBConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM Offres WHERE idO='"+idO+"'");
				if(!rs.next())
					throw new SQLException();
				int q = rs.getInt("quantite"), p = rs.getInt("prix");
				stmt.executeUpdate("UPDATE Utilisateurs set solde=solde+"+(p*q)+" WHERE login='"+rs.getString("passeur")+"'");
				stmt.executeUpdate("DELETE FROM Offres WHERE idO='"+idO+"'");
				res.sendRedirect("./ListeOrdres");
			} catch(SQLException e) {
				e.printStackTrace();
				res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,e.getMessage());
			} finally {
				try { con.close(); } catch(Exception e) {}
				return;
			}

		try {
			HTMLCode html = doListePage(id);
			if(html == null) return;
			html.setMenuContent( role=='m'? html.getMenuMaker() : html.getMenuUser());
			html.appendInfoContent(html.getPorteMonnaie(id,role));
			res.setContentType("text/html");
			res.getWriter().println( html.getAllHTMLCode() );
		} catch(SQLException e) {
			e.printStackTrace();
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"sqlexception: "+e);
			return;
		}
	}
	protected HTMLCode doListePage(String login) throws SQLException {
		HTMLCode html = new HTMLCode("Mon porte-feuilles");
		Connection con = BourseInfo.getInstance().getDBConnection();
		Statement stmt = con.createStatement();
		ResultSet rs;

		html.appendPageContent("<div class=\"titre1\">Mes bons</div>");
		rs = stmt.executeQuery("SELECT * FROM Bons WHERE proprietaire = '"+login+"'");
		if(!rs.next()) {
			html.appendPageContent("<p>Vous n'avez actuellement aucun bon.</p>");
		} else {
			html.appendPageContent("<center><table border=\"1\">");
			html.appendPageContent("<tr> <td>marche</td><td>signe</td>  <td>quantit&eacute;</td></tr>");
			do	html.appendPageContent("<tr> <td><a href=\"./AchatVente?market="+rs.getString("marche")+"\">"+rs.getString("marche")+"</a></td><td>"+(rs.getBoolean("positif")?"+":"-")+"</td><td>"+rs.getString("quantite")+"</td></tr>");
			while(rs.next());
			html.appendPageContent("</table></center><br/>");
		}

		html.appendPageContent("<div class=\"titre1\">Mes offres en cours</div>");
		rs = stmt.executeQuery("SELECT * FROM Offres WHERE passeur = '"+login+"' AND symetrique = false");
		if(!rs.next())
			html.appendPageContent("<p>Vous n'avez aucune offre non satisfaite.</p>");
		else {
			html.appendPageContent("<center><table border=\"1\">");
			html.appendPageContent("<tr> <td>type</td><td>marche</td><td>signe</td>  <td>prix</td><td>quantit&eacute;</td></tr>");
			do	html.appendPageContent("<tr> <td>"+(rs.getBoolean("achat")?"achat":"vente")+"</td><td><a href=\"./AchatVente?market="+rs.getString("marche")+"\">"+rs.getString("marche")+"</a></td><td>"+(rs.getBoolean("positif")?"+":"-")+"</td>  <td>"+rs.getString("prix")+"</td><td>"+rs.getString("quantite")+"</td> <td><a href=\"./ListeOrdres?del-offre="+rs.getInt("idO")+"\"><img src=\"../img/supprimer.gif\" alt=\"supprimer\" style=\"border:none;\"/></a></td></tr>");
			while(rs.next());
			html.appendPageContent("</table></center><br/>");
		}

		return html;
	}
}
