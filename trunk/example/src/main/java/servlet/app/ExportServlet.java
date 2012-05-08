package servlet.app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import servlet.LuceneUtils;

import com.googlecode.lucene.gae.datastore.DataStoreDirectory;
import com.googlecode.lucene.gae.tool.IndexTool;

@SuppressWarnings("serial")
public class ExportServlet extends BaseHttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Integer index = getIndex(req);

		if (index != null) {

			String name = LuceneUtils.getNameForDirectory(index);

			DataStoreDirectory directory = new DataStoreDirectory(name);

			IndexTool tool = new IndexTool(directory);

			resp.setContentType("application/zip");

			ServletOutputStream out = resp.getOutputStream();

			try {
				tool.exportZip(out);
			} finally {
				out.close();
			}

		}

	}

}
