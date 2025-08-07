
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Deconnexion extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		session.invalidate();
		res.sendRedirect((new HTMLCode()).getIdentPath());
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException { doGet(req, res); }

}
