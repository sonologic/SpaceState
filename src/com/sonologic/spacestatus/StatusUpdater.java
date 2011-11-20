/**
 * The StatusUpdater implements the actual http request and creates
 * the JSONObject.
 * 
 * @author Koen Martens <kmartens@sonologic.net>
 */
package com.sonologic.spacestatus;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author gmc
 *
 */
public class StatusUpdater extends BroadcastReceiver {
	
	private Context context;
	/**
	 * 
	 */
	public StatusUpdater(Context context) {
		this.context=context;
		trustAllHosts();
	}
	
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals("com.sonologic.spacestatus.GETUPDATE")) {
		  this.update();
		}
		if(intent.getAction().equals("com.sonologic.spacestatus.UPDATELIST")) {
			this.updateList();
		}
		if(intent.getAction().equals("com.sonologic.spacestatus.SETFREQ")) {
			  ((GetStatusService)this.context).setUpdateFrequency(
					  intent.getIntExtra("f", 5) * 60 );
		}
	}

	private void trustAllHosts() {    	
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[] {};
                }

                public void checkClientTrusted(X509Certificate[] chain,
                                String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain,
                                String authType) throws CertificateException {
                }
        } };

        // Install the all-trusting trust manager
        try {
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection
                                .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        	//tv.setText(e.toString()+"\n");
        }
        
        HttpsURLConnection.setDefaultHostnameVerifier(new NaiveHostnameVerifier());
        
    }
	
	public void update() {
		try {
			URL url = new URL("https://revspace.nl/status/status.php");
			HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
			//conn.setHostnameVerifier(new NaiveHostnameVerifier());
			try {
				InputStream in = new BufferedInputStream(conn.getInputStream());
				BufferedReader r = new BufferedReader(new InputStreamReader(in));
				String page="";
				String s=r.readLine();
				while(s!=null) {
					page+=s;
					s=r.readLine();
				} 

				JSONObject json = (JSONObject) new JSONTokener(page).nextValue();
				SpaceStatusParcelable status = new SpaceStatusParcelable(json);

				Intent i = new Intent("com.sonologic.spacestatus.UPDATE");
				i.putExtra("status",status);
				i.putExtra("html", page);
				i.putExtra("contentType",conn.getContentType());
				i.putExtra("contentEncoding",conn.getContentEncoding());
				context.sendBroadcast(i);
			} finally {
				conn.disconnect();
			}
        } catch(Exception E) {
        	//tv.setText("Exception:"+E.toString());
        }
	}
	
	public void updateList() {
		Log.i("spacestatus","update list");
		try {
			URL url = new URL("http://chasmcity.sonologic.nl/spacestatusdirectory.php");
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			//conn.setHostnameVerifier(new NaiveHostnameVerifier());
			try {
				InputStream in = new BufferedInputStream(conn.getInputStream());
				BufferedReader r = new BufferedReader(new InputStreamReader(in));
				String page="";
				String s=r.readLine();
				while(s!=null) {
					page+=s;
					s=r.readLine();
				} 

				JSONObject json = (JSONObject) new JSONTokener(page).nextValue();
				StatusDirectoryParcelable dir = new StatusDirectoryParcelable(json);

				Intent i = new Intent("com.sonologic.spacestatus.LISTUPDATE");
				i.putExtra("directory",dir);
				context.sendBroadcast(i);
			} catch(Exception E) {
				throw E;
				//Log.e("updateList",E.toString());
			} finally {
				conn.disconnect();
			}
        } catch(Exception E) {
        	//tv.setText("Exception:"+E.toString());
        }
	}
}
