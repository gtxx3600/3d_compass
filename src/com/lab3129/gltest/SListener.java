package com.lab3129.gltest;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class SListener implements SensorEventListener {
	public float orientation_data[];
	public float mag_data[];
	public float acc_data[];
	public float rotationMatrix[];
	private SensorManager sensorManager;
	private Sensor mOrientation;
	private Sensor mAccelerometer;
	private Sensor mMag;
	public SListener(Context context)
	{
		orientation_data = new float[3];
		acc_data = new float[3];
		rotationMatrix = new float[16];
		mag_data = new float[3];
		sensorManager = (SensorManager)context.getSystemService("sensor");
		mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mMag = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		mOrientation = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		sensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_FASTEST);
		sensorManager.registerListener(this, mMag, SensorManager.SENSOR_DELAY_FASTEST);
		sensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
	}
	@Override
	public void finalize()
	{
		sensorManager.unregisterListener(this);
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
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		float p = 0.05f;
		switch(event.sensor.getType())
		{
		case Sensor.TYPE_ORIENTATION:
			this.orientation_data[0] = event.values[0];
			this.orientation_data[1] = event.values[1];
			this.orientation_data[2] = event.values[2];
			break;
		case Sensor.TYPE_ACCELEROMETER:
			this.acc_data[0] = this.acc_data[0]*(1-p) + p*event.values[0];
			this.acc_data[1] = this.acc_data[1]*(1-p) + p*event.values[1];
			this.acc_data[2] = this.acc_data[2]*(1-p) + p*event.values[2];
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			this.mag_data[0] = this.mag_data[0]*(1-p) + p*event.values[0];
			this.mag_data[1] = this.mag_data[1]*(1-p) + p*event.values[1];
			this.mag_data[2] = this.mag_data[2]*(1-p) + p*event.values[2];
		default:
			break;
		}
	}

}
