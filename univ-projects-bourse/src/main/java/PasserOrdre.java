import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;



public class PasserOrdre extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		if (session == null) { res.sendRedirect(HTMLCode.getIdentPath()); return; }
		String id = (String)session.getAttribute("id");
		String role_ = (String)session.getAttribute("role");
		if (session == null || role_ == null || "".equals(role_)) { res.sendRedirect(HTMLCode.getIdentPath()); return; }
		char role = role_.charAt(0);

		HTMLCode html = new HTMLCode("Debogage only");

		html.appendPageContent("<form action=\"./PasserOrdre\" method=\"post\">");
		html.appendPageContent("alias du marche&nbsp;:&nbsp;<input name=\"marche\" size=\"3\" /><br/>");
		html.appendPageContent("prix unitaire&nbsp;:&nbsp;<input name=\"prix\" /><br/>");
		html.appendPageContent("quantite&nbsp;:&nbsp;<input name=\"quantite\" /><br/>");
		html.appendPageContent("achat/vente ?&nbsp;:&nbsp;<input name=\"type\" /><br/>");
		html.appendPageContent("+/-?&nbsp;:&nbsp;<input name=\"signe\" /><br/>");
		html.appendPageContent("<input type=\"submit\" /><br/>");
		html.appendPageContent("</form>");

		html.setMenuContent( role=='m'? html.getMenuMaker() : html.getMenuUser());
		html.appendInfoContent(html.getPorteMonnaie(id,role));
		res.setContentType("text/html");
		res.getWriter().println( html.getAllHTMLCode() );
	}


	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		if (session == null) { res.sendRedirect(HTMLCode.getIdentPath()); return; }
		String id = (String)session.getAttribute("id");
		String role_ = (String)session.getAttribute("role");
		if (session == null || role_ == null || "".equals(role_)) { res.sendRedirect(HTMLCode.getIdentPath()); return; }
		char role = role_.charAt(0);

		String marche;
		int prix, quantite;
		char type, signe;
		try {
			marche   = req.getParameter("marche");				if(marche == null) throw new Exception();
			prix     = Integer.parseInt(req.getParameter("prix"));		if(prix<0 || prix>100) throw new Exception();
			quantite = Integer.parseInt(req.getParameter("quantite"));
			type  = req.getParameter("type").toLowerCase().charAt(0);	if( type!='a'&& type!='v') throw new Exception();
			signe = req.getParameter("signe").toLowerCase().charAt(0);	if(signe!='+'&&signe!='-') throw new Exception();
		} catch(NumberFormatException e) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST, "des nombres etaient attendus");
			return;
		} catch(Exception e) { // not. NullPointerException lors de charAt & cie
			res.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		Connection con = null;
		try {      con = BourseInfo.getInstance().getDBConnection();
		} catch(SQLException e) {
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"impossible d'obtenir une connection avec DB");
			return;
		}
		try {
			Statement stmt = con.createStatement();

			boolean petitmalin = false;
			if(type == 'a') { // verifier qu'il a les sous
				ResultSet rs = stmt.executeQuery("SELECT solde FROM Utilisateurs WHERE login='"+id+"'");
				rs.next(); // a moins que la session soit foireuse, l'enregistrement existe et est unique
				int solde = rs.getInt(1);
				petitmalin = solde < prix*quantite;
			} else { // verifier qu'il a les bons
				int disponible = 0;
				ResultSet rs = stmt.executeQuery("SELECT quantite FROM Bons WHERE "
					+"proprietaire='"+id+"' AND marche='"+marche+"'");
				while(rs.next())
					disponible += rs.getInt(1);
				petitmalin = disponible < quantite;
			}
			if(petitmalin) {
				HTMLCode html = new HTMLCode("Hahaha");
				html.setMenuContent( role=='m'? html.getMenuMaker() : html.getMenuUser());
				html.appendInfoContent(html.getPorteMonnaie(id,role));
				html.appendPageContent("<p>Vous voulez vivre au-dessus de vos moyens&nbsp;!</p>");
				res.setContentType("text/html");
				res.getWriter().println( html.getAllHTMLCode() );
				return;
			}

			// on fait la poignee de fait (si possible)
			/* return violamment lorsque la poignee est totalement faite */
			{
				ResultSet rs = con.createStatement().executeQuery("SELECT idO,prix,quantite,passeur FROM Offres "
					+"WHERE prix "+(type=='a'?"<=":">=")+prix
					+" ORDER BY prix");
				while(quantite>0 && rs.next()) {
					ResultSet rsA = stmt.executeQuery("SELECT 1 from Bons WHERE proprietaire = '"+id+"' AND marche = '"+marche+"'");
					if(!rsA.next())
						stmt.executeUpdate("UPDATE Utilisateurs SET total_marches = total_marches+1 WHERE login='"+rs.getString("passeur")+"'");
					ResultSet rsB = stmt.executeQuery("SELECT 1 from Bons WHERE proprietaire = '"+rs.getString("passeur")+"' AND marche = '"+marche+"'");
					if(!rsB.next())
						stmt.executeUpdate("UPDATE Utilisateurs SET total_marches = total_marches+1 WHERE login='"+id+"'");
					int q = quantite>rs.getInt("quantite")? rs.getInt("quantite") : quantite;
					stmt.executeUpdate("INSERT INTO Bons (proprietaire,quantite,marche,positif) VALUES "+
						"('"+id+"',"+q+",'"+marche+"',"+((type=='a')==(signe=='+'))+")");
					stmt.executeUpdate("INSERT INTO Bons (proprietaire,quantite,marche,positif) VALUES "+
						"('"+rs.getString("passeur")+"',"+q+",'"+marche+"',"+((type=='a')!=(signe=='+'))+")");
					stmt.executeUpdate("UPDATE Utilisateurs "+
							" SET solde = solde"+(type=='a'?'-':'+')+(rs.getInt("prix")*q)+
							" WHERE login = '"+id+"'");
					if(type == 'a') { // quand on achete, il faut payer l'autre utilisateur
						stmt.executeUpdate("UPDATE Utilisateurs SET "
							+" solde = solde+"+(rs.getInt("prix")*q)
							+" WHERE login = '"+rs.getString("passeur")+"'");
					}
					quantite -= q;
					if(rs.getInt("quantite") == q) // on a pris l'offre entiere
						stmt.executeUpdate("DELETE FROM Offres WHERE idO='"+rs.getString("idO")+"'");
					else	stmt.executeUpdate("UPDATE Offres SET quantite = quantite-"+q
								+" WHERE idO='"+rs.getString("idO")+"'");

				}
				if(quantite <= 0) {
					res.sendRedirect("./ListeOrdres");
					return;
				}
			}

			// sinon on mes les offres en attente de poignee de main
			/* on arrive ici que s'il n'y a pas eu de poigne complete */
			stmt.executeUpdate("INSERT INTO Offres (passeur,marche,prix,achat,symetrique,quantite,positif) VALUES "+
					"('"+id+"','"+marche+"',"+prix+","+(type=='a')+",false,"+quantite+","+(signe=='+')+")");
			//stmt.executeUpdate("INSERT INTO Offres (passeur,marche,prix,achat,symetrique,quantite,positif) VALUES "+
			//		"('"+id+"','"+marche+"',"+(100-prix)+","+(type=='a')+",true," +quantite+","+(signe!='+')+")");
			if(type == 'a')
				stmt.executeUpdate("UPDATE Utilisateurs SET solde = solde-"+(prix*quantite)+
						" WHERE login='"+id+"'");
		} catch(SQLException e) {
			//System.err.println(e.getClass().getName()+": "+e.getMessage());
			e.printStackTrace();
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"sqlexception: "+e);
			return;
		} finally {			try { con.close(); } catch(Exception e) {} }


		//HTMLCode html = new HTMLCode("Ok");
		//html.appendPageContent("<p>Demande enregistr&eacute;e.</p>");
		//res.setContentType("text/html");
		//res.getWriter().println( html.getAllHTMLCode() );
		res.sendRedirect("./ListeOrdres");
		return;
	}
}
