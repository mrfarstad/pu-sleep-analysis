package tdt4140.gr1800.app.core;

public class LatLong {
	
	public final double latitude, longitude;

	public LatLong(double latitude, double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public final static String SEPARATOR = ",";

	@Override
	public String toString() {
		return latitude + SEPARATOR + longitude;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		LatLong other = (LatLong) obj;
		return (Double.doubleToLongBits(latitude) == Double.doubleToLongBits(other.latitude) &&
				Double.doubleToLongBits(longitude) == Double.doubleToLongBits(other.longitude));
	}

	public static LatLong valueOf(String s) {
		return valueOf(s, SEPARATOR);
	}
	
	public static LatLong valueOf(String s, String sep) {
		int pos = s.indexOf(sep);
		if (pos < 0) {
			throw new IllegalArgumentException("No '" + sep + "' in "  + s);
		}
		double lat = Double.valueOf(s.substring(0, pos).trim());
		double lon = Double.valueOf(s.substring(pos + sep.length()).trim());
		
		return new LatLong(lat, lon);
	}
	
	/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::                                                                         :*/
	/*::  This routine calculates the distance between two points (given the     :*/
	/*::  latitude/longitude of those points). It is being used to calculate     :*/
	/*::  the distance between two locations using GeoDataSource (TM) products   :*/
	/*::                                                                         :*/
	/*::  Definitions:                                                           :*/
	/*::    South latitudes are negative, east longitudes are positive           :*/
	/*::                                                                         :*/
	/*::  Passed to function:                                                    :*/
	/*::    lat1, lon1 = Latitude and Longitude of point 1 (in decimal degrees)  :*/
	/*::    lat2, lon2 = Latitude and Longitude of point 2 (in decimal degrees)  :*/
	/*::  Worldwide cities and other features databases with latitude longitude  :*/
	/*::  are available at http://www.geodatasource.com                          :*/
	/*::                                                                         :*/
	/*::  For enquiries, please contact sales@geodatasource.com                  :*/
	/*::                                                                         :*/
	/*::  Official Web site: http://www.geodatasource.com                        :*/
	/*::                                                                         :*/
	/*::           GeoDataSource.com (C) All Rights Reserved 2015                :*/
	/*::                                                                         :*/
	/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	public static double distance(double lat1, double lon1, double lat2, double lon2) {
		if (lon1 == lon2 && lat1 == lat2) {
			return 0.0;
		}
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		// convert to degrees
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		// convert to meters 
		dist = dist * 1609.344;
		return dist;
	}

	public static double distance(LatLong latLong1, LatLong latLong2) {
		return distance(latLong1.latitude, latLong1.longitude, latLong2.latitude, latLong2.longitude);
	}

	public double distance(LatLong latLong2) {
		return distance(latitude, longitude, latLong2.latitude, latLong2.longitude);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts decimal degrees to radians						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts radians to decimal degrees						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}
}
