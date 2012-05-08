package com.googlecode.lucene.gae;

import java.io.IOException;

public interface CleanableDirectory {

	void cleanFiles() throws IOException;

}