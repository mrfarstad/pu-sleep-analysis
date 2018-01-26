package tdt4140.gr1800.app.json;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import tdt4140.gr1800.app.core.GeoLocations;
import tdt4140.gr1800.app.core.GeoLocationsPersistence;

public class GeoLocationsJsonPersistence implements GeoLocationsPersistence {

	private final ObjectMapper objectMapper;

	public GeoLocationsJsonPersistence() {
		objectMapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addSerializer(new GeoLocationsJsonSerializer());
		module.addDeserializer(GeoLocations.class, new GeoLocationsJsonDeserializer());
		objectMapper.registerModule(module);
	}
	
	@Override
	public Collection<GeoLocations> loadLocations(InputStream inputStream) throws Exception {
		return objectMapper.readValue(inputStream, objectMapper.getTypeFactory().constructCollectionType(List.class, GeoLocations.class));
	}

	@Override
	public void saveLocations(Collection<GeoLocations> geoLocations, OutputStream outputStream) throws Exception {
		objectMapper.writeValue(outputStream, geoLocations);
	}
}
