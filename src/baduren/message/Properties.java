package baduren.message;

import java.util.HashMap;

/**
 * The type Properties.
 */
public class Properties implements java.io.Serializable{
	/**
	 * The Boolean properties.
	 */
	HashMap<String, Boolean> booleanProperties = new HashMap<String, Boolean>();
	/**
	 * The Byte properties.
	 */
	HashMap<String, Byte> byteProperties = new HashMap<String, Byte>();
	/**
	 * The Character properties.
	 */
	HashMap<String, Character> characterProperties = new HashMap<String, Character>();
	/**
	 * The Double properties.
	 */
	HashMap<String, Double> doubleProperties = new HashMap<String, Double>();
	/**
	 * The Float properties.
	 */
	HashMap<String, Float> floatProperties = new HashMap<String, Float>();
	/**
	 * The Integer properties.
	 */
	HashMap<String, Integer> integerProperties = new HashMap<String, Integer>();
	/**
	 * The Long properties.
	 */
	HashMap<String, Long> longProperties = new HashMap<String, Long>();
	/**
	 * The Short properties.
	 */
	HashMap<String, Short> shortProperties = new HashMap<String, Short>();
	/**
	 * The String properties.
	 */
	HashMap<String, String> stringProperties = new HashMap<String, String>();


	/**
	 * Put prop.
	 *
	 * @param name the name
	 * @param v    the value
	 */
	public void putProp(String name, boolean v) {
		this.booleanProperties.put(name,v); 
	}

	/**
	 * Put prop.
	 *
	 * @param name the name
	 * @param v    the valuealue
	 */
	public void putProp(String name, byte v) {
		this.byteProperties.put(name,v); 
	}

	/**
	 * Put prop.
	 *
	 * @param name the name
	 * @param v    the value
	 */
	public void putProp(String name, char v) {
		this.characterProperties.put(name,v); 
	}

	/**
	 * Put prop.
	 *
	 * @param name the name
	 * @param v    the value
	 */
	public void putProp(String name, double v) {
		this.doubleProperties.put(name, v); 
	}

	/**
	 * Put prop.
	 *
	 * @param name the name
	 * @param v    the value
	 */
	public void putProp(String name, float v) {
		this.floatProperties.put(name, v); 
	}

	/**
	 * Put prop.
	 *
	 * @param name the name
	 * @param v    the value
	 */
	public void putProp(String name, int v) {
		this.integerProperties.put(name, v); 
	}

	/**
	 * Put prop.
	 *
	 * @param name the name
	 * @param v    the value
	 */
	public void putProp(String name, long v) {
		this.longProperties.put(name, v); 
	}

	/**
	 * Put prop.
	 *
	 * @param name the name
	 * @param v    the value
	 */
	public void putProp(String name, short v) {
		this.shortProperties.put(name, v); 
	}

	/**
	 * Put prop.
	 *
	 * @param name the name
	 * @param v    the value
	 */
	public void putProp(String name, String v) {
		this.stringProperties.put(name, v); 
	}


	/**
	 * Gets boolean prop.
	 *
	 * @param name the name
	 * @return the boolean prop
	 */
	public boolean getBooleanProp(String name) {
		/*
		Nous devons le spécifier ainsi car la méthode get dans une map renvoie null ou la valeur associé
		à la clé name.
		 */
		System.out.println("res ");
		if (this.booleanProperties.containsKey(name)) {
			boolean res = this.booleanProperties.get(name);
			System.out.println("res " + res);
			if ((res == true) || (res == false)) {
				System.out.println("res " + res);
				return res;
			}
		}
		System.out.println("res y est pas");
		return false;
	}

	/**
	 * Gets byte prop.
	 *
	 * @param name the name
	 * @return the byte prop
	 */
	public byte getByteProp(String name) {
		if(this.byteProperties.containsKey(name))
			return this.byteProperties.get(name);
		return 0;
	}

	/**
	 * Gets char prop.
	 *
	 * @param name the name
	 * @return the char prop
	 */
	public char getCharProp(String name) {
		if(this.characterProperties.containsKey(name ))
			return this.characterProperties.get(name);
		return ' ';
	}

	/**
	 * Gets double prop.
	 *
	 * @param name the name
	 * @return the double prop
	 */
	public double getDoubleProp(String name) {
		if(this.doubleProperties.containsKey(name ))
			return this.doubleProperties.get(name);
		return 0;
	}

	/**
	 * Gets float prop.
	 *
	 * @param name the name
	 * @return the float prop
	 */
	public float getFloatProp(String name) {
		if(this.floatProperties.containsKey(name ))
			return this.floatProperties.get(name);
		return 0;
	}

	/**
	 * Gets int prop.
	 *
	 * @param name the name
	 * @return the int prop
	 */
	public int getIntProp(String name) {
		if(this.integerProperties.containsKey(name))
			return this.integerProperties.get(name);
		return 0;
	}

	/**
	 * Gets long prop.
	 *
	 * @param name the name
	 * @return the long prop
	 */
	public long getLongProp(String name) {
		if(this.longProperties.containsKey(name))
			return this.longProperties.get(name);
		return 0;
	}

	/**
	 * Gets short prop.
	 *
	 * @param name the name
	 * @return the short prop
	 */
	public short getShortProp(String name) {
		if(this.shortProperties.containsKey(name))
			return this.shortProperties.get(name);
		return 0;
	}

	/**
	 * Gets string prop.
	 *
	 * @param name the name
	 * @return the string prop
	 */
	public String getStringProp(String name) {
		String res = this.stringProperties.get(name);
		if(res == null)
			return "";
		return res;
	} 
}
