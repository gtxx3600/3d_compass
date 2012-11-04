package com.lab3129.gltest;

public class RingBuffer {
	public int size;
	private float data[];
	private int ptr;
	private int put_count;
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
		this.put_count = 0;
		this.sum = 0;
	}
//	public void put(float d)
//	{
//		this.put_count++;
//		if(this.put_count > this.size)
//		{
//			this.sum -= data[ptr];
//		}
//		data[ptr++] = d;
//		this.sum += d;
//		if(ptr >= this.size)
//		{
//			ptr = 0;
//			this.sum = 0;
//			
//			for(int i=0;i<this.size;i++)
//			{
//				this.sum += this.data[i];
//			}
//		}
//	}
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
