/**
 * 
 */
package com.sonologic.spacestatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author gmc
 *
 */
public class SpaceStatusParcelable implements Parcelable {
    private boolean open;
    private String name;
    private String url;
    private String address;
    private String phone;
    private String logo;
    private String cam;
    private long lastchange;
    private JSONArray checkins;
    
    

	/* Create object from parcel
     * 
     */
	private SpaceStatusParcelable(Parcel in) {
		this.open    = (Boolean) in.readValue(null);
		this.name    = in.readString();
		this.url     = in.readString();
		this.address = in.readString();
		this.phone   = in.readString();
		this.cam     = in.readString();
		this.lastchange = in.readLong();
		
	}
	
	
	private String getStringFromJSON(JSONObject json,String key) {
		String rv;
		try {
			rv=json.getString(key);
		} catch(JSONException e) {
			rv="";
		}
		return rv;
	}
	
	/* Create object from json object
	 * @param JSONObject the spacestatus json parsed into a json object
	 */
	public SpaceStatusParcelable(JSONObject json) throws JSONException {
		this.open=json.getBoolean("open");		
		this.name=this.getStringFromJSON(json,"name");
		this.url=this.getStringFromJSON(json,"url");
		this.address=this.getStringFromJSON(json,"address");
		this.phone=this.getStringFromJSON(json,"phone");
		this.cam=this.getStringFromJSON(json,"cam");
		this.lastchange=json.getLong("lastchange");
		/*
		try {
			this.checkins = json.getJSONArray("checkins");
		} catch(JSONException e) {
			this.checkins = null;
		}
		*/
	}

	/* ?
	 * @see android.os.Parcelable#describeContents()
	 */
	public int describeContents() {
		return 0;
	}

	/* Write the class contents to the parcel
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	public void writeToParcel(Parcel out, int flags) {
		out.writeValue(this.open);
		out.writeString(this.name);
		out.writeString(this.url);
		out.writeString(this.address);
		out.writeString(this.phone);
		out.writeString(this.cam);
		out.writeLong(this.lastchange);
		/*
		if(this.checkins!=null) {
		    out.writeValue(this.checkins.length());
		    for(int i=0;i<this.checkins.length();i++) {
		    	try {
		    		out.writeValue(this.checkins.getJSONObject(i).getString("name"));
		    	} catch(JSONException e) {
		    		out.writeValue("unkown");
		    	}
		    	try {    		
		    		out.writeValue(this.checkins.getJSONObject(i).getString("type"));
		    	} catch(Exception e) {
		    		out.writeValue("unknown");
		    	}
		    	try {
		    		out.writeValue(this.checkins.getJSONObject(i).getInt("t"));
		    	} catch(Exception e) {
		    		out.writeValue(0);
		    	}
		    }
		} else {
			out.writeValue(0);
		}
		*/
	}
	
    public static final Parcelable.Creator<SpaceStatusParcelable> CREATOR
    	= new Parcelable.Creator<SpaceStatusParcelable>() {
      public SpaceStatusParcelable createFromParcel(Parcel in) {	
        return new SpaceStatusParcelable(in);
      }

      public SpaceStatusParcelable[] newArray(int size) {
        return new SpaceStatusParcelable[size];
      }
    };

	
	public boolean getOpen() {
		return this.open;
	}

	/**
	 * @param open the open to set
	 */
	public void setOpen(boolean open) {
		this.open = open;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
    /**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the logo
	 */
	public String getLogo() {
		return logo;
	}

	/**
	 * @param logo the logo to set
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}

	/**
	 * @return the cam
	 */
	public String getCam() {
		return cam;
	}

	/**
	 * @param cam the cam to set
	 */
	public void setCam(String cam) {
		this.cam = cam;
	}

	/**
	 * @return the lastchange
	 */
	public long getLastchange() {
		return lastchange;
	}

	/**
	 * @param lastchange the lastchange to set
	 */
	public void setLastchange(long lastchange) {
		this.lastchange = lastchange;
	}

}
