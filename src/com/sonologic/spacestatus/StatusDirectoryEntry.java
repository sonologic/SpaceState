/**
 * Contains all info related to one hackerspaces.
 * 
 */
package com.sonologic.spacestatus;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author gmc
 *
 */
public class StatusDirectoryEntry {
	private String name;
	private URL url;
	
	public StatusDirectoryEntry(String name,URL url) {
		this.name=name;
		this.url=url;
	}

	public StatusDirectoryEntry(String name,String url) {
		this.name=name;
		try {
			this.url=new URL(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	 * @return the url
	 */
	public URL getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(URL url) {
		this.url = url;
	}
	
	
}
