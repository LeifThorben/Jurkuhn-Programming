package finallab;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Mandelbrot extends JPanel implements Runnable {

	private static final int N = 10000;
	protected Double xmin = -1.5;
	protected Double xmax = -0.5;
	protected Double ymin = -0.25;
	protected Double ymax = 0.25;

	private int T = 4;

	protected BufferedImage image;

	private static final int wd = 300;
	private static final int ht = 200;

	protected boolean recompute;

	public BufferedImage getImage() {
		return image;
	}

	/**
	 * Sets the image. Moreover it avoids repainting (incl. recomputing) and resizes
	 * the size of the panel.
	 * 
	 * @param image the image to be shown
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
		recompute = false;
		setMinimumSize(new Dimension(image.getWidth(), image.getHeight()));
		setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		repaint();
	}

	/**
	 * Constructor setting the window size to default values.
	 */
	public Mandelbrot() {
		image = new BufferedImage(wd, ht, BufferedImage.TYPE_INT_RGB);
		setBackground(Color.white); // meaning should be obvious
		setMinimumSize(new Dimension(wd, ht));
		setPreferredSize(new Dimension(wd, ht));
		recompute = true;
	}

	// for each panel this must be overridden!
	// parameter g is a graphic context and
	// we are allowed to draw on it.
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g); // never forget!
		Dimension dim = getSize();

		// only recompute if necessary!
		if (recompute || dim.getWidth() > image.getWidth() || dim.getHeight() > image.getHeight()) {

			image = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_RGB);
			setPreferredSize(dim);
			setMinimumSize(dim);

			Thread[] threads = new Thread[T];

			for (int i = 0; i < T; i++) {

				int startx = i * dim.width / T;
				int endx = (i + 1) * dim.width / T;

				threads[i] = new Thread() {
					public void run() {
						int[][] nValues = new int[dim.width][dim.height];

						for (int x = startx; x < endx; x++) {
							for (int y = 0; y < dim.height; y++) {

								// compute the complex number
								double re = xmin + (xmax - xmin) * x / dim.width;
								double im = ymax + (ymin - ymax) * y / dim.height;

								// now do the computation
								Complex c = new Complex(re, im);
								Complex z = new Complex(0, 0);
								int n;
								for (n = 0; n < N && z.abs() < 2000; n++) {
									z = z.squarePlusC(c); // z = z*z+c
								}
								nValues[x][y] = n;
							}
						}

						//

						synchronized (Mandelbrot.class) {
							for (int x = startx; x < endx; x++) {
								for (int y = 0; y < dim.height; y++) {
									// now draw the point, choose the color
									// first
									int red = 255 * nValues[x][y] / N;
									int blue = nValues[x][y] % 256;
									int green = (red + blue) / 2;

									image.setRGB(x, y, new Color(red, green, blue).getRGB());
								}
							}
						}
					}
				};
				threads[i].start();
			}

			for (int i = 0; i < T; i++) {
				try {
					threads[i].join();
				} catch (InterruptedException e) {
					;
				}

			}

			/*
			 * 
			 * for (int x = 0; x < dim.width; x++) { for (int y = 0; y < dim.height; y++) {
			 * 
			 * // compute the complex number double re = xmin + (xmax - xmin) * x /
			 * dim.width; double im = ymax + (ymin - ymax) * y / dim.height;
			 * 
			 * // now do the computation Complex c = new Complex(re,im); Complex z = new
			 * Complex(0,0); int n; for (n = 0; n < N && z.abs() < 2000; n++) { z =
			 * z.squarePlusC(c); // z = z*z+c }
			 * 
			 * // n is the result of this computation // with 0 <= n <= N
			 * 
			 * // now draw the point, choose the color first int red = 255 * n / N; int blue
			 * = n % 256; int green = (red + blue) / 2;
			 * 
			 * image.setRGB(x, y, new Color(red,green,blue).getRGB()); } }
			 */
			recompute = false; // no need to compute unless resizing to
			// a larger scale
		}
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
	}

	/**
	 * Sets all four limits of the picture. Meaning of the parameters should be
	 * obvious by the names given.
	 * 
	 * @param xmin lower x bound
	 * @param xmax upper x bound
	 * @param ymin lower y bound
	 * @param ymax upper y bound
	 */
	public void setLimits(double xmin, double xmax, double ymin, double ymax) {
		// if limits are changed, there is a need to recompute
		recompute = (this.xmin != xmin) || (this.xmax != xmax) || (this.ymin != ymin) || (this.ymax != ymax);

		// now copy the values into the fields.
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
	}

	/**
	 * A main method just for testing purposes.
	 * 
	 * @param args not used here
	 */
	/*
	 * public static void main(String[] args) { JFrame window = new JFrame();
	 * Mandelbrot mandelbrot = new Mandelbrot();
	 * 
	 * window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); window.add(new
	 * JScrollPane(mandelbrot)); // add the panel to the frame window.pack(); //
	 * organise the container window.setVisible(true); // show time! }
	 */

	public void setT(int numofthreads) {
		this.T = T;
	}

	public final int getT() {
		return T;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}
}
