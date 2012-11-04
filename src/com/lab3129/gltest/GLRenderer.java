package com.lab3129.gltest;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.opengl.Matrix;

public class GLRenderer implements Renderer {
	private Square s;
	private GL10 gles;
	private float theta;
	private long lastTime;
	private long startTime;
	private long createTime;
	private int fps_count;
	private SListener sl;
	private float centerVec[] = {0.0f, 0.0f, -1.0f, 1.0f};
	private float upperVec[] = {0.0f, 1.0f, 0.0f, 1.0f};
	public GLRenderer(SListener slistener) {
		// TODO Auto-generated constructor stub
		s = new Square();
		theta = 0;
		fps_count = 0;
		sl = slistener;
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		//gl.glMatrixMode(GL10.GL_MODELVIEW);// OpenGL docs.
		// Reset the modelview matrix
		gl.glLoadIdentity();// OpenGL docs.
		//GLU.gluLookAt(gl, 0, 0, 6, 0, 0, -1, (float)Math.cos(theta), (float)Math.sin(theta), 0 );
		
		long time = System.currentTimeMillis();
//		theta = (float) ((time - this.createTime) / 1000.0 * 90);
//		gl.glTranslatef(0, 0, -14);
//		
//		gl.glRotatef(sl.orientation_data[0], 0, 0, 1);
//		gl.glRotatef(sl.orientation_data[2], 0, 1, 0);
//		gl.glRotatef(sl.orientation_data[1], 1, 0, 0);
		
		float [] rotationMatrix = sl.computeRotationMatrix();
		float r[] = new float[16];
		Matrix.invertM(r, 0, rotationMatrix, 0);
		float center[] = new float[4];
		float upper[] = new float[4];
		Matrix.multiplyMV(center, 0, r, 0, centerVec, 0);
		Matrix.multiplyMV(upper, 0, r, 0, upperVec, 0);
		GLU.gluLookAt(gl, -center[0] * 10, -center[1] * 10, -center[2] * 10, 
				center[0], center[1], center[2], 
				upper[0], upper[1], upper[2] );
		
		//gl.glTranslatef(4, 0, 0);
		s.draw(gl);
		lastTime = time;
		fps_count ++;
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		gl.glViewport(0, 0, width, height);// OpenGL docs.
		// Select the projection matrix
		gl.glMatrixMode(GL10.GL_PROJECTION);// OpenGL docs.
		// Reset the projection matrix
		gl.glLoadIdentity();// OpenGL docs.
		// Calculate the aspect ratio of the window
		GLU.gluPerspective(gl, 45.0f,
		(float) width / (float) height,
		0.1f, 100.0f);
		// Select the modelview matrix
		gl.glMatrixMode(GL10.GL_MODELVIEW);// OpenGL docs.
		// Reset the modelview matrix
		gl.glLoadIdentity();// OpenGL docs.
		GLU.gluLookAt(gl, 0, 0, 6, 0, 0, -1, 0, 1, 0 );
		this.gles = gl;
		this.startTime = System.currentTimeMillis();
		this.fps_count = 0;
		
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);  // OpenGL docs.
		// Enable Smooth Shading, default not really needed.
		gl.glShadeModel(GL10.GL_SMOOTH);// OpenGL docs.
		// Depth buffer setup.
		gl.glClearDepthf(1.0f);// OpenGL docs.
		// Enables depth testing.
		gl.glEnable(GL10.GL_DEPTH_TEST);// OpenGL docs.
		// The type of depth testing to do.
		gl.glDepthFunc(GL10.GL_LEQUAL);// OpenGL docs.
		// Really nice perspective calculations.
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, // OpenGL docs.
		GL10.GL_NICEST);
		
		this.createTime = System.currentTimeMillis();

	}
	
	public float getFPS()
	{
		long now = System.currentTimeMillis();
		float diff = (float)((now - this.startTime)/1000.0f);
		float ret = this.fps_count / diff;
		this.startTime = now;
		this.fps_count = 0;
		return ret;
		
	}

}
