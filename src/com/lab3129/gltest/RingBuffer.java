package com.lab3129.gltest;

public class RingBuffer {
	public int size;
	private float data[];
	private int ptr;
	public float sum;
	public RingBuffer(int size) {
		super();
		this.size = size;
		this.data = new float[size];
		for(int i=0;i<this.size;i++)
		{
			this.data[i] = 0;
		}
		this.ptr = 0;
		this.sum = 0;
	}

	public float getSTDERR()
	{
		float avg = ((float)this.sum) / this.size;
		float ret = 0;
		for(int i = 0; i < this.size; i++)
		{
			ret += (this.data[i] - avg) * (this.data[i] - avg);
		}
		return ret;
	}
	public void put(float d)
	{
		this.sum += d - this.data[this.ptr];
		this.data[this.ptr] = d;
		this.ptr++;
		
		if(this.ptr >= this.size)
		{
			this.ptr = 0;
		}
		
	}
	

	
}
