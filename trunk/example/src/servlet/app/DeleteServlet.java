package servlet.app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.store.gae.datastore.file.DataStoreFileRepository;

import servlet.LuceneUtils;

@SuppressWarnings("serial")
public class DeleteServlet extends BaseHttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Integer index = getIndex(req);

		if (index != null) {

			String name = LuceneUtils.getNameForDirectory(index);

			DataStoreFileRepository instance = new DataStoreFileRepository(name);

			instance.deleteAll();

		}

	}

}
