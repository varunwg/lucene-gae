package servlet.task;

import javax.servlet.http.HttpServlet;

import servlet.LuceneUtils;

public class BaseTaskHttpServlet extends HttpServlet {

	private int	index	= 0;

	protected synchronized int getIndex() {

		index++;

		if (index >= LuceneUtils.NUMBER_OF_DIRECTORYS) {
			index = 0;
		}

		return index;

	}

}
