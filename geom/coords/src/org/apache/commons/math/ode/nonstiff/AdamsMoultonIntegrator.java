/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.math.ode.nonstiff;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.fraction.BigFraction;
import org.apache.commons.math.linear.DefaultRealMatrixChangingVisitor;
import org.apache.commons.math.linear.FieldLUDecompositionImpl;
import org.apache.commons.math.linear.FieldMatrix;
import org.apache.commons.math.linear.Array2DRowFieldMatrix;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.MatrixVisitorException;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.RealMatrixPreservingVisitor;
import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.IntegratorException;
import org.apache.commons.math.ode.MultistepIntegrator;
import org.apache.commons.math.ode.events.CombinedEventsManager;
import org.apache.commons.math.ode.sampling.NordsieckStepInterpolator;
import org.apache.commons.math.ode.sampling.StepHandler;


/**
 * This class implements implicit Adams-Moulton integrators for Ordinary
 * Differential Equations.
 *
 * <p>Adams-Moulton methods (in fact due to Adams alone) are implicit
 * multistep ODE solvers with fixed stepsize. The value of state vector
 * at step n+1 is a simple combination of the value at step n and of the
 * derivatives at steps n+1, n, n-1 ... Since y'<sub>n+1</sub> is needed to
 * compute y<sub>n+1</sub>, another method must be used to compute a first
 * estimate of y<sub>n+1</sub>, then compute y'<sub>n+1</sub>, then compute
 * a final estimate of y<sub>n+1</sub> using the following formulas. Depending
 * on the number k of previous steps one wants to use for computing the next
 * value, different formulas are available for the final estimate:</p>
 * <ul>
 *   <li>k = 1: y<sub>n+1</sub> = y<sub>n</sub> + h y'<sub>n+1</sub></li>
 *   <li>k = 2: y<sub>n+1</sub> = y<sub>n</sub> + h (y'<sub>n+1</sub>+y'<sub>n</sub>)/2</li>
 *   <li>k = 3: y<sub>n+1</sub> = y<sub>n</sub> + h (5y'<sub>n+1</sub>+8y'<sub>n</sub>-y'<sub>n-1</sub>)/12</li>
 *   <li>k = 4: y<sub>n+1</sub> = y<sub>n</sub> + h (9y'<sub>n+1</sub>+19y'<sub>n</sub>-5y'<sub>n-1</sub>+y'<sub>n-2</sub>)/24</li>
 *   <li>...</li>
 * </ul>
 *
 * <p>A k-steps Adams-Moulton method is of order k+1. There is no theoretical limit to the
 * value of k, but due to an implementation limitation k must be greater than 1.</p>
 *
 * <h3>Implementation details</h3>
 *
 * <p>We define scaled derivatives s<sub>i</sub>(n) at step n as:
 * <pre>
 * s<sub>1</sub>(n) = h y'<sub>n</sub> for first derivative
 * s<sub>2</sub>(n) = h<sup>2</sup>/2 y''<sub>n</sub> for second derivative
 * s<sub>3</sub>(n) = h<sup>3</sup>/6 y'''<sub>n</sub> for third derivative
 * ...
 * s<sub>k</sub>(n) = h<sup>k</sup>/k! y(k)<sub>n</sub> for k<sup>th</sup> derivative
 * </pre></p>
 *
 * <p>The definitions above use the classical representation with several previous first
 * derivatives. Lets define
 * <pre>
 *   q<sub>n</sub> = [ s<sub>1</sub>(n-1) s<sub>1</sub>(n-2) ... s<sub>1</sub>(n-(k-1)) ]<sup>T</sup>
 * </pre>
 * (we omit the k index in the notation for clarity). With these definitions,
 * Adams-Moulton methods can be written:
 * <ul>
 *   <li>k = 1: y<sub>n+1</sub> = y<sub>n</sub> + s<sub>1</sub>(n+1)</li>
 *   <li>k = 2: y<sub>n+1</sub> = y<sub>n</sub> + 1/2 s<sub>1</sub>(n+1) + [ 1/2 ] q<sub>n+1</sub></li>
 *   <li>k = 3: y<sub>n+1</sub> = y<sub>n</sub> + 5/12 s<sub>1</sub>(n+1) + [ 8/12 -1/12 ] q<sub>n+1</sub></li>
 *   <li>k = 4: y<sub>n+1</sub> = y<sub>n</sub> + 9/24 s<sub>1</sub>(n+1) + [ 19/24 -5/24 1/24 ] q<sub>n+1</sub></li>
 *   <li>...</li>
 * </ul></p>
 *
 * <p>Instead of using the classical representation with first derivatives only (y<sub>n</sub>,
 * s<sub>1</sub>(n+1) and q<sub>n+1</sub>), our implementation uses the Nordsieck vector with
 * higher degrees scaled derivatives all taken at the same step (y<sub>n</sub>, s<sub>1</sub>(n)
 * and r<sub>n</sub>) where r<sub>n</sub> is defined as:
 * <pre>
 * r<sub>n</sub> = [ s<sub>2</sub>(n), s<sub>3</sub>(n) ... s<sub>k</sub>(n) ]<sup>T</sup>
 * </pre>
 * (here again we omit the k index in the notation for clarity)
 * </p>
 *
 * <p>Taylor series formulas show that for any index offset i, s<sub>1</sub>(n-i) can be
 * computed from s<sub>1</sub>(n), s<sub>2</sub>(n) ... s<sub>k</sub>(n), the formula being exact
 * for degree k polynomials.
 * <pre>
 * s<sub>1</sub>(n-i) = s<sub>1</sub>(n) + &sum;<sub>j</sub> j (-i)<sup>j-1</sup> s<sub>j</sub>(n)
 * </pre>
 * The previous formula can be used with several values for i to compute the transform between
 * classical representation and Nordsieck vector. The transform between r<sub>n</sub>
 * and q<sub>n</sub> resulting from the Taylor series formulas above is:
 * <pre>
 * q<sub>n</sub> = s<sub>1</sub>(n) u + P r<sub>n</sub>
 * </pre>
 * where u is the [ 1 1 ... 1 ]<sup>T</sup> vector and P is the (k-1)&times;(k-1) matrix built
 * with the j (-i)<sup>j-1</sup> terms:
 * <pre>
 *        [  -2   3   -4    5  ... ]
 *        [  -4  12  -32   80  ... ]
 *   P =  [  -6  27 -108  405  ... ]
 *        [  -8  48 -256 1280  ... ]
 *        [          ...           ]
 * </pre></p>
 * 
 * <p>Using the Nordsieck vector has several advantages:
 * <ul>
 *   <li>it greatly simplifies step interpolation as the interpolator mainly applies
 *   Taylor series formulas,</li>
 *   <li>it simplifies step changes that occur when discrete events that truncate
 *   the step are triggered,</li>
 *   <li>it allows to extend the methods in order to support adaptive stepsize (not implemented yet).</li>
 * </ul></p>
 * 
 * <p>The predicted Nordsieck vector at step n+1 is computed from the Nordsieck vector at step
 * n as follows:
 * <ul>
 *   <li>Y<sub>n+1</sub> = y<sub>n</sub> + s<sub>1</sub>(n) + u<sup>T</sup> r<sub>n</sub></li>
 *   <li>S<sub>1</sub>(n+1) = h f(t<sub>n+1</sub>, Y<sub>n+1</sub>)</li>
 *   <li>R<sub>n+1</sub> = (s<sub>1</sub>(n) - s<sub>1</sub>(n+1)) P<sup>-1</sup> u + P<sup>-1</sup> A P r<sub>n</sub></li>
 * </ul>
 * where A is a rows shifting matrix (the lower left part is an identity matrix):
 * <pre>
 *        [ 0 0   ...  0 0 | 0 ]
 *        [ ---------------+---]
 *        [ 1 0   ...  0 0 | 0 ]
 *    A = [ 0 1   ...  0 0 | 0 ]
 *        [       ...      | 0 ]
 *        [ 0 0   ...  1 0 | 0 ]
 *        [ 0 0   ...  0 1 | 0 ]
 * </pre>
 * From this predicted vector, the corrected vector is computed as follows:
 * <ul>
 *   <li>y<sub>n+1</sub> = y<sub>n</sub> + S<sub>1</sub>(n+1) + [ -1 +1 -1 +1 ... &plusmn;1 ] r<sub>n+1</sub></li>
 *   <li>s<sub>1</sub>(n+1) = h f(t<sub>n+1</sub>, y<sub>n+1</sub>)</li>
 *   <li>r<sub>n+1</sub> = R<sub>n+1</sub> + (s<sub>1</sub>(n+1) - S<sub>1</sub>(n+1)) P<sup>-1</sup> u</li>
 * </ul>
 * where the upper case Y<sub>n+1</sub>, S<sub>1</sub>(n+1) and R<sub>n+1</sub> represent the
 * predicted states whereas the lower case y<sub>n+1</sub>, s<sub>n+1</sub> and r<sub>n+1</sub>
 * represent the corrected states.</p>
 *
 * <p>The P<sup>-1</sup>u vector and the P<sup>-1</sup> A P matrix do not depend on the state,
 * they only depend on k and therefore are precomputed once for all.</p>
 *
 * @version $Revision: 785473 $ $Date: 2009-06-17 00:02:35 -0400 (Wed, 17 Jun 2009) $
 * @since 2.0
 */
public class AdamsMoultonIntegrator extends MultistepIntegrator implements Serializable {

    /**
     * Serial identification Number
     */
    private static final long serialVersionUID = 3624292432281962886L;

    /** Cache for already computed coefficients. */
    private static final Map<Integer, CachedCoefficients> cache =
        new HashMap<Integer, CachedCoefficients>();

    /** Coefficients of the method. */
    private final transient CachedCoefficients coefficients;

    /** Integration step. */
    private final double step;

    /**
     * Build an Adams-Moulton integrator with the given order and step size.
     * @param order order of the method (must be greater than 1: due to
     * an implementation limitation the order 1 method is not supported)
     * @param step integration step size
     * @exception IllegalArgumentException if order is 1 or less
     */
    public AdamsMoultonIntegrator(final int order, final double step)
        throws IllegalArgumentException {

        super("Adams-Moulton", order);
        if (order <= 1) {
            throw MathRuntimeException.createIllegalArgumentException(
                  "{0} is supported only for orders 2 or more",
                  getName());
        }

        // cache the coefficients for each order, to avoid recomputing them
        synchronized(cache) {
            CachedCoefficients coeff = cache.get(order);
            if (coeff == null) {
                coeff = new CachedCoefficients(order);
                cache.put(order, coeff);
            }
            coefficients = coeff;
        }

        this.step = Math.abs(step);

    }
      
    
    /** {@inheritDoc} */
    public double integrate(final FirstOrderDifferentialEquations equations,
                            final double t0, final double[] y0,
                            final double t, final double[] y)
        throws DerivativeException, IntegratorException {

        final int n = y0.length;
        sanityChecks(equations, t0, y0, t, y);
        setEquations(equations);
        resetEvaluations();
        final boolean forward = (t > t0);

        // initialize working arrays
        if (y != y0) {
            System.arraycopy(y0, 0, y, 0, n);
        }
        final double[] yTmp = new double[y0.length];

        // set up two interpolators sharing the integrator arrays
        final NordsieckStepInterpolator interpolator = new NordsieckStepInterpolator();
        interpolator.reinitialize(y, forward);
        final NordsieckStepInterpolator interpolatorTmp = new NordsieckStepInterpolator();
        interpolatorTmp.reinitialize(yTmp, forward);

        // set up integration control objects
        stepStart = t0;
        stepSize  = forward ? step : -step;
        for (StepHandler handler : stepHandlers) {
            handler.reset();
        }
        CombinedEventsManager manager = addEndTimeChecker(t0, t, eventsHandlersManager);

        // compute the first few steps using the configured starter integrator
        double stopTime = start(previousF.length, stepSize, manager, stepStart, y);
        if (Double.isNaN(previousT[0])) {
            return stopTime;
        }
        stepStart = previousT[0];
        double hNew = 0;

        // convert to Nordsieck representation
        double[]   scaled    = convertToNordsieckLow();
        RealMatrix nordsieck = convertToNordsieckHigh(scaled);
        interpolator.reinitialize(stepStart, stepSize, scaled, nordsieck);
        interpolator.storeTime(stepStart);

        boolean lastStep = false;
        while (!lastStep) {

            // shift all data
            interpolator.shift();

            hNew  = forward ? step : -step;
            for (boolean loop = true; loop;) {

                stepSize = hNew;

                // predict a first estimate of the state at step end (P in the PECE sequence)
                final double stepEnd = stepStart + stepSize;
                interpolator.setInterpolatedTime(stepEnd);
                System.arraycopy(interpolator.getInterpolatedState(), 0, yTmp, 0, y0.length);

                // evaluate a first estimate of the derivative (first E in the PECE sequence)
                final double[] f0 = previousF[0];
                previousT[0] = stepEnd;
                computeDerivatives(stepEnd, yTmp, f0);

                // update Nordsieck vector
                final RealMatrix nordsieckTmp = coefficients.msUpdate.multiply(nordsieck);
                final double[] predictedScaled = new double[y0.length];
                for (int j = 0; j < y0.length; ++j) {
                    predictedScaled[j] = stepSize * f0[j];
                }
                nordsieckTmp.walkInOptimizedOrder(new NordsieckUpdater(scaled, predictedScaled, coefficients.c1));

                // apply correction (C in the PECE sequence)
                nordsieckTmp.walkInOptimizedOrder(new Corrector(y, predictedScaled, yTmp));

                // evaluate a final estimate of the derivative (second E in the PECE sequence)
                computeDerivatives(stepEnd, yTmp, f0);

                // update Nordsieck vector
                final double[] correctedScaled = new double[y0.length];
                for (int j = 0; j < y0.length; ++j) {
                    correctedScaled[j] = stepSize * f0[j];
                }
                nordsieckTmp.walkInOptimizedOrder(new NordsieckUpdater(predictedScaled, correctedScaled, coefficients.c1));

                // discrete events handling
                interpolatorTmp.reinitialize(stepEnd, stepSize, correctedScaled, nordsieckTmp);
                interpolatorTmp.storeTime(stepStart);
                interpolatorTmp.shift();
                interpolatorTmp.storeTime(stepEnd);
                if (manager.evaluateStep(interpolatorTmp)) {
                    final double dt = manager.getEventTime() - stepStart;
                    if (Math.abs(dt) <= Math.ulp(stepStart)) {
                        // rejecting the step would lead to a too small next step, we accept it
                        loop = false;
                    } else {
                        // reject the step to match exactly the next switch time
                        hNew = dt;
                    }
                } else {
                    // accept the step
                    scaled    = correctedScaled;
                    nordsieck = nordsieckTmp;
                    interpolator.reinitialize(stepEnd, stepSize, scaled, nordsieck);
                    loop = false;
                }

            }

            // the step has been accepted (may have been truncated)
            final double nextStep = stepStart + stepSize;
            System.arraycopy(yTmp, 0, y, 0, n);
            interpolator.storeTime(nextStep);
            manager.stepAccepted(nextStep, y);
            lastStep = manager.stop();

            // provide the step data to the step handler
            interpolator.setInterpolatedTime(nextStep);
            for (StepHandler handler : stepHandlers) {
                handler.handleStep(interpolator, lastStep);
            }
            stepStart = nextStep;

            if (!lastStep && manager.reset(stepStart, y)) {

                // some events handler has triggered changes that
                // invalidate the derivatives, we need to restart from scratch
                stopTime = start(previousF.length, stepSize, manager, stepStart, y);
                if (Double.isNaN(previousT[0])) {
                    return stopTime;
                }
                stepStart = previousT[0];

                // convert to Nordsieck representation
                scaled    = convertToNordsieckLow();
                nordsieck = convertToNordsieckHigh(scaled);
                interpolator.reinitialize(stepStart, stepSize, scaled, nordsieck);
                interpolator.storeTime(stepStart);

            }

        }

        stopTime  = stepStart;
        stepStart = Double.NaN;
        stepSize  = Double.NaN;
        return stopTime;

    }

    /** Convert the multistep representation after a restart to Nordsieck representation.
     * @return first scaled derivative
     */
    private double[] convertToNordsieckLow() {

        final double[] f0 = previousF[0];
        final double[] scaled = new double[f0.length];
        for (int j = 0; j < f0.length; ++j) {
            scaled[j] = stepSize * f0[j];
        }
        return scaled;

    }

    /** Convert the multistep representation after a restart to Nordsieck representation.
     * @param scaled first scaled derivative
     * @return Nordsieck matrix of the higher scaled derivatives
     */
    private RealMatrix convertToNordsieckHigh(final double[] scaled) {

        final double[] f0 = previousF[0];
        final double[][] multistep = new double[coefficients.msToN.getColumnDimension()][f0.length];
        for (int i = 0; i < multistep.length; ++i) {
            final double[] msI = multistep[i];
            final double[] fI  = previousF[i + 1];
            for (int j = 0; j < f0.length; ++j) {
                msI[j] = stepSize * fI[j] - scaled[j];
            }
        }

        return coefficients.msToN.multiply(new Array2DRowRealMatrix(multistep, false));

    }

    /** Corrector for current state in Adams-Moulton method.
     * <p>
     * This visitor implements the Taylor series formula:
     * <pre>
     * Y<sub>n+1</sub> = y<sub>n</sub> + s<sub>1</sub>(n+1) + [ -1 +1 -1 +1 ... &plusmn;1 ] r<sub>n+1</sub>
     * </pre>
     * </p>
     */
    private static class Corrector implements RealMatrixPreservingVisitor {

        /** Previous state. */
        private final double[] previous;

        /** Current scaled first derivative. */
        private final double[] scaled;

        /** Placeholder where to put the recomputed current state. */
        private final double[] corrected;

        /** Simple constructor.
         * @param previous previous state
         * @param scaled current scaled first derivative
         * @param corrected placeholder where to put the corrected current state
         */
        public Corrector(final double[] previous, final double[] scaled, final double[] corrected) {
            this.previous = previous;
            this.scaled    = scaled;
            this.corrected  = corrected;
        }

        /** {@inheritDoc} */
        public void start(int rows, int columns,
                          int startRow, int endRow, int startColumn, int endColumn) {
            Arrays.fill(corrected, 0.0);
        }

        /** {@inheritDoc} */
        public void visit(int row, int column, double value)
            throws MatrixVisitorException {
            if ((row & 0x1) == 0) {
                corrected[column] -= value;
            } else {
                corrected[column] += value;
            }
        }

        /** {@inheritDoc} */
        public double end() {
            for (int i = 0; i < corrected.length; ++i) {
                corrected[i] += previous[i] + scaled[i];
            }
            return 0;
        }
    }

    /** Updater for Nordsieck vector. */
    private static class NordsieckUpdater extends DefaultRealMatrixChangingVisitor {

        /** Scaled first derivative at step start. */
        private final double[] start;

        /** Scaled first derivative at step end. */
        private final double[] end;

        /** Update coefficients. */
        private final double[] c1;

        /** Simple constructor.
         * @param start scaled first derivative at step start
         * @param end scaled first derivative at step end
         * @param c1 update coefficients
         */
        public NordsieckUpdater(final double[] start, final double[] end,
                                final double[] c1) {
            this.start = start;
            this.end   = end;
            this.c1    = c1;
        }

       /** {@inheritDoc} */
        @Override
        public double visit(int row, int column, double value)
            throws MatrixVisitorException {
            return value + c1[row] * (start[column] - end[column]);
        }

    }

    /** Cache for already computed coefficients. 
     * @param <impements>*/
    private static class CachedCoefficients implements Serializable {

        /**
         * Serialization UID
         */
        private static final long serialVersionUID = -8464316300182136812L;

        /** Transformer between multistep and Nordsieck representations. */
        private final RealMatrix msToN;

        /** Update coefficients of the higher order derivatives wrt y'', y''' ... */
        private final RealMatrix msUpdate;

        /** Update coefficients of the higher order derivatives wrt y'. */
        private final double[] c1;

        /** Simple constructor.
         * @param order order of the method (must be greater than 1: due to
         * an implementation limitation the order 1 method is not supported)
         */
        public CachedCoefficients(int order) {

            // compute exact coefficients
            FieldMatrix<BigFraction> bigNtoMS = buildP(order);
            FieldMatrix<BigFraction> bigMStoN =
                new FieldLUDecompositionImpl<BigFraction>(bigNtoMS).getSolver().getInverse();
            BigFraction[] u = new BigFraction[order - 1];
            Arrays.fill(u, BigFraction.ONE);
            BigFraction[] bigC1 = bigMStoN.operate(u);

            // update coefficients are computed by combining transform from
            // Nordsieck to multistep, then shifting rows to represent step advance
            // then applying inverse transform
            BigFraction[][] shiftedP = bigNtoMS.getData();
            for (int i = shiftedP.length - 1; i > 0; --i) {
                // shift rows
                shiftedP[i] = shiftedP[i - 1];
            }
            shiftedP[0] = new BigFraction[order - 1];
            Arrays.fill(shiftedP[0], BigFraction.ZERO);
            FieldMatrix<BigFraction> bigMSupdate =
                bigMStoN.multiply(new Array2DRowFieldMatrix<BigFraction>(shiftedP, false));

            // convert coefficients to double
            msToN    = MatrixUtils.bigFractionMatrixToRealMatrix(bigMStoN);
            msUpdate = MatrixUtils.bigFractionMatrixToRealMatrix(bigMSupdate);
            c1       = new double[order - 1];
            for (int i = 0; i < order - 1; ++i) {
                c1[i] = bigC1[i].doubleValue();
            }

        }

        /** Build the P matrix transforming multistep to Nordsieck.
         * <p>
         * <p>
         * Multistep representation uses y(k), s<sub>1</sub>(k), s<sub>1</sub>(k-1) ... s<sub>1</sub>(k-(n-1)).
         * Nordsieck representation uses y(k), s<sub>1</sub>(k), s<sub>2</sub>(k) ... s<sub>n</sub>(k).
         * The two representations share their two first components y(k) and
         * s<sub>1</sub>(k). The P matrix is used to transform the remaining ones:
         * <pre>
         * [ s<sub>1</sub>(k-1) ... s<sub>1</sub>(k-(n-1)]<sup>T</sup> = s<sub>1</sub>(k) [1 ... 1]<sup>T</sup> + P [s<sub>2</sub>(k) ... s<sub>n</sub>(k)]<sup>T</sup>
         * </pre>
         * </p>
         * @param order order of the method (must be strictly positive)
         * @return P matrix
         */
        private static FieldMatrix<BigFraction> buildP(final int order) {

            final BigFraction[][] pData = new BigFraction[order - 1][order - 1];

            for (int i = 0; i < pData.length; ++i) {
                // build the P matrix elements from Taylor series formulas
                final BigFraction[] pI = pData[i];
                final int factor = -(i + 1);
                int aj = factor;
                for (int j = 0; j < pI.length; ++j) {
                    pI[j] = new BigFraction(aj * (j + 2));
                    aj *= factor;
                }
            }

            return new Array2DRowFieldMatrix<BigFraction>(pData, false);

        }

    }

    /** Serialize the instance.
     * @param oos stream where object should be written
     * @throws IOException if object cannot be written to stream
     */
    private void writeObject(ObjectOutputStream oos)
        throws IOException {
        oos.defaultWriteObject();
        oos.writeInt(coefficients.msToN.getRowDimension() + 1);
    }

    /** Deserialize the instance.
     * @param ois stream from which the object should be read
     * @throws ClassNotFoundException if a class in the stream cannot be found
     * @throws IOException if object cannot be read from the stream
     */
    private void readObject(ObjectInputStream ois)
      throws ClassNotFoundException, IOException {
        try {

            ois.defaultReadObject();
            final int order = ois.readInt();

            final Class<AdamsMoultonIntegrator> cl = AdamsMoultonIntegrator.class;
            final Field f = cl.getDeclaredField("coefficients");
            f.setAccessible(true);

            // cache the coefficients for each order, to avoid recomputing them
            synchronized(cache) {
                CachedCoefficients coeff = cache.get(order);
                if (coeff == null) {
                    coeff = new CachedCoefficients(order);
                    cache.put(order, coeff);
                }
                f.set(this, coeff);
            }

        } catch (NoSuchFieldException nsfe) {
            IOException ioe = new IOException();
            ioe.initCause(nsfe);
            throw ioe;
        } catch (IllegalAccessException iae) {
            IOException ioe = new IOException();
            ioe.initCause(iae);
            throw ioe;
        }

    }

}
