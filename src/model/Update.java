package model;

public class Update {
	
	String mId;
	Object mArg;
	
	public Update(String mId, Object mArg) {
		this.mId = mId;
		this.mArg = mArg;
	}
	public String getId() {
		return mId;
	}
	public Object getArg() {
		return mArg;
	}
	
	public void setArg(Object arg) {
		this.mArg=arg;
	}
	
}
