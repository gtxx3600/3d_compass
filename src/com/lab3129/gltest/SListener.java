package com.lab3129.gltest;

import java.util.Timer;
import java.util.TimerTask;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.Matrix;
import android.util.Log;

public class SListener implements SensorEventListener {
	public float orientation_data[];
	public float mag_data[];
	public float acc_data[];
	public float rotationMatrix[];
	private SensorManager sensorManager;
	private Sensor mAccelerometer;
	private Sensor mMag;
	public float speed[];
	public float position[];
	private long accEvtLastTimeStamp = -1;
	private Sensor mLinearAcc;
	private float stderr = 99;
	private RingBuffer accRingBuff[];
	private Timer err_schedule;
	class myTask extends TimerTask{
		
		@Override
		public void run() {
			getCurrERR();
		}
		
	}
	public String pos2String()
	{
		return "[" + position[0] +","+ position[1] +","+ position[2] + "]";
	}
	public SListener(Context context)
	{
		orientation_data = new float[3];
		acc_data = new float[3];
		rotationMatrix = new float[16];
		speed = new float[3];
		position = new float[3];
		for(int i=0;i<speed.length;i++)
		{
			speed[i] = 0.0f;
			position[i] = 0.0f;
		}
		accRingBuff = new RingBuffer[3];
		for(int i=0;i<3;i++)
		{
			accRingBuff[i] = new RingBuffer(50);
		}
		position[2] = 10;
		mag_data = new float[3];
		sensorManager = (SensorManager)context.getSystemService("sensor");
		mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mMag = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		mLinearAcc = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		
		sensorManager.registerListener(this, mLinearAcc, SensorManager.SENSOR_DELAY_FASTEST);
		sensorManager.registerListener(this, mMag, SensorManager.SENSOR_DELAY_FASTEST);
		sensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
		
		err_schedule = new Timer();
		err_schedule.schedule(new myTask(), 10, 10);
	}
	@Override
	public void finalize()
	{
		sensorManager.unregisterListener(this);
		err_schedule.cancel();
		try {
			super.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			Log.e("GLTest", "SListener: "+e.toString());
		}
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}
	public float[] computeRotationMatrix()
	{
		SensorManager.getRotationMatrix(this.rotationMatrix, null, this.acc_data, this.mag_data);
		return this.rotationMatrix;
	}
	public float getCurrERR()
	{
		this.stderr = this.accRingBuff[0].getSTDERR() + this.accRingBuff[1].getSTDERR() + this.accRingBuff[2].getSTDERR();
		return stderr;
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		float p = 0.05f;
		long evtTimeStamp = event.timestamp; //nano second
		double diff = 0;
		float buff[] = new float[4];
		float data[] = new float[4];
	
		switch(event.sensor.getType())
		{
		case Sensor.TYPE_ACCELEROMETER:
		{
			this.accRingBuff[0].put(event.values[0]);
			this.accRingBuff[1].put(event.values[1]);
			this.accRingBuff[2].put(event.values[2]);
			this.acc_data[0] = this.acc_data[0]*(1-p) + p*event.values[0];
			this.acc_data[1] = this.acc_data[1]*(1-p) + p*event.values[1];
			this.acc_data[2] = this.acc_data[2]*(1-p) + p*event.values[2];
			break;
		}
		case Sensor.TYPE_LINEAR_ACCELERATION:
		{
			if( this.accEvtLastTimeStamp > 0)
			{
				diff = (evtTimeStamp - this.accEvtLastTimeStamp) * 0.000000001;
				data[0] = event.values[0];
				data[1] = event.values[1];
				data[2] = event.values[2];
				data[3] = 1;
				Matrix.multiplyMV(buff, 0, this.rotationMatrix, 0, data, 0);
				this.speed[0] += buff[0]*diff;
				this.speed[1] += buff[1]*diff;
				this.speed[2] += buff[2]*diff;

			}
			if(this.stderr < 2)
			{
				this.speed[0] = 0;
				this.speed[1] = 0;
				this.speed[2] = 0;
			}
			this.accEvtLastTimeStamp = evtTimeStamp;
			break;
		}
		case Sensor.TYPE_MAGNETIC_FIELD:
			this.mag_data[0] = this.mag_data[0]*(1-p) + p*event.values[0];
			this.mag_data[1] = this.mag_data[1]*(1-p) + p*event.values[1];
			this.mag_data[2] = this.mag_data[2]*(1-p) + p*event.values[2];
		default:
			break;
		}
	}

}
