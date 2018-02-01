package tdt4140.gr1800.app.core;

import org.junit.Assert;
import org.junit.Test;

public class LatLongTest {
	
	@Test
	public void testToString() {
		LatLong latLong = new LatLong(63.0, 10.0);
		Assert.assertEquals(Double.toString(63) + "," + Double.toString(10), latLong.toString());
	}
	
	@Test
	public void testValueOf() {
		testLatLong(LatLong.valueOf("63.0, 10.0"), 63.0, 10.0);
		testLatLong(LatLong.valueOf("63.0, 10.0", ","), 63.0, 10.0);
		testLatLong(LatLong.valueOf("63.0; 10.0", ";"), 63.0, 10.0);
	}

	private void testLatLong(LatLong latLong, double lat, double lon) {
		Assert.assertEquals(lat, latLong.latitude, 0.0);
		Assert.assertEquals(lon, latLong.longitude, 0.0);
	}

	@Test
	public void testEquals() {
		Assert.assertTrue(new LatLong(63.0, 10.0).equals(new LatLong(63.0, 10.0)));
		Assert.assertFalse(new LatLong(10.0, 63.0).equals(new LatLong(63.0, 10.0)));
		Assert.assertFalse(new LatLong(10.0, 63.0).equals(null));
		Assert.assertFalse(new LatLong(10.0, 63.0).equals("10.0, 63.0"));
	}
}
