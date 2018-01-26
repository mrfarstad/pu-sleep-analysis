package tdt4140.gr1800.app.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import tdt4140.gr1800.app.core.GeoLocations;
import tdt4140.gr1800.app.core.LatLong;

public class GeoLocationsJsonSerializer extends StdSerializer<GeoLocations> {
	
	public static final String LONGITUDE_FIELD_NAME = "longitude";
	public static final String LATITUDE_FIELD_NAME = "latitude";
	public static final String LOCATIONS_FIELD_NAME = "locations";
	public static final String PATH_FIELD_NAME = "path";
	public static final String NAME_FIELD_NAME = "name";

	public GeoLocationsJsonSerializer() {
		super(GeoLocations.class);
	}

	@Override
	public void serialize(GeoLocations geoLocations, JsonGenerator jsonGen, SerializerProvider serProvider) throws IOException {
		jsonGen.writeStartObject();
		jsonGen.writeFieldName(NAME_FIELD_NAME);
		jsonGen.writeString(geoLocations.getName());
		jsonGen.writeFieldName(PATH_FIELD_NAME);
		jsonGen.writeBoolean(geoLocations.isPath());
		jsonGen.writeFieldName(LOCATIONS_FIELD_NAME);
		jsonGen.writeStartArray();
		for (LatLong latLon : geoLocations) {
			jsonGen.writeStartArray();
			jsonGen.writeNumber(latLon.latitude);
			jsonGen.writeNumber(latLon.longitude);
			jsonGen.writeEndArray();
		}
		jsonGen.writeEndArray();
		jsonGen.writeEndObject();
	}
}
