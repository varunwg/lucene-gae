package com.googlecode.lucene.gae.tool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;

import com.googlecode.lucene.gae.CleanableDirectory;

public class IndexTool {

	private final int bufferSize;
	private final Directory directory;

	public IndexTool(Directory directory) {
		this(directory, 1024);
	}

	public IndexTool(Directory directory, int bufferSize) {
		this.directory = directory;
		this.bufferSize = bufferSize;
	}

	public void clean() throws IOException {

		if (directory instanceof CleanableDirectory) {
			CleanableDirectory dir = (CleanableDirectory) directory;
			dir.cleanFiles();
		}

	}

	public void exportZip(OutputStream out) throws IOException {

		ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(out));

		String[] files = directory.listAll();

		for (String file : files) {

			zipOut.putNextEntry(new ZipEntry(file));

			writeFile(file, zipOut);

			zipOut.closeEntry();

		}

		zipOut.close();

	}

	public void importZip(InputStream in) throws IOException {

		ZipInputStream zipIn = new ZipInputStream(new BufferedInputStream(in));

		ZipEntry entry = zipIn.getNextEntry();

		while (entry != null) {

			String file = entry.getName();

			readFile(file, zipIn);

			entry = zipIn.getNextEntry();

		}

		zipIn.close();

	}

	public void optimize(IndexWriterConfig config) throws Exception {
		IndexWriter writer = new IndexWriter(directory, config);
		try {
			writer.optimize();
		} finally {
			writer.close();
		}
	}

	public void optimizeAndClean(IndexWriterConfig config) throws Exception {
		optimize(config);
		clean();
	}

	public void readFile(String file, InputStream src) throws IOException {

		byte[] buffer = new byte[bufferSize];

		IndexOutput out = directory.createOutput(file);

		int count = 0;
		int start = 0;

		while ((count = src.read(buffer)) != -1) {
			out.writeBytes(buffer, start, count);
			start += count;
		}

		out.close();

	}

	public void writeFile(String file, OutputStream dest) throws IOException {

		byte[] buffer = new byte[bufferSize];

		IndexInput in = directory.openInput(file);

		long total = in.length();
		long start = 0;

		while (start < total) {

			long left = total - start;
			long lenght = left > bufferSize ? bufferSize : left;

			in.readBytes(buffer, 0, (int) lenght);
			dest.write(buffer, 0, (int) lenght);

			start += lenght;

		}

		in.close();

	}

}
