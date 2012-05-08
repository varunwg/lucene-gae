package servlet.app;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseHttpServlet extends HttpServlet {

	protected Integer getIndex(HttpServletRequest req) {

		String index = req.getParameter("index");

		Integer result = -1;

		if (index != null) {
			result = Integer.valueOf(index);
		}

		return result;

	}

	protected String getText(HttpServletRequest req) {
		return req.getParameter("text");
	}

	protected String getSearch(HttpServletRequest req) {
		return req.getParameter("q");
	}

	protected void redirectToIndex(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {
		String nextJSP = "/index.jsp";
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
		dispatcher.forward(req, resp);
	}

}
