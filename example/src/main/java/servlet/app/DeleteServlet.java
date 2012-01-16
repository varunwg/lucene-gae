package servlet.app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import servlet.LuceneUtils;

import com.googlecode.lucene.gae.datastore.DataStoreDirectory;

@SuppressWarnings("serial")
public class DeleteServlet extends BaseHttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Integer index = getIndex(req);

		if (index != null) {

			String name = LuceneUtils.getNameForDirectory(index);

			DataStoreDirectory directory = new DataStoreDirectory(name);

			directory.deleteFiles();

		}

	}

}
