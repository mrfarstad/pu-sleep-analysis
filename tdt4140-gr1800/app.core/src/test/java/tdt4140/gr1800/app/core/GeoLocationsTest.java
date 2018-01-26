package tdt4140.gr1800.app.core;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GeoLocationsTest {

	private GeoLocations geoLocations;
	
	@Before
	public void setUp() {
		geoLocations = new GeoLocations();
	}

	@Test
	public void testGeoLocations() {
		Assert.assertEquals(0, geoLocations.size());
		Assert.assertEquals(1, new GeoLocations(new LatLong(0, 0)).size());
		Assert.assertEquals(2, new GeoLocations(new LatLong(0, 0), new LatLong(0, 0)).size());
		Assert.assertEquals(2, new GeoLocations(new LatLong(0, 0), new LatLong(1, 1)).size());
	}

	private void assertGeoLocations(LatLong...latLongs) {
		assertGeoLocations(geoLocations, latLongs);
	}

	public static void assertGeoLocations(GeoLocations geoLocations, LatLong...latLongs) {
		Iterator<LatLong> it = geoLocations.iterator();
		Assert.assertEquals(latLongs.length, geoLocations.size());
		int pos = 0;
		while (it.hasNext()) {
			Assert.assertEquals(latLongs[pos], it.next());
			pos++;
		}
	}

	@Test
	public void testAddLocation() {
		Assert.assertEquals(0, geoLocations.size());
		LatLong latLong1 = new LatLong(0, 0);
		geoLocations.addLocation(latLong1);
		assertGeoLocations(latLong1);

		geoLocations.addLocation(new LatLong(0, 0));
		assertGeoLocations(latLong1, latLong1);

		LatLong latLong2 = new LatLong(1, 1);
		geoLocations.addLocation(latLong2);
		assertGeoLocations(latLong1, latLong1, latLong2);
	}

	@Test
	public void testFindLocationsNearby() {
		LatLong latLong = new LatLong(0, 0);
		Assert.assertTrue(geoLocations.findLocationsNearby(latLong, 0).isEmpty());
		geoLocations.addLocation(latLong);
		Collection<LatLong> locationsNearby = geoLocations.findLocationsNearby(latLong, 0);
		Assert.assertEquals(1, locationsNearby.size());
		Assert.assertEquals(latLong, geoLocations.iterator().next()); 
	}
	
	@Test
	public void testFindNearestLocation() {
		LatLong latLong = new LatLong(0, 0);
		Assert.assertNull(geoLocations.findNearestLocation(latLong));
		geoLocations.addLocation(latLong);
		LatLong nearestlocations = geoLocations.findNearestLocation(latLong);
		Assert.assertEquals(latLong, nearestlocations); 
	}
	
	@Test
	public void testRemoveSameLocations() {
		GeoLocations geoLocations1 = new GeoLocations(new LatLong(0, 0), new LatLong(1, 1));
		Assert.assertEquals(2, geoLocations1.size());
		geoLocations1.removeLocations(new LatLong(0, 0), 0.0);
		Assert.assertEquals(1, geoLocations1.size());
		geoLocations1.removeLocations(new LatLong(1, 1), 0.0);
		Assert.assertEquals(0, geoLocations1.size());		
		GeoLocations geoLocations2 = new GeoLocations(new LatLong(0, 0), new LatLong(0, 0));
		Assert.assertEquals(2, geoLocations2.size());
		geoLocations2.removeLocations(new LatLong(0, 0), 0.0);
		Assert.assertEquals(0, geoLocations2.size());
	}
}
