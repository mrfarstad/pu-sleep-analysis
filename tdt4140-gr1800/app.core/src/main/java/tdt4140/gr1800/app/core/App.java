package tdt4140.gr1800.app.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import tdt4140.gr1800.app.json.GeoLocationsJsonPersistence;

public class App {

	private GeoLocationsPersistence geoLocationsLoader = new GeoLocationsJsonPersistence();
	
	public void loadGeoLocations(URI uri) throws Exception {
		geoLocations = geoLocationsLoader.loadLocations(uri.toURL().openStream());
	}

	private Collection<GeoLocations> geoLocations;

	public Iterable<String> getGeoLocationNames() {
		Collection<String> names = new ArrayList<String>(geoLocations != null ? geoLocations.size() : 0);
		if (geoLocations != null) {
			for (GeoLocations geoLocations : geoLocations) {
				names.add(geoLocations.getName());
			}
		}
		return names;
	}

	public boolean hasGeoLocations(String name) {
		if (geoLocations != null) {
			for (GeoLocations geoLocations : geoLocations) {
				if (name.equals(geoLocations.getName())) {
					return true;
				}
			}
		}
		return false;
	}

	public GeoLocations getGeoLocations(String name) {
		if (geoLocations != null) {
			for (GeoLocations geoLocations : geoLocations) {
				if (name.equals(geoLocations.getName())) {
					return geoLocations;
				}
			}
		}
		return null;
	}
	
	// 

	private DocumentStorageImpl<Collection<GeoLocations>, File> documentStorage = new DocumentStorageImpl<Collection<GeoLocations>, File>() {

		@Override
		protected Collection<GeoLocations> getDocument() {
			return geoLocations;
		}

		@Override
		protected void setDocument(Collection<GeoLocations> document) {
			geoLocations = document;
		}

		@Override
		protected Collection<GeoLocations> createDocument() {
			return new ArrayList<GeoLocations>();
		}

		@Override
		protected Collection<GeoLocations> loadDocument(File file) throws IOException {
			try {
				return geoLocationsLoader.loadLocations(new FileInputStream(file));
			} catch (Exception e) {
				throw new IOException(e);
			}
		}

		@Override
		protected void storeDocument(Collection<GeoLocations> document, File file) throws IOException {
			try {
				geoLocationsLoader.saveLocations(document, new FileOutputStream(file));
			} catch (Exception e) {
				throw new IOException(e);
			}
		}
	};

	public IDocumentStorage<File> getDocumentStorage() {
		return documentStorage;
	}
}
