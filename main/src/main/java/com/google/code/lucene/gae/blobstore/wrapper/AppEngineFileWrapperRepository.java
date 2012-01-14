package com.google.code.lucene.gae.blobstore.wrapper;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.blobstore.*;
import com.google.appengine.api.datastore.*;

public class AppEngineFileWrapperRepository {

	private static final String							KIND		= "_AppEngineFileWrapper_";

	private DatastoreService							datastore	= DatastoreServiceFactory.getDatastoreService();
	private BlobstoreService							blobstorage	= BlobstoreServiceFactory.getBlobstoreService();

	private static final AppEngineFileWrapperRepository	INSTANCE	= new AppEngineFileWrapperRepository();

	public static AppEngineFileWrapperRepository getInstance() {
		return INSTANCE;
	}

	private AppEngineFileWrapperRepository() {
	}

	public void delete(String name) {

		AppEngineFileWrapper fileWrapper = get(name);

		if (fileWrapper != null) {

			if (!fileWrapper.isNew()) {
				BlobKey key = new BlobKey(fileWrapper.getBlobKey());
				blobstorage.delete(key);
			}

			Entity entity = getEntityByName(name);
			datastore.delete(entity.getKey());

		}

	}

	public AppEngineFileWrapper get(String name) {

		Entity entity = getEntityByName(name);

		AppEngineFileWrapper result = null;

		if (entity != null) {
			result = convertToGAEFile(entity);
		}

		return result;

	}

	public List<AppEngineFileWrapper> list() {

		List<AppEngineFileWrapper> result = new ArrayList<AppEngineFileWrapper>();

		Query query = new Query(KIND);

		Iterable<Entity> iter = datastore.prepare(query).asIterable();

		for (Entity entity : iter) {
			result.add(convertToGAEFile(entity));
		}

		return result;

	}

	public void put(AppEngineFileWrapper file) {

		Entity entity = convertToEntity(file);

		datastore.put(entity);

	}

	private Entity convertToEntity(AppEngineFileWrapper file) {

		String name = file.getName();

		Entity entity = new Entity(KIND, name);
		entity.setProperty("blobKey", file.getBlobKey());
		entity.setProperty("modified", file.getLastModified());
		entity.setProperty("length", file.getLength());

		return entity;

	}

	private AppEngineFileWrapper convertToGAEFile(Entity entity) {

		String name = entity.getKey().getName();

		AppEngineFileWrapper file = new AppEngineFileWrapper(name);

		file.setLastModified((Long) entity.getProperty("modified"));
		file.setLength((Long) entity.getProperty("length"));
		file.setBlobKey((String) entity.getProperty("blobKey"));

		return file;

	}

	private Entity getEntityByName(String name) {
		Key key = KeyFactory.createKey(KIND, name);
		try {
			return datastore.get(key);
		} catch (EntityNotFoundException e) {
			// não faz nada
		}
		return null;
	}

}
