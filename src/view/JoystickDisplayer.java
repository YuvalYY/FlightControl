package view;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class JoystickDisplayer extends Canvas {
	
	private static final double SMALL_RADIUS=80;
	private static final double LARGE_RADIUS=200;
	
	private double pressX,pressY;
	private GraphicsContext gc;
	

	public JoystickDisplayer() {
		super();
		gc=this.getGraphicsContext2D();
		this.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				redraw();
			}});
		this.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				redraw(event.getX()-pressX, event.getY()-pressY);
			}
			
		});
		this.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				pressX=event.getX();
				pressY=event.getY();
			}
			
		});
		redraw();
	}
	
	private void redrawLargeCircle() {
		gc.setFill(Color.GRAY);
		gc.fillOval((this.getWidth()-LARGE_RADIUS)/2, (this.getHeight()-LARGE_RADIUS)/2, LARGE_RADIUS, LARGE_RADIUS);
	}
	
	private void redrawSmallCircle(double smallX,double smallY) {
		gc.setFill(Color.BLACK);
		gc.fillOval((this.getWidth()-SMALL_RADIUS+smallX)/2, (this.getHeight()-SMALL_RADIUS+smallY)/2, SMALL_RADIUS, SMALL_RADIUS);
	}
	
	public void redraw(double smallX,double smallY) {
		double centerDist=Math.sqrt(Math.pow(smallX,2)+Math.pow(smallY, 2));
		if(centerDist<=LARGE_RADIUS-SMALL_RADIUS) {
			redrawLargeCircle();
			redrawSmallCircle(smallX,smallY);
		}
	}
	
	public void redraw() {
		redraw(0,0);
	}
	
}