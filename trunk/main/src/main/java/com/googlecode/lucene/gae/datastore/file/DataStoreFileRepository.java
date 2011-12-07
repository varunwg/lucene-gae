package com.googlecode.lucene.gae.datastore.file;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.ShortBlob;

public class DataStoreFileRepository {

	private final String			FILE_KIND;
	private final String			FILE_PART_KIND;

	private final DatastoreService	datastore	= DatastoreServiceFactory.getDatastoreService();

	public DataStoreFileRepository(String indexName) {
		FILE_KIND = String.format("_%0$s_DataStoreFile_", indexName);
		FILE_PART_KIND = String.format("_%0$s_DataStoreFilePart_", indexName);
	}

	public void delete(String name) {

		Entity entity = getFileEntityByName(name);

		if (entity != null) {
			List<Key> keys = getPartsKeysForFileName(name);
			keys.add(0, entity.getKey());
			datastore.delete(keys);
		}

	}

	public void deleteAll() {

		List<Key> keys = new ArrayList<Key>();

		Query query1 = new Query(FILE_KIND).setKeysOnly();

		for (Entity entity : datastore.prepare(query1).asIterable()) {
			keys.add(entity.getKey());
		}

		Query query2 = new Query(FILE_PART_KIND).setKeysOnly();

		for (Entity entity : datastore.prepare(query2).asIterable()) {
			keys.add(entity.getKey());
		}

		datastore.delete(keys);

	}

	public DataStoreFile get(String name) {
		return get(name, true);
	}

	public DataStoreFile get(String name, boolean full) {

		Entity entity = getFileEntityByName(name);

		DataStoreFile result = null;

		if (entity != null) {
			result = convertToFile(entity, full);
		}

		return result;

	}

	public List<DataStoreFile> list() {
		return list(true);
	}

	public List<DataStoreFile> list(boolean full) {

		List<DataStoreFile> result = new ArrayList<DataStoreFile>();

		for (String name : listNames()) {
			Entity entity = getFileEntityByName(name);
			result.add(convertToFile(entity, full));
		}

		return result;

	}

	public List<String> listDeletedNames() {

		List<String> result = new ArrayList<String>();

		Query query = new Query(FILE_KIND).addFilter("deleted", FilterOperator.EQUAL, true).setKeysOnly();

		Iterable<Entity> iter = datastore.prepare(query).asIterable();

		for (Entity entity : iter) {
			result.add(entity.getKey().getName());
		}

		return result;

	}

	public List<String> listNames() {

		List<String> result = new ArrayList<String>();

		Query query = new Query(FILE_KIND).addFilter("deleted", FilterOperator.EQUAL, false).setKeysOnly();

		Iterable<Entity> iter = datastore.prepare(query).asIterable();

		for (Entity entity : iter) {
			result.add(entity.getKey().getName());
		}

		return result;

	}

	public void put(DataStoreFile file) {

		List<Entity> entities = convertToEntities(file);

		datastore.put(entities);

	}

	private List<Entity> convertToEntities(DataStoreFile file) {

		List<Entity> entities = new ArrayList<Entity>();

		Entity fEntity = new Entity(FILE_KIND, file.getName());
		fEntity.setProperty("length", file.getLength());
		fEntity.setProperty("lastModified", file.getLastModified());
		fEntity.setProperty("deleted", file.isDeleted());
		fEntity.setProperty("parts", file.getParts().size());

		entities.add(fEntity);

		for (DataStoreFilePart part : file.getParts()) {

			if (part.isModified()) {

				Entity pEntity = new Entity(FILE_PART_KIND, part.getName(), fEntity.getKey());
				pEntity.setProperty("bytes", new ShortBlob(part.getBytes()));
				pEntity.setProperty("length", part.getLength());

				entities.add(pEntity);

			}

		}

		return entities;

	}

	private DataStoreFile convertToFile(Entity entity, boolean full) {

		String fName = entity.getKey().getName();

		DataStoreFile file = new DataStoreFile(fName);
		file.setLength((Long) entity.getProperty("length"));
		file.setLastModified((Long) entity.getProperty("lastModified"));
		file.setDeleted((Boolean) entity.getProperty("deleted"));

		if (full) {

			Long size = (Long) entity.getProperty("parts");

			for (int i = 0; i < size; i++) {

				String pName = fName + "_" + i;

				Entity pEntity = getPartEntityByName(fName, pName);

				DataStoreFilePart part = new DataStoreFilePart(pName);

				Long length = (Long) pEntity.getProperty("length");
				ShortBlob blob = (ShortBlob) pEntity.getProperty("bytes");
				part.setLength(length.intValue());
				part.setBytes(blob.getBytes());

				file.getParts().add(part);

			}

		}

		return file;

	}

	private Entity getEntityByKey(Key key) {
		try {
			return datastore.get(key);
		} catch (EntityNotFoundException e) {
			// não faz nada
		}
		return null;
	}

	private Entity getFileEntityByName(String name) {
		Key key = getFileKey(name);
		return getEntityByKey(key);
	}

	private Key getFileKey(String fileName) {
		return KeyFactory.createKey(FILE_KIND, fileName);
	}

	private Entity getPartEntityByName(String fileName, String partName) {
		Key fKey = getFileKey(fileName);
		Key pKey = getPartKey(fKey, partName);
		return getEntityByKey(pKey);
	}

	private Key getPartKey(Key fileKey, String partName) {
		return KeyFactory.createKey(fileKey, FILE_PART_KIND, partName);
	}

	private List<Key> getPartsKeysForFileName(String fileName) {

		Key fKey = getFileKey(fileName);
		Entity fEntity = getEntityByKey(fKey);

		Long size = (Long) fEntity.getProperty("parts");

		List<Key> keys = new ArrayList<Key>(size.intValue());

		for (int i = 0; i < size; i++) {

			String pName = fileName + "_" + i;
			Key pKey = getPartKey(fKey, pName);
			keys.add(pKey);

		}

		return keys;

	}

}
