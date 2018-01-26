package tdt4140.gr1800.app.core;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

public interface GeoLocationsPersistence {
	public Collection<GeoLocations> loadLocations(InputStream inputStream) throws Exception;
	public void saveLocations(Collection<GeoLocations> geoLocations, OutputStream outputStream) throws Exception;
}
