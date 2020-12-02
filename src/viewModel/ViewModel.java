package viewModel;

import java.util.Observable;
import java.util.Observer;
import model.Update;
import model.Model;

public class ViewModel extends Observable implements Observer{
	
	private Model mModel;
	private static final double JOYSTICK_NORMALIZER=120.0;
	
	public ViewModel(Model m) {
		mModel=m;
		m.addObserver(this);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		Update update=(Update)arg1;
		switch (update.getId()) {
		case "Path":
			update.setArg(update.getArg().toString().split(","));
			
		}
		notifyAllObservers(update);
	}
	
	public void vmLoadData() {
		
	}
	
	public void vmConnectToSimulator(String ip, int port) {
		String[] s= {
				"connect "+ip+" "+port,
				"var breaks = bind \"/controls/flight/speedbrake\"",
				"var throttle = bind \"/controls/engines/current-engine/throttle\"",
				"var heading = bind \"/instrumentation/heading-indicator/indicated-heading-deg\"",
				"var airspeed = bind \"/instrumentation/airspeed-indicator/indicated-speed-kt\"",
				"var roll = bind \"/instrumentation/attitude-indicator/indicated-roll-deg\"",
				"var pitch = bind \"/instrumentation/attitude-indicator/internal-pitch-deg\"",
				"var rudder = bind \"/controls/flight/rudder\"",
				"var aileron = bind \"/controls/flight/aileron\"",
				"var elevator = bind \"/controls/flight/elevator\"",
				"var alt = bind \"/instrumentation/altimeter/indicated-altitude-ft\""};
				/*breaks = 0
				throttle = 1
				var h0 = heading
				sleep 5000
				while alt < 1000 {
					rudder = (h0 - heading) / 180
					aileron= - roll / 70
					elevator = pitch / 50
					sleep 150
				}
				print "done"*/
		mModel.interpret(s);
	}
	
	public void vmCalculatePath(String ip, int port, int[][] matrix, int start, int end) {
		String sStart=start/matrix.length+","+start%matrix.length;
		String sEnd=end/matrix.length+","+end%matrix.length;
		mModel.calculatePath(ip, port, intMatTODoubleMat(matrix), sStart, sEnd);
	}
	public void vmMapPress() {}
	
	public void vmJoystickChanged(double x, double y) {
		String[] s= {
				"aileron = "+x/JOYSTICK_NORMALIZER, 
				"elevator = "+y/JOYSTICK_NORMALIZER};
		mModel.interpret(s);
	}
	
	public void vmThrottleChanged(double value) {
		String[] s= {"throttle = "+value};
		mModel.interpret(s);
	}
	
	public void vmRudderChanged(double value) {
		String[] s= {"rudder = "+value};
		mModel.interpret(s);
	}
	
	public void notifyAllObservers(Update update) {
		setChanged();
		notifyObservers(update);
	}
	
	public double[][] intMatTODoubleMat(int[][] intMat){
		double[][] doubleMat=new double[intMat.length][intMat[0].length];
		for (int i = 0; i < doubleMat.length; i++) {
			for (int j = 0; j < doubleMat[i].length; j++) {
				doubleMat[i][j]=(double)intMat[i][j];
			}
		}
		return doubleMat;
	}
	
	public void vmManualToAuto(String[] lines) {
		//TODO maybe do a new function in model which opens a thread and interprets things for this, maybe move the interpret method to the old myinterpreter class, might be better
		mModel.interpret(lines);

	}
	
	public void vmAutoToManual(double throttle, double rudder) {
		String lines[]= {
				"throttle = "+throttle,
				"rudder = "+rudder,
				"elevator = 0",
				"aileron = 0"
		};
		mModel.interpret(lines);
	}
}
