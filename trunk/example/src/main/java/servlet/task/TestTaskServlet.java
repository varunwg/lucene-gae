package servlet.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.util.IOUtils;

import servlet.LuceneUtils;

@SuppressWarnings("serial")
public class TestTaskServlet extends BaseTaskHttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		int index = getIndex();

		IndexWriter writer = LuceneUtils.getWriter(index);

		String texts = getTexts();

		LuceneUtils.writeTexts(writer, texts, index);

		LuceneUtils.resetWriter(index);

		System.out.println("Index=" + index);

	}

	private String getTexts() throws IOException {
		URL url = new URL("http://www.uol.com.br");
		URLConnection urlConn = url.openConnection();
		urlConn.setConnectTimeout(30000);
		urlConn.setReadTimeout(30000);
		urlConn.setAllowUserInteraction(false);
		urlConn.setDoOutput(true);
		InputStreamReader reader = new InputStreamReader(urlConn.getInputStream());
		return IOUtils.toString(new BufferedReader(reader));
	}

}
