package com.eatApp.AndroidDebuggingTool;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AndroidDebuggingToolActivity extends Activity {
	/** Called when the activity is first created. */

	private Button btnStart;
	private Button btnStop;
	private EditText etPort;
	private TextView tvCurrentIP;

	// su//获取root权限
	// setprop service.adb.tcp.port 5555//设置监听的端口，端口可以自定义，如5554，5555是默认的
	// stop adbd//关闭adbd
	// start adbd//重新启动adbd
	// adb connect a.b.c.d

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		btnStart = (Button) findViewById(R.id.btn_start);
		btnStop = (Button) findViewById(R.id.btn_stop);
		etPort = (EditText) findViewById(R.id.listen_port);
		tvCurrentIP = (TextView) findViewById(R.id.tv_currenip);
		tvCurrentIP.setText(GetIpAddress());

		btnStop.setClickable(false);

		btnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				execCmd(etPort.getText().toString());
				btnStart.setClickable(false);
				btnStop.setClickable(true);

			}
		});

		btnStop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				execCmd("-1");
				btnStart.setClickable(true);
				btnStop.setClickable(false);
			}
		});

	}

	/***
	 * 
	 * @param port
	 * @return
	 * 
	 *         好东西 http://su1216.iteye.com/blog/1668734
	 */

	private Vector execCmd(String port) {
		Vector localVector = new Vector();
		try {
			Process localProcess = Runtime.getRuntime().exec("su ");
			OutputStream localOutputStream = localProcess.getOutputStream();
			DataOutputStream localDataOutputStream = new DataOutputStream(
					localOutputStream);
			InputStream localInputStream = localProcess.getInputStream();
			DataInputStream localDataInputStream = new DataInputStream(
					localInputStream);
			// localVector.add(localDataInputStream.readLine());
			localDataOutputStream.writeBytes(new String(
					"setprop service.adb.tcp.port " + port + "\n"));

			localDataOutputStream.writeBytes(new String("stop adbd \n"));

			localDataOutputStream.writeBytes(new String("start adbd \n"));

			localDataOutputStream.writeBytes(new String("exit\n"));
			localDataOutputStream.flush();
			// localVector.add(localDataInputStream.readLine());

			// localDataOutputStream.flush();
			// localVector.add(localDataInputStream.readLine());

			// localDataOutputStream.flush();
			// localVector.add(localDataInputStream.readLine());

			// localDataOutputStream.flush();
			// localVector.add(localDataInputStream.readLine());
			localProcess.waitFor();
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return localVector;
	}

	public String GetIpAddress() {
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifiManager.getConnectionInfo();
		return new String(intToIp(info.getIpAddress()));
	}

	private String intToIp(int i) {

		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + (i >> 24 & 0xFF);
	}

}