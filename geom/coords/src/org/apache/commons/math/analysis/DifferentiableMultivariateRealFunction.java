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

package org.apache.commons.math.analysis;

/** 
 * Extension of {@link MultivariateRealFunction} representing a differentiable
 * multivariate real function.
 * @version $Revision: 758046 $ $Date: 2009-03-24 18:05:13 -0400 (Tue, 24 Mar 2009) $
 * @since 2.0
 */
public interface DifferentiableMultivariateRealFunction extends MultivariateRealFunction {

    /**
     * Returns the partial derivative of the function with respect to a point coordinate.
     * <p>
     * The partial derivative is defined with respect to point coordinate
     * x<sub>k</sub>. If the partial derivatives with respect to all coordinates are
     * needed, it may be more efficient to use the {@link #gradient()} method which will
     * compute them all at once.
     * </p>
     * @param k index of the coordinate with respect to which the partial
     * derivative is computed
     * @return the partial derivative function with respect to k<sup>th</sup> point coordinate
     */
    MultivariateRealFunction partialDerivative(int k);

    /**
     * Returns the gradient function.
     * <p>If only one partial derivative with respect to a specific coordinate is
     * needed, it may be more efficient to use the {@link #partialDerivative(int)} method
     * which will compute only the specified component.</p>
     * @return the gradient function
     */
    MultivariateVectorialFunction gradient();

}
