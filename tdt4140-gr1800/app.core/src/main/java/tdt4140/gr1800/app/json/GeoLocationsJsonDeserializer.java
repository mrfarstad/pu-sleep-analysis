package tdt4140.gr1800.app.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import tdt4140.gr1800.app.core.GeoLocations;
import tdt4140.gr1800.app.core.LatLong;

public class GeoLocationsJsonDeserializer extends StdDeserializer<GeoLocations> {
	
	public GeoLocationsJsonDeserializer() {
		super(GeoLocations.class);
	}
	
	@Override
	public GeoLocations deserialize(JsonParser jsonParser, DeserializationContext deserContext) throws IOException, JsonProcessingException {
		JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
		return deserialize(jsonNode);
	}
	
	private GeoLocations deserialize(JsonNode jsonNode) throws IOException, JsonProcessingException {
		if (jsonNode instanceof ObjectNode) {
			ObjectNode objectNode = (ObjectNode) jsonNode;
			String name = objectNode.get(GeoLocationsJsonSerializer.NAME_FIELD_NAME).asText();
			GeoLocations geoLocations = new GeoLocations(name);
			JsonNode pathNode = objectNode.get(GeoLocationsJsonSerializer.PATH_FIELD_NAME);
			geoLocations.setPath(pathNode != null && pathNode.asBoolean(false));
			JsonNode locationsNode = objectNode.get(GeoLocationsJsonSerializer.LOCATIONS_FIELD_NAME);
			if (locationsNode instanceof ArrayNode) {
				for (JsonNode locationNode : (ArrayNode) locationsNode) {
					LatLong latLong = decodeLatLong(locationNode);
					geoLocations.addLocation(latLong);
				}
			} else {
				LatLong latLong = decodeLatLong(locationsNode);
				geoLocations.addLocation(latLong);				
			}
			return geoLocations;
		}
		return null;
	}

	private LatLong decodeLatLong(JsonNode locationNode) {
		LatLong latLong = null;
		if (locationNode instanceof ObjectNode) {
			ObjectNode objectNode = (ObjectNode) locationNode;
			if (objectNode.has(GeoLocationsJsonSerializer.LATITUDE_FIELD_NAME) && objectNode.has(GeoLocationsJsonSerializer.LONGITUDE_FIELD_NAME)) {
				double lat = objectNode.get(GeoLocationsJsonSerializer.LATITUDE_FIELD_NAME).asDouble();
				double lon = objectNode.get(GeoLocationsJsonSerializer.LONGITUDE_FIELD_NAME).asDouble();
				latLong = new LatLong(lat, lon);
			}
		} else if (locationNode instanceof ArrayNode) {
			ArrayNode arrayNode = (ArrayNode) locationNode;
			if (arrayNode.size() == 2) {
				double lat = arrayNode.get(0).asDouble();
				double lon = arrayNode.get(1).asDouble();
				latLong = new LatLong(lat, lon);			
			}
		}
		return latLong;
	}
}
