package servlet.app;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import servlet.LuceneUtils;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreInputStream;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.googlecode.lucene.gae.datastore.DataStoreDirectory;
import com.googlecode.lucene.gae.tool.IndexTool;

@SuppressWarnings("serial")
public class ImportServlet extends BaseHttpServlet {

	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Integer index = getIndex(req);

		BlobKey key = getBlobKey(req);

		if (key != null) {

			String name = LuceneUtils.getNameForDirectory(index);

			DataStoreDirectory directory = new DataStoreDirectory(name);

			IndexTool tool = new IndexTool(directory);

			BlobstoreInputStream in = new BlobstoreInputStream(key);

			try {
				tool.importZip(in);
			} finally {
				in.close();
			}

			blobstoreService.delete(key);

			resp.sendRedirect("/index.jsp");

		}

	}

	private BlobKey getBlobKey(HttpServletRequest req) {

		BlobKey result = null;

		Map<String, List<BlobKey>> uploads = blobstoreService.getUploads(req);

		Collection<List<BlobKey>> values = uploads.values();

		if (!values.isEmpty()) {
			List<BlobKey> list = values.iterator().next();

			if (!list.isEmpty()) {
				result = list.get(0);
			}

		}

		return result;

	}

}
