package servlet.task;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.index.IndexWriter;

import servlet.LuceneUtils;

@SuppressWarnings("serial")
public class OtimizeTaskServlet extends BaseTaskHttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		int index = getIndex();

		IndexWriter writer = LuceneUtils.getWriter(index);

		writer.optimize();

		LuceneUtils.resetSearcher(index);
		LuceneUtils.resetWriter(index);

		System.out.println("Index=" + index);

	}

}
