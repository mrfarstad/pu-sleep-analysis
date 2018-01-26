package tdt4140.gr1800.app.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
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
		testLoadLocations(getClass().getResourceAsStream("geoLocations.json"));
	}
	
	private void testLoadLocations(InputStream inputStream) {
		try {
			Collection<GeoLocations> geoLocations = persistence.loadLocations(inputStream);
			Assert.assertEquals(2, geoLocations.size());
			Iterator<GeoLocations> it = geoLocations.iterator();
			GeoLocationsTest.assertGeoLocations(it.next(), new LatLong(63, 10), new LatLong(63.1, 10.1));
			GeoLocationsTest.assertGeoLocations(it.next(), new LatLong(64, 11), new LatLong(64.1, 11.1));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testSaveLocations() {
		Collection<GeoLocations> geoLocations = new ArrayList<GeoLocations>();
		geoLocations.add(new GeoLocations("1", new LatLong(63, 10), new LatLong(63.1, 10.1)));
		geoLocations.add(new GeoLocations("2", new LatLong(64, 11), new LatLong(64.1, 11.1)));
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			persistence.saveLocations(geoLocations, outputStream);
			outputStream.close();
			testLoadLocations(new ByteArrayInputStream(outputStream.toByteArray()));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
}
