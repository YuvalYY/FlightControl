package view;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

//TODO change mGcontext to be a data memeber of the class
//TODO also make w and h into memebrs

public class MapDisplayer extends Canvas{
	private int[][] mMapData;
	private Color[] mColors;
	private int mCurrentPosition=0;
	private int mEnd=-1;
	private GraphicsContext mGcontext;
	String[] mRoute;
	
	//C'tors -------------------------------------------------------------------
	
	public MapDisplayer() {
		mGcontext=getGraphicsContext2D();
	}
	
	public MapDisplayer(int[][] mapData) {
		super();
		mGcontext=getGraphicsContext2D();
		setMapData(mapData);
		
	}
	
	//Getters and Setters -----------------------------------------------------
	
	public int[][] getMapData(){
		return this.mMapData;
	}
	
	public void setMapData(int[][] mapData) {
		this.mMapData = mapData;
		calculateColors(mapData);
		redraw();
	}
	
	public int getCurrentPosition() {
		return this.mCurrentPosition;
	}

	public int getEnd() {
		return this.mEnd;
	}
	
	public void setCurrentPosition(int mStart) {
		this.mCurrentPosition = mStart;
		redraw();
	}
		
	
	public void setRoute(String[] mRoute) {
		this.mRoute = mRoute;
		redraw();
	}

	public void setEnd(int mEnd) {
		this.mEnd = mEnd;
		redraw();
	}
	
	//Redraw Methods --------------------------------------------------------------------------
	
	public void redraw() {
		if(mMapData!=null) {
			double w=getWidth()/mMapData[0].length;
			double h=getHeight()/mMapData.length;
			redrawColor(w,h);
			if(mCurrentPosition!=-1)
				redrawPoint(w, h, true);
			if(mEnd!=-1)
				redrawPoint(w, h, false);
			redrawNumbers(w, h);
			if(mRoute!=null)
				redrawLine(w, h);
		}
	}
	
	private void redrawPoint(double w, double h, boolean isStart) {
		mGcontext.setFill(Color.PURPLE);
		double x=(mEnd%mMapData.length)*w;
		double y=(mEnd/mMapData.length)*h;
		if(isStart) {
			mGcontext.setFill(Color.BLUE);
			x=(mCurrentPosition%mMapData.length)*w;
			y=(mCurrentPosition/mMapData.length)*h;
		}
		mGcontext.fillRect(x, y, w, h);
	}
	
	private void redrawColor(double w, double h) {
		for (int i = 0; i < mMapData.length; i++) {
			for (int j = 0; j < mMapData[i].length; j++) {
				mGcontext.setFill(mColors[mMapData[i][j]]);
				mGcontext.fillRect(j*w, i*h, w, h);
			}
		}
	}
	
	private void redrawLine(double w, double h) {
		double x=(mCurrentPosition%mMapData.length)*w+w/2-2;
		double y=(mCurrentPosition/mMapData.length)*h+h/2-2;
		mGcontext.setFill(Color.GRAY);
		for(String direction : mRoute) {
			switch (direction) {
			case "Up":
				mGcontext.fillRect(x, y-h, 4, h);
				y-=h;
				break;
			case "Down":
				mGcontext.fillRect(x, y, 4, h);
				y+=h;
				break;
			case "Left":
				mGcontext.fillRect(x-w, y, w, 4);
				x-=w;
				break;
			case "Right":
				mGcontext.fillRect(x, y, w, 4);
				x+=w;
				break;
			}
		}
	}
	
	
	private void redrawNumbers(double w, double h) {
		mGcontext.setFill(Color.BLACK);
		mGcontext.setTextAlign(TextAlignment.CENTER);
		mGcontext.setTextBaseline(VPos.CENTER);
		for (int i = 0; i < mMapData.length; i++) {
			for (int j = 0; j < mMapData[i].length; j++) {
				mGcontext.fillText(String.valueOf(mMapData[i][j]),j*w+w/2, i*h+h/2);
			}
		}
	}
	
	//Event Methods ---------------------------------------------------------------------------------------
	
	public void onMouseClick(double x, double y) {
    	double w=getWidth()/mMapData[0].length;
		double h=getHeight()/mMapData.length;
		int pos=((int)(y/h))*mMapData[0].length+(int)(x/w);
		setEnd(pos);
    }
	
	public void continueOneTile() {
		
	}
	
	//Utility Functions --------------------------------------------------------------------------------------
	
	public void calculateColors(int[][] matrix) {
		double increment=255/((double)findMax(matrix));
		mColors=new Color[findMax(matrix)+1];
		for (int i = 0; i < mColors.length; i++) {
			mColors[i]=Color.web(String.format("#%02x%02x%02x", (int)(255-i*increment), (int)(i*increment), 0));
		}
		
	}
	
	public int findMax(int[][] matrix) {
		int max=-1;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if(matrix[i][j]>max)max=matrix[i][j];
			}
		}
		return max;
	}
	
	
	public void resetRoute() {
		this.mCurrentPosition=0;
		this.mEnd=-1;
		this.mRoute=null;
		redraw();
	}	
}