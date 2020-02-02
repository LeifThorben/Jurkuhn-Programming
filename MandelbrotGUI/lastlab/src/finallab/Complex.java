package finallab;

/**
 * Complex numbers with minimal set of attributes and methods. 
 * Suitable for Mandelbrot computations only.
 * 
 * @author Henning Dierks
 * @version 1.0
 */
public class Complex {

  // attributes
  private double re;
  private double im;

  /**
   * Standard constructor. 
   * @param re real part of the complex number
   * @param im imaginary part of the complex number
   */
  public Complex(double re, double im) {
    this.re = re;
    this.im = im;
  }

  /**
   * Computes the distance from (0,0).
   * @return distance 
   */
  public double abs() {
    return Math.sqrt(re * re + im * im);
  }

  /**
   * Basic operation for Mandelbrot computation. 
   * It computes z_{n+1} = z_n*z_n+c
   * @param c complex number
   * @return this^2+c
   */
  public Complex squarePlusC(Complex c) {

    // compute (this)^2 + c
    double r = (re * re - im * im) + c.re;
    double i = (2 * re * im) + c.im;

    return new Complex(r,i);
  }

}
