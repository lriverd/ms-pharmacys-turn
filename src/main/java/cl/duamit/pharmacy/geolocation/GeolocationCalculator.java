package cl.duamit.pharmacy.geolocation;

import cl.duamit.pharmacy.model.Coordinates;

public class GeolocationCalculator {

	private static final int EARTH_RADIUS = 6371;

	/**
	 * Calculate distance between two points in latitude and longitude taking
	 * into account height difference. If height is null then considerate a plain distance
	 *
	 * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
	 * el2 End altitude in meters
	 * @returns Distance in Meters
	 */
	public static double distanceBetwenPoints(Coordinates from, Coordinates to) {

		double latDistance = Math.toRadians(to.getLatitude() - from.getLatitude());
		double lonDistance = Math.toRadians(to.getLongitude() - from.getLongitude());
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
			+ Math.cos(Math.toRadians(from.getLatitude())) * Math.cos(Math.toRadians(to.getLatitude()))
			* Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		double distanceInMeters = Math.abs(EARTH_RADIUS * c * 1000);

		double height = from.getAltitude() - to.getAltitude();

		return Math.sqrt(Math.pow(distanceInMeters, 2) + Math.pow(height, 2));
	}
}
