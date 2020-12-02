package interpreter.parser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import interpreter.client.MyClient;

public class SymbolTable {
	private Map<String, MyVar> map;
	private Map<String, MyVar> bindMap;
	private Map<String, String> bindNameMap;
	private static volatile SymbolTable instance=null;
	
	private class MyVar extends Observable{
		private Object val;
		
		public MyVar(Object val) {
			this.val=val;
		}
		
		public Object getVar() {
			return val;
		}
		public void setVar(Object val) {
			this.val=val;
			setChanged();
		}
	}
	
	private SymbolTable()
	{
		map=new HashMap<String,MyVar>();
		bindMap=new HashMap<String, MyVar>();
		bindNameMap=new HashMap<String, String>();
		map.put("returnValue", new MyVar(0.0));
	}
	
	public static SymbolTable getInstance(){
		SymbolTable result=instance;
		if(result==null) {
			synchronized(SymbolTable.class) {
				result=instance;
				if(result==null) {
					instance=result=new SymbolTable();
				}
			}
		}
		return result;
	}
	
	public Object getVal(String word){
		Object val;
		try { val=Double.parseDouble(word); }
		catch (NumberFormatException e) { val=map.get(word).getVar(); }
		return val;
	}
	
	public void setVal(String name, Object value) {
		if(map.containsKey(name)) {
			map.get(name).setVar(value);
			List<String> arguments=new LinkedList<>();
			arguments.add(bindNameMap.get(name));
			arguments.add(""+value);
			map.get(name).notifyObservers(arguments);
		}
		else
			map.put(name, new MyVar(value));
	}
	
	public void bind(String var,String bindTo) {
		if(!bindMap.containsKey(bindTo)) {
			bindMap.put(bindTo, new MyVar(0.0));
			bindMap.get(bindTo).addObserver(MyClient.getInstance());
		}
		if(map.containsKey(var))
			map.replace(var, bindMap.get(bindTo));
		else
			map.put(var, bindMap.get(bindTo));
		if(bindNameMap.containsKey(var))
			bindNameMap.replace(var, bindTo);
		else
			bindNameMap.put(var, bindTo);
	}
	
	public void addToBindMap(String name,Object val) {
		System.out.println(name+" "+val.toString());
		if(!bindMap.containsKey(name)) {
			bindMap.put(name, new MyVar(val));
			bindMap.get(name).addObserver(MyClient.getInstance());
		}
		else {
			bindMap.get(name).setVar(val);
			bindMap.get(name).notifyObservers();
		}
	}
	public void clear() {
		bindNameMap.clear();
		bindMap.clear();
		map.clear();
	}
}