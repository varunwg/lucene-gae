package servlet.app;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import servlet.LuceneUtils;

@SuppressWarnings("serial")
public class SearchServlet extends BaseHttpServlet {

	private Query createQuery(String string, String query) {
		try {
			return LuceneUtils.createQuery("text").parse(query);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Integer index = getIndex(req);
		String text = getText(req);

		if (index != null && text != null) {

			IndexSearcher searcher = LuceneUtils.getSearcher(index);
			IndexReader reader = searcher.getIndexReader();

			TopDocs topDocs = searcher.search(createQuery("text", text), 10);

			Map<String, String> results = new LinkedHashMap<String, String>();

			for (ScoreDoc doc : topDocs.scoreDocs) {
				Document document = reader.document(doc.doc);
				String docId = document.get("id");
				String docText = document.get("text");
				results.put(docId, docText);
			}

			req.setAttribute("results", results);
			req.setAttribute("text", text);
			req.setAttribute("index", index.toString());

		}

		redirectToIndex(req, resp);

	}

}
