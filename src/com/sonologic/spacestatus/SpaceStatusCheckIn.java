/**
 * 
 */
package com.sonologic.spacestatus;

import android.text.format.Time;

/**
 * @author gmc
 *
 */
public class SpaceStatusCheckIn {
	private String name;
	private String type;
	private Time t;
	
	
	/**
	 * @param name name of the checkin
	 * @param type 'in' or 'out'
	 * @param t time in seconds since epoch
	 */
	public SpaceStatusCheckIn(String name, String type, long t) {
		this.name = name;
		this.type = type;
		this.t = new Time();
		this.t.set(t*1000);
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the t
	 */
	public Time getT() {
		return t;
	}
	/**
	 * @param t the t to set
	 */
	public void setT(Time t) {
		this.t = t;
	}
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	
}
