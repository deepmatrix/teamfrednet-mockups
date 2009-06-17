/* Copyright 2002-2008 CS Communication & Systèmes
 * Licensed to CS Communication & Systèmes (CS) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * CS licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.orekit.propagation.events;

import org.apache.commons.math.geometry.Vector3D;
import org.orekit.bodies.BodyShape;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.propagation.SpacecraftState;
import org.orekit.utils.PVCoordinates;

/** Finder for satellite altitude crossing events.
 * <p>This class finds altitude events (i.e. satellite crossing
 * a predefined altitude level above ground).</p>
 * <p>The default implementation behavior is to {@link
 * EventDetector#CONTINUE continue} propagation when ascending and to
 * {@link EventDetector#STOP stop} propagation
 * when descending. This can be changed by overriding the
 * {@link #eventOccurred(SpacecraftState) eventOccurred} method in a
 * derived class.</p>
 * @see org.orekit.propagation.Propagator#addEventDetector(EventDetector)
 * @author Luc Maisonobe
 * @version $Revision: 2190 $ $Date: 2008-10-03 14:47:09 +0200 (ven 03 oct 2008) $
 */
public class AltitudeDetector extends AbstractDetector {

    /** Serializable UID. */
    private static final long serialVersionUID = 5811148350768568016L;

    /** Threshold altitude value. */
    private final double altitude;

    /** Body shape with respect to which altitude should be evaluated. */
    private final BodyShape bodyShape;

    /** Build a new instance.
     * <p>The maximal interval between elevation checks should
     * be smaller than the half period of the orbit.</p>
     * @param maxCheck maximal interval in seconds
     * @param altitude threshold altitude value
     * @param bodyShape body shape with respect to which altitude should be evaluated
     */
    public AltitudeDetector(final double maxCheck, final double altitude,
                            final BodyShape bodyShape) {
        super(maxCheck, 1.0e-3);
        this.altitude = altitude;
        this.bodyShape = bodyShape;
    }

    /** Get the threshold altitude value.
     * @return the threshold altitude value
     */
    public double getAltitude() {
        return altitude;
    }

    /** Get the body shape.
     * @return the body shape
     */
    public BodyShape getBodyShape() {
        return bodyShape;
    }

    /** Handle an altitude event and choose what to do next.
     * <p>The default implementation behavior is to {@link
     * EventDetector#CONTINUE continue} propagation when ascending and to
     * {@link EventDetector#STOP stop} propagation
     * when descending. This can be changed by overriding the
     * {@link #eventOccurred(SpacecraftState) eventOccurred} method in a
     * derived class.</p>
     * @param s the current state information : date, kinematics, attitude
     * @return one of {@link #STOP}, {@link #RESET_STATE}, {@link #RESET_DERIVATIVES}
     * or {@link #CONTINUE}
     * @exception OrekitException if some specific error occurs
     */
    public int eventOccurred(final SpacecraftState s) throws OrekitException {
        final Frame bodyFrame      = bodyShape.getBodyFrame();
        final PVCoordinates pvBody = s.getPVCoordinates(bodyFrame);
        final GeodeticPoint point  = bodyShape.transform(pvBody.getPosition(),
                                                         bodyFrame, s.getDate());
        final double zVelocity     = Vector3D.dotProduct(pvBody.getVelocity(),
                                                         point.getZenith());
        return (zVelocity > 0) ? CONTINUE : STOP;
    }

    /** {@inheritDoc} */
    public double g(final SpacecraftState s) throws OrekitException {
        final Frame bodyFrame      = bodyShape.getBodyFrame();
        final PVCoordinates pvBody = s.getPVCoordinates(bodyFrame);
        final GeodeticPoint point  = bodyShape.transform(pvBody.getPosition(),
                                                         bodyFrame, s.getDate());
        return point.getAltitude() - altitude;
    }

}
