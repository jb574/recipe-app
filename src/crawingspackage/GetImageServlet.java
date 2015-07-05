package crawingspackage;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import crawingspackage.models.WebImage;

/**
 * 
 * @author Guillaume Boell
 *
 */
public class GetImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Get images for a recipe. If there is no image stored, respond with our
	 * own default image.
	 */
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String imageIdStr = req.getParameter("id");

		if (imageIdStr != null) {
			int imgId = new Integer(imageIdStr);

			WebImage img = ARecipeFactory.getImageFromIndex(imgId);
			if (img != null && img.getImageContentType() != null) {
				// Set the appropriate Content-Type header and write the raw
				// bytes
				// to the response's output stream
				resp.setContentType(img.getImageContentType());
				resp.getOutputStream().write(img.getImageBytes());
				return;
			}
		}
		/*
		 * If no image is found with the given title, redirect the user to a
		 * static image
		 */
		resp.sendRedirect("/img/noimage.jpg");
	}
}