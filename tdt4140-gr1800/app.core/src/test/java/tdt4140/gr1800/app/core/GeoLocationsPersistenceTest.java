package tdt4140.gr1800.app.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import tdt4140.gr1800.app.json.GeoLocationsJsonPersistence;

public class GeoLocationsPersistenceTest {

	private GeoLocationsPersistence persistence;
	
	@Before
	public void setUp() {
		persistence = new GeoLocationsJsonPersistence();
	}
	
	@Test
	public void testLoadLocations() {
		try {
			Collection<GeoLocations> geoLocations = persistence.loadLocations(getClass().getResourceAsStream("geoLocations.json"));
			testGeoLocationsDotJson(geoLocations);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	public static Collection<GeoLocations> createGeoLocationsDotJson() {
		return Arrays.asList(
			new GeoLocations("1", new LatLong(63, 10), new LatLong(63.1, 10.1)),
			new GeoLocations("2", new LatLong(64, 11), new LatLong(64.1, 11.1))
		);
	}
	
	public static void testGeoLocationsDotJson(Collection<GeoLocations> geoLocations) {
		Assert.assertEquals(2, geoLocations.size());
		Iterator<GeoLocations> it = geoLocations.iterator();
		GeoLocationsTest.assertGeoLocations(it.next(), new LatLong(63, 10), new LatLong(63.1, 10.1));
		GeoLocationsTest.assertGeoLocations(it.next(), new LatLong(64, 11), new LatLong(64.1, 11.1));
	}

	@Test
	public void testSaveLocations() {
		Collection<GeoLocations> geoLocations = createGeoLocationsDotJson();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			persistence.saveLocations(geoLocations, outputStream);
			outputStream.close();
			Collection<GeoLocations> geoLocations2 = persistence.loadLocations(new ByteArrayInputStream(outputStream.toByteArray()));
			testGeoLocationsDotJson(geoLocations2);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
}
