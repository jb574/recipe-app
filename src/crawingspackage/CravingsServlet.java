package crawingspackage;

import java.io.IOException;

import javax.servlet.http.*;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;

/**
 * 
 * @author Guillaume Boell
 *
 */

/**
 * Class that communicates with our backend to create background tasks
 * 
 * @author Guillaume Boell
 *
 */
@SuppressWarnings("serial")
public class CravingsServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		int startIdString = Integer.parseInt(req.getParameter("startId"));
		int endIdString = Integer.parseInt(req.getParameter("endId"));

		queueCrawlingTasks(startIdString, endIdString);
	}

	/**
	 * Add crawling tasks to the queue, these tasks will crawl the ids between startIndex and endIndex
	 * 
	 * @param startIndex
	 *            first task index
	 * @param endIndex
	 *            last task index
	 */
	public void queueCrawlingTasks(int startIndex, int endIndex) {
		// Add the task to the default queue.
		Queue queue = QueueFactory.getQueue("crawling");
		for (int index = startIndex; index < endIndex; ++index) {
			queue.add(com.google.appengine.api.taskqueue.TaskOptions.Builder
					.withUrl("/crawlRecipe").param("id",
							Integer.toString(index)));
		}
	}
}
