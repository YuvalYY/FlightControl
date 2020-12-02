package model;

public class CSVFile {
	double mLongitude;
	double mLatitude;
	double mDistanceDelta;
	int[][] mMapData;
	
	public CSVFile(double mLongitude, double mLatitude, double mDistanceDelta, int[][] mMapData) {
		super();
		this.mLongitude = mLongitude;
		this.mLatitude = mLatitude;
		this.mDistanceDelta = mDistanceDelta;
		this.mMapData = mMapData;
	}

	public double getmLongitude() {
		return mLongitude;
	}

	public double getmLatitude() {
		return mLatitude;
	}

	public double getmDistanceDelta() {
		return mDistanceDelta;
	}

	public int[][] getmMapData() {
		return mMapData;
	}

	
}
