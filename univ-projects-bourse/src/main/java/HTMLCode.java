
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class HTMLCode {

	private static final String TITLE = "Les bourses bien pleines";
	private static final String COPYRIGHT = "(c) Les bourses bien pleines - Matschieu & Azollyx -, 2008";
	private static final String IDENT_PAGE = "./Ident";

	private String pageTitle;
	private String menuContent;
	private String pageContent;
	private String infoContent;

	public HTMLCode() { this(""); }
	public HTMLCode(String title) {
		pageTitle = title;
		menuContent = "";
		pageContent = "";
		infoContent = "";
	}

	public void setPageTitle(String title) { pageTitle = title; }
	public void setMenuContent(String str) { menuContent = str; }
	public void setPageContent(String str) { pageContent = str; }
	public void setInfoContent(String str) { infoContent = str; }

	public void appendPageTitle(String title) { pageTitle += title + "\n"; }
	public void appendMenuContent(String str) { menuContent += "\t\t\t" + str + "\n"; }
	public void appendPageContent(String str) { pageContent += "\t\t\t\t" + str + "\n"; }
	public void appendInfoContent(String str) { infoContent += "\t\t\t\t" + str + "\n"; }

	public String getPageTitle()   { return pageTitle; }
	public String getMenuContent() { return menuContent; }
	public String getPageContent() { return pageContent; }
	public String getInfoContent() { return infoContent; }

	public static String getIdentPath() { return IDENT_PAGE; }
	public static String getSiteTitle() { return TITLE; }

	public String getHeader() {
		StringBuffer str = new StringBuffer("");
		str.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\n");
		str.append("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n\n");
		str.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"fr\" lang=\"fr\">\n\n");
		str.append("<head>\n");
		str.append("\t<title>" + TITLE + (pageTitle.equals("") ? "" : " : " + pageTitle) + "</title>\n");
		str.append("\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" />\n");
		str.append("\t<link href=\"../styles.css\" rel=\"stylesheet\" type=\"text/css\" />\n");
		str.append("</head>\n\n");
		str.append("<body>\n");
		str.append("\t<div id=\"cadre\">\n");
		str.append("\t\t<div id=\"haut\"></div>\n");
		return str.toString();
	}

	public String getMenu() {
		StringBuffer str = new StringBuffer("");
		str.append("\t\t<div id=\"menu\" class=\"texteCentre\">\n");
		str.append(menuContent);
		str.append("\t\t</div>\n");
		return str.toString();
	}

	public String getMiddle() {
		StringBuffer str = new StringBuffer("");
		str.append("\t\t<div id=\"containeur\">\n");
		str.append("\t\t\t<div id=\"centre\" class=\"texte\">\n");
		str.append(pageContent);
		str.append("\t\t\t</div>\n");
		str.append("\t\t\t<div id=\"info\" class=\"texte\">\n");
		str.append(infoContent);
		str.append("\t\t\t</div>\n");
		str.append("\t\t</div>\n");
		return str.toString();
	}

	public String getFooter() {
		StringBuffer str = new StringBuffer("");
		str.append("\t\t<div id=\"bas\" class=\"copyright\">\n");
		str.append("\t\t\t" + COPYRIGHT + "\n");
		str.append("\t\t</div>\n");
		str.append("\t</div>\n");
		str.append("</body>\n\n");
		str.append("</html>\n");
		return str.toString();
	}

	public String getAllHTMLCode() {
		StringBuffer str = new StringBuffer("");
		str.append(getHeader());
		str.append(getMenu());
		str.append(getMiddle());
		str.append(getFooter());
		return str.toString();
	}

	public String getMainMenu() {
		StringBuffer str = new StringBuffer("");
		str.append("\t\t\t<a href=\"./Ident\" class=\"lienMenu\" target=\"_parent\">Accueil</a> | \n");
		str.append("\t\t\t<a href=\"./Inscription\" class=\"lienMenu\" target=\"_parent\">S'inscrire</a> | \n");
		str.append("\t\t\t<a href=\"./HallOfFame\" class=\"lienMenu\" target=\"_parent\">Hall of fame</a>\n");
		return str.toString();
	}

	public String getMenuMaker() {
		StringBuffer str = new StringBuffer("");
		str.append("\t\t\t<a href=\"./CreerMarche\" class=\"lienMenu\" target=\"_parent\">Créer un marché</a> | \n");
		str.append("\t\t\t<a href=\"./ListeMarche\" class=\"lienMenu\" target=\"_parent\">Liste des marchés</a> | \n");
		str.append("\t\t\t<a href=\"./ListeOrdres\" class=\"lienMenu\" target=\"_parent\">Ordres</a> | \n");
		str.append("\t\t\t<a href=\"./TerminerMarche\" class=\"lienMenu\" target=\"_parent\">Terminer un marché</a> | \n");
		str.append("\t\t\t<a href=\"./AjouterMaker\" class=\"lienMenu\" target=\"_parent\">Ajouter un maker</a> | \n");
		str.append("\t\t\t<a href=\"./HallOfFame\" class=\"lienMenu\" target=\"_parent\">Hall of fame</a> | \n");
		str.append("\t\t\t<a href=\"./Deconnexion\" class=\"lienMenu\" target=\"_parent\">Déconnexion</a>\n");
		return str.toString();
	}

	public String getMenuUser() {
		StringBuffer str = new StringBuffer("");
		str.append("\t\t\t<a href=\"./ListeMarche\" class=\"lienMenu\" target=\"_parent\">Liste des marchés</a> | \n");
		str.append("\t\t\t<a href=\"./ListeOrdres\" class=\"lienMenu\" target=\"_parent\">Liste des ordres</a> | \n");
		str.append("\t\t\t<a href=\"./HallOfFame\" class=\"lienMenu\" target=\"_parent\">Hall of fame</a> | \n");
		str.append("\t\t\t<a href=\"./Deconnexion\" class=\"lienMenu\" target=\"_parent\">Déconnexion</a>\n");
		return str.toString();
	}

	public String getIdent() {
		StringBuffer str = new StringBuffer("");
		str.append("<img src=\"../img/cles.png\" alt=\"\" border=\"0\" align=\"left\" />");
		str.append("Veuillez vous identifier pour continuer :<br /><br />");
		str.append("<form method=\"post\" action=\"./Ident\">");
		str.append("<div class=\"texteDroite\">");
		str.append("Identifiant : <input type=\"text\" name=\"id\" class=\"form\" /><br /><br />");
		str.append("Mot de passe : <input type=\"password\" name=\"pwd\" class=\"form\" /><br /><br />");
		str.append("</div>");
		str.append("<div class=\"texteCentre\">");
		str.append("<input type=\"submit\" name=\"button\" value=\"Envoyer\" class=\"form\" /><br /><br />");
		str.append("<a href=\"./Inscription\" class=\"lien\" target=\"_parent\">s'inscrire</a>");
		str.append("</div>");
		str.append("</form>");
		return str.toString();
	}

	public String getPorteMonnaie(String id, char role) {
		StringBuffer str = new StringBuffer("");
		String nom = "";
		String prenom = "";
		int solde = 0;
		int totalMarches = 0;
		int marchesGagnant = 0;
		int gain = 0;
		int bons = 0;
		try {
			Connection con = BourseInfo.getInstance().getDBConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select nom, prenom, solde, total_marches, marches_gagnant, gain " +
					"from Utilisateurs where login='" + id + "'");
			rs.next();
			nom = rs.getString(1);
			prenom = rs.getString(2);
			solde = rs.getInt(3);
			totalMarches = rs.getInt(4);
			marchesGagnant = rs.getInt(5);
			gain = rs.getInt(6);
			rs = stmt.executeQuery("select sum(quantite) from Bons where proprietaire = '" + id + "'");
			rs.next();
			bons = rs.getInt(1);
			con.close();
		}
		catch(Exception e) { }
		str.append("\t\t\t\t<div class=\"titre2\">Bonjour " + id + "</div><br />\n");
		str.append("\t\t\t\tVos informations personnelles : <br />\n");
		str.append("\t\t\t\t<ul>\n");
		str.append("\t\t\t\t<li>Nom : " + nom + "</li>\n");
		str.append("\t\t\t\t<li>Prénom : " + prenom + "</li>\n");
		str.append("\t\t\t\t</ul>\n");
		str.append("\t\t\t\tVotre porte monnaie : <br />\n");
		str.append("\t\t\t\t<ul>\n");
		str.append("\t\t\t\t<li>Cash disponible : " + solde + "</li>\n");
		str.append("\t\t\t\t<li>Mes bons : " + bons + "</li>\n");
		str.append("\t\t\t\t<li>Ma performance : " + (marchesGagnant != 0 ? (float)totalMarches / marchesGagnant * 100 : 0) + "%<br />\n");
		str.append("\t\t\t\t<li>Mon gain : " + gain + "</li>\n");
		str.append("\t\t\t\t</ul>\n");
		return str.toString();
	}

}
