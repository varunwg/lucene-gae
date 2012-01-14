package servlet.task;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import servlet.LuceneUtils;

import com.googlecode.lucene.gae.datastore.file.DataStoreFileRepository;

@SuppressWarnings("serial")
public class DeleteTaskServlet extends BaseTaskHttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		int index = getIndex();

		String directoryName = LuceneUtils.getNameForDirectory(index);

		DataStoreFileRepository instance = new DataStoreFileRepository(directoryName);

		List<String> names = instance.listDeletedNames();

		for (String name : names) {
			instance.delete(name);
		}

		System.out.println("Index=" + index);

	}

}
