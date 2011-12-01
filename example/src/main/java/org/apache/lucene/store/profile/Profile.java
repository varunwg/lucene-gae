package org.apache.lucene.store.profile;

import java.util.logging.Logger;

public class Profile {

	private long start;
	private long end;
	private String method;

	private Logger log = Logger.getLogger(Profile.class.getName());

	public Profile(String method) {
		this.method = method;
	}

	public Profile end() {
		end = System.currentTimeMillis();
		return this;
	}

	public long getTime() {
		return end - start;
	}

	public void log() {
		log.info("Method " + method + " (" + getTime() + ")");
	}

	public void log(String... parans) {
		log.info("Method " + method + " (" + getTime() + ") with parans " + getArray(parans));
	}

	public Profile start() {
		start = System.currentTimeMillis();
		return this;
	}

	private String getArray(String[] parans) {

		StringBuilder builder = new StringBuilder();

		builder.append("[");

		for (String string : parans) {
			builder.append(string).append(",");
		}

		builder.append("]");

		return builder.toString();

	}

}
