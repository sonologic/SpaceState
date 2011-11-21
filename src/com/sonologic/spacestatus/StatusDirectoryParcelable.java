/**
 * 
 */
package com.sonologic.spacestatus;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author gmc
 *
 */
public class StatusDirectoryParcelable implements Parcelable {
    public ArrayList<StatusDirectoryEntry> dir = new ArrayList<StatusDirectoryEntry>();


	/* Create object from parcel
     * 
     */
	private StatusDirectoryParcelable(Parcel in) {
		int n = in.readInt();
		for(int i=0;i<n;i++) {
			String name = in.readString();
			this.dir.add(new StatusDirectoryEntry(name,in.readString()));
		}
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
	public StatusDirectoryParcelable(JSONObject json) throws JSONException {
		@SuppressWarnings("rawtypes")
		Iterator i = json.keys();
		while(i.hasNext()) {
			String name = (String) i.next();
			String url = this.getStringFromJSON(json,name);
			this.dir.add(new StatusDirectoryEntry(name,url));
		}
	}

	public int size() {
		return this.dir.size();
	}
	
	public StatusDirectoryEntry get(int i) {
		return this.dir.get(i);
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
		out.writeInt(this.dir.size());
		for(int i=0;i<this.dir.size();i++) {
			out.writeString(this.dir.get(i).getName());
			out.writeString(this.dir.get(i).getUrl().toString());
		}
/*		out.writeValue(this.open);
		out.writeString(this.name);
		out.writeString(this.url);
		out.writeString(this.address);
		out.writeString(this.phone);
		out.writeString(this.cam);
		out.writeLong(this.lastchange);
	
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
	
    public static final Parcelable.Creator<StatusDirectoryParcelable> CREATOR
    	= new Parcelable.Creator<StatusDirectoryParcelable>() {
      public StatusDirectoryParcelable createFromParcel(Parcel in) {	
        return new StatusDirectoryParcelable(in);
      }

      public StatusDirectoryParcelable[] newArray(int size) {
        return new StatusDirectoryParcelable[size];
      }
    };

}
