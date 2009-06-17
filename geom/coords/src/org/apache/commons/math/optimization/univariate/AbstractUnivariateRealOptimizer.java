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

package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math.ConvergingAlgorithmImpl;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.optimization.UnivariateRealOptimizer;

/**
 * Provide a default implementation for several functions useful to generic
 * optimizers.
 *  
 * @version $Revision: 758056 $ $Date: 2009-03-24 18:14:03 -0400 (Tue, 24 Mar 2009) $
 * @since 2.0
 */
public abstract class AbstractUnivariateRealOptimizer
    extends ConvergingAlgorithmImpl implements UnivariateRealOptimizer {

    /** Serializable version identifier. */
    private static final long serialVersionUID = 4543031162377070699L;

    /** Indicates where a root has been computed. */
    protected boolean resultComputed;

    /** The last computed root. */
    protected double result;

    /** Value of the function at the last computed result. */
    protected double functionValue;

    /**
     * Construct a solver with given iteration count and accuracy.
     * 
     * @param defaultAbsoluteAccuracy maximum absolute error
     * @param defaultMaximalIterationCount maximum number of iterations
     * @throws IllegalArgumentException if f is null or the 
     * defaultAbsoluteAccuracy is not valid
     */
    protected AbstractUnivariateRealOptimizer(final int defaultMaximalIterationCount,
                                              final double defaultAbsoluteAccuracy) {
        super(defaultMaximalIterationCount, defaultAbsoluteAccuracy);
        resultComputed = false;
    }

    /** Check if a result has been computed.
     * @exception IllegalStateException if no result has been computed
     */
    protected void checkResultComputed() throws IllegalStateException {
        if (!resultComputed) {
            throw MathRuntimeException.createIllegalStateException("no result available");
        }
    }

    /** {@inheritDoc} */
    public double getResult() {
        checkResultComputed();
        return result;
    }

    /** {@inheritDoc} */
    public double getFunctionValue() {
        checkResultComputed();
        return functionValue;
    }

    /**
     * Convenience function for implementations.
     * 
     * @param result the result to set
     * @param iterationCount the iteration count to set
     */
    protected final void setResult(final double result, final int iterationCount) {
        this.result         = result;
        this.iterationCount = iterationCount;
        this.resultComputed = true;
    }

    /**
     * Convenience function for implementations.
     * 
     * @param x the result to set
     * @param fx the result to set
     * @param iterationCount the iteration count to set
     */
    protected final void setResult(final double x, final double fx,
                                   final int iterationCount) {
        this.result         = x;
        this.functionValue  = fx;
        this.iterationCount = iterationCount;
        this.resultComputed = true;
    }

    /**
     * Convenience function for implementations.
     */
    protected final void clearResult() {
        this.resultComputed = false;
    }

}