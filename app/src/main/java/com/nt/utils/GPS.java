package com.nt.utils;

public class GPS {

	// 计算两点距离

	private final static double EARTH_RADIUS = 6378137.0;

	public static double getDistance(double from_lat, double from_lng, double to_lat,
			double to_lng) {

		double radLat1 = (from_lat * Math.PI / 180.0);

		double radLat2 = (to_lat * Math.PI / 180.0);

		double a = radLat1 - radLat2;

		double b = (from_lng - to_lng) * Math.PI / 180.0;

		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)

		+ Math.cos(radLat1) * Math.cos(radLat2)

		* Math.pow(Math.sin(b / 2), 2)));

		s = s * EARTH_RADIUS;

		s = Math.round(s * 10000) / 10000;

		return s;

	}
	
	public static void main(String[] args) {
		
		double from_lat = 106.522600;
		double from_lng = 29.617500;
		double to_lat = 106.522611;
		double to_lng = 29.617538;
		
		double s = getDistance(from_lat, from_lng, to_lat, to_lng);
	}

}
