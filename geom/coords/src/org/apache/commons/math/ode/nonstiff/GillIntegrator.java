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

import java.io.Serializable;

/**
 * This class implements the Gill fourth order Runge-Kutta
 * integrator for Ordinary Differential Equations .

 * <p>This method is an explicit Runge-Kutta method, its Butcher-array
 * is the following one :
 * <pre>
 *    0  |    0        0       0      0
 *   1/2 |   1/2       0       0      0
 *   1/2 | (q-1)/2  (2-q)/2    0      0
 *    1  |    0       -q/2  (2+q)/2   0
 *       |-------------------------------
 *       |   1/6    (2-q)/6 (2+q)/6  1/6
 * </pre>
 * where q = sqrt(2)</p>
 *
 * @see EulerIntegrator
 * @see ClassicalRungeKuttaIntegrator
 * @see MidpointIntegrator
 * @see ThreeEighthesIntegrator
 * @version $Revision: 785473 $ $Date: 2009-06-17 00:02:35 -0400 (Wed, 17 Jun 2009) $
 * @since 1.2
 */

public class GillIntegrator
  extends RungeKuttaIntegrator implements Serializable {

  /** Serializable version identifier. */
  private static final long serialVersionUID = 5566682259665027132L;

  /** Time steps Butcher array. */
  private static final double[] c = {
    1.0 / 2.0, 1.0 / 2.0, 1.0
  };

  /** Internal weights Butcher array. */
  private static final double[][] a = {
    { 1.0 / 2.0 },
    { (Math.sqrt(2.0) - 1.0) / 2.0, (2.0 - Math.sqrt(2.0)) / 2.0 },
    { 0.0, -Math.sqrt(2.0) / 2.0, (2.0 + Math.sqrt(2.0)) / 2.0 }
  };

  /** Propagation weights Butcher array. */
  private static final double[] b = {
    1.0 / 6.0, (2.0 - Math.sqrt(2.0)) / 6.0, (2.0 + Math.sqrt(2.0)) / 6.0, 1.0 / 6.0
  };

  /** Simple constructor.
   * Build a fourth-order Gill integrator with the given step.
   * @param step integration step
   */
  public GillIntegrator(final double step) {
    super("Gill", c, a, b, new GillStepInterpolator(), step);
  }

}
