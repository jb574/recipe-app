package crawingspackage;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.BigOvenRecipeCrawler;

/**
 * Servlet for communicating with BigOven to get some recipes from them
 * 
 * @author Guillaume Boell
 *
 */
public class CrawlingTaskServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String idString = req.getParameter("id");
		int id = Integer.parseInt(idString);

		BigOvenRecipeCrawler bigOven = new BigOvenRecipeCrawler();

		resp.setContentType("text/plain");
		resp.getWriter().println("crawling recipe" + id + "...");

		bigOven.getRecipeFromIndex(id);
		resp.getWriter().println("done !");
	}
}
