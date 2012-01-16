package servlet.task;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import servlet.LuceneUtils;

import com.googlecode.lucene.gae.datastore.DataStoreDirectory;

@SuppressWarnings("serial")
public class DeleteTaskServlet extends BaseTaskHttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		int index = getIndex();

		String name = LuceneUtils.getNameForDirectory(index);
		DataStoreDirectory directory = new DataStoreDirectory(name);

		directory.cleanDeletedFiles();

		System.out.println("Index=" + index);

	}

}
