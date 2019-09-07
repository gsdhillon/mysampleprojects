package cpnds.MyMathLib;


/**
 * @type     : Java Class
 * @name     : Complex
 * @file     : Complex.java
 * @created  : Feb 3, 2011 12:08:47 PM
 * @version  : 1.2
 */
public class Complex {
    private final float re;   // the real part
    private final float im;   // the imaginary part

    /**
     * create a new object with the given real and imaginary parts
     */
    public Complex(float real, float imag) {
        re = real;
        im = imag;
    }
    /**
     * return abs
     */ 
    public float abs()   {
        return (float)Math.hypot(re, im);
    }
    
    /**
     * return phase
     */
    public float phase() {
        return (float)Math.atan2(im, re);
    }

    /**
     * return a new Complex object whose value is the conjugate of this
     */ 
    public Complex conjugate() {  return new Complex(re, -im); }

    /**
     *  return a new Complex object whose value is (this + b)
     */
    public Complex plus(Complex b) {
        Complex a = this;             // invoking object
        float real = a.re + b.re;
        float imag = a.im + b.im;
        return new Complex(real, imag);
    }

    /**
     *  return a new Complex object whose value is (this - b)
     */
    public Complex minus(Complex b) {
        Complex a = this;
        float real = a.re - b.re;
        float imag = a.im - b.im;
        return new Complex(real, imag);
    }

    /**
     * return a new Complex object whose value is (this * b)
     */
    public Complex times(Complex b) {
        Complex a = this;
        float real = a.re * b.re - a.im * b.im;
        float imag = a.re * b.im + a.im * b.re;
        return new Complex(real, imag);
    }

    /**
     * scalar multiplication
     * return a new object whose value is (this * alpha)
     */
    public Complex times(float alpha) {
        return new Complex(alpha * re, alpha * im);
    }

    /**
     * return a new Complex object whose value is the reciprocal of this
     */
    public Complex reciprocal() {
        float scale = re*re + im*im;
        return new Complex(re / scale, -im / scale);
    }

    /**
     * return the real  part
     */
    public float real() {
        return re;
    }

    /**
     *
     * return imaginary part
     */
    public float imag() {
        return im;
    }

    /**
     * return a / b
     */
    public Complex divides(Complex b) {
        Complex a = this;
        return a.times(b.reciprocal());
    }
    /**
     * a static version of plus
     */
    public static Complex plus(Complex a, Complex b) {
        float real = a.re + b.re;
        float imag = a.im + b.im;
        Complex sum = new Complex(real, imag);
        return sum;
    }
}