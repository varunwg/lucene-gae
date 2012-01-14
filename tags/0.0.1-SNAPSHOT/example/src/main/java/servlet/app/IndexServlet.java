package servlet.app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.index.IndexWriter;

import servlet.LuceneUtils;

@SuppressWarnings("serial")
public class IndexServlet extends BaseHttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Integer index = getIndex(req);
		String text = getText(req);

		if (index != null && text != null) {

			IndexWriter writer = LuceneUtils.getWriter(index);

			LuceneUtils.writeTexts(writer, text, index);

			LuceneUtils.resetWriter(index);

		}

		redirectToIndex(req, resp);

	}

}
