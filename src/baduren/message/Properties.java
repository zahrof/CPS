package baduren.message;

import java.util.HashMap;

public class Properties implements java.io.Serializable{
	 HashMap<String, Boolean> booleanProperties = new HashMap<String, Boolean>();
	 HashMap<String, Byte> byteProperties = new HashMap<String, Byte>();
	 HashMap<String, Character> characterProperties = new HashMap<String, Character>();
	 HashMap<String, Double> doubleProperties = new HashMap<String, Double>();
	 HashMap<String, Float> floatProperties = new HashMap<String, Float>();
	 HashMap<String, Integer> integerProperties = new HashMap<String, Integer>();
	 HashMap<String, Long> longProperties = new HashMap<String, Long>();
	 HashMap<String, Short> shortProperties = new HashMap<String, Short>();
	 HashMap<String, String> stringProperties = new HashMap<String, String>();
	 
	
	
	public void putProp(String name, boolean v) {
		this.booleanProperties.put(name,v); 
	}
	public void putProp(String name, byte v) {
		this.byteProperties.put(name,v); 
	} 
	public void putProp(String name, char v) {
		this.characterProperties.put(name,v); 
	} 
	public void putProp(String name, double v) {
		this.doubleProperties.put(name, v); 
	} 
	public void putProp(String name, float v) {
		this.floatProperties.put(name, v); 
	} 
	public void putProp(String name, int v) {
		this.integerProperties.put(name, v); 
	} 
	public void putProp(String name, long v) {
		this.longProperties.put(name, v); 
	} 
	public void putProp(String name, short v) {
		this.shortProperties.put(name, v); 
	} 
	public void putProp(String name, String v) {
		this.stringProperties.put(name, v); 
	} 
	
	
	public boolean getBooleanProp(String name) {
		return this.booleanProperties.get(name); 
	} 
	public byte getByteProp(String name) {
		return this.byteProperties.get(name); 
	} 
	public char getCharProp(String name) {
		return this.characterProperties.get(name); 
	} 
	public double getDoubleProp(String name) {
		return this.doubleProperties.get(name); 
	} 
	public float getFloatProp(String name) {
		return this.floatProperties.get(name); 
	} 
	public int getIntProp(String name) {
		return this.integerProperties.get(name); 
	} 
	public long getLongProp(String name) {
		return this.longProperties.get(name); 
	} 
	public short getShortProp(String name) {
		return this.shortProperties.get(name); 
	} 
	public String getStringProp(String name) {
		return this.stringProperties.get(name); 
	} 
}
