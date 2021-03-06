/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2018, Grum Ltd (Romain Gallet)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of Geocalc nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.hanatour.anymeal.geocalc;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.tan;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

import lombok.AllArgsConstructor;
import lombok.val;

/**
 * Earth related calculations.
 */
public class EarthCalc {

    public static final double EARTH_RADIUS = 6_356_752.314245D; // radius at the poles, meters

    public static class gcd {
        /**
         * This is the half-way point along a great circle path between the two points.
         *
         * @param standPoint standPoint
         * @param forePoint  standPoint
         * @return mid point
         * @see <a href="http://www.movable-type.co.uk/scripts/latlong.html"></a>
         */
        public static Point midPoint(Point standPoint, Point forePoint) {
            val ??1 = toRadians(standPoint.longitude);
            val ??2 = toRadians(forePoint.longitude);

            val ??1 = toRadians(standPoint.latitude);
            val ??2 = toRadians(forePoint.latitude);

            val Bx = cos(??2) * cos(??2 - ??1);
            val By = cos(??2) * sin(??2 - ??1);

            val ??3 = atan2(sin(??1) + sin(??2), sqrt((cos(??1) + Bx) * (cos(??1) + Bx) + By * By));
            val ??3 = ??1 + atan2(By, cos(??1) + Bx);

            return Point.at(Coordinate.fromRadians(??3), Coordinate.fromRadians(??3));
        }

        /**
         * Returns the distance between two points at spherical law of cosines.
         *
         * @param standPoint The stand point
         * @param forePoint  The fore point
         * @return The distance, in meters
         */
        public static double distance(Point standPoint, Point forePoint) {

            val ???? = toRadians(abs(forePoint.longitude - standPoint.longitude));
            val ??1 = toRadians(standPoint.latitude);
            val ??2 = toRadians(forePoint.latitude);

            //spherical law of cosines
            val sphereCos = (sin(??1) * sin(??2)) + (cos(??1) * cos(??2) * cos(????));
            val c = acos(max(min(sphereCos, 1d), -1d));

            return EARTH_RADIUS * c;
        }

        /**
         * Returns the coordinates of a point which is "distance" away
         * from standPoint in the direction of "bearing"
         * <p>
         * Note: North is equal to 0 for bearing value
         *
         * @param standPoint Origin
         * @param bearing    Direction in degrees, clockwise from north
         * @param distance   distance in meters
         * @return forePoint coordinates
         * @see <a href="http://www.movable-type.co.uk/scripts/latlong.html"></a>
         */
        public static Point pointAt(Point standPoint, double bearing, double distance) {
        /*
         ??2 = asin( sin ??1 ??? cos ?? + cos ??1 ??? sin ?? ??? cos ?? )
         ??2 = ??1 + atan2( sin ?? ??? sin ?? ??? cos ??1, cos ?? ??? sin ??1 ??? sin ??2 )

         where
         ?? is latitude,
         ?? is longitude,
         ?? is the bearing (clockwise from north),
         ?? is the angular distance d/R;
         d being the distance travelled, R the earth???s radius
         */

            val ??1 = toRadians(standPoint.latitude);
            val ??1 = toRadians(standPoint.longitude);
            val ?? = toRadians(bearing);
            val ?? = distance / EARTH_RADIUS; // normalize linear distance to radian angle

            val ??2 = asin(sin(??1) * cos(??) + cos(??1) * sin(??) * cos(??));
            val ??2 = ??1 + atan2(sin(??) * sin(??) * cos(??1), cos(??) - sin(??1) * sin(??2));

            val ??2_harmonised = (??2 + 3 * PI) % (2 * PI) - PI; // normalise to ???180..+180??

            return Point.at(Coordinate.fromRadians(??2), Coordinate.fromRadians(??2_harmonised));
        }

        /**
         * Returns the (azimuth) bearing, in decimal degrees, from standPoint to forePoint
         *
         * @param standPoint Origin point
         * @param forePoint  Destination point
         * @return (azimuth) bearing, in decimal degrees
         */
        public static double bearing(Point standPoint, Point forePoint) {
            /*
             * Formula: ?? = atan2( 	sin(??long).cos(lat2), cos(lat1).sin(lat2) ??? sin(lat1).cos(lat2).cos(??long) )
             */

            val ??long = toRadians(forePoint.longitude - standPoint.longitude);
            val y = sin(??long) * cos(toRadians(forePoint.latitude));
            val x = cos(toRadians(standPoint.latitude)) * sin(toRadians(forePoint.latitude))
                    - sin(toRadians(standPoint.latitude)) * cos(toRadians(forePoint.latitude)) * cos(??long);

            val bearing = (atan2(y, x) + 2 * PI) % (2 * PI);

            return toDegrees(bearing);
        }

        /**
         * Returns an area around standPoint
         *
         * @param standPoint The centre of the area
         * @param distance   Distance around standPoint, im meters
         * @return The area
         * @see <a href="http://www.movable-type.co.uk/scripts/latlong.html"></a>
         */
        public static BoundingArea around(Point standPoint, double distance) {

            //45 degrees going north-east
            val northEast = pointAt(standPoint, 45, distance);

            //225 degrees going south-west
            val southWest = pointAt(standPoint, 225, distance);

            return BoundingArea.at(northEast, southWest);
        }
    }

    public static class haversine {
        /**
         * Returns the distance between two points at Haversine formula.
         *
         * @param standPoint The stand point
         * @param forePoint  The fore point
         * @return The distance, in meters
         */
        public static double distance(Point standPoint, Point forePoint) {

            val ???? = toRadians(abs(forePoint.longitude - standPoint.longitude));
            val ??1 = toRadians(standPoint.latitude);
            val ??2 = toRadians(forePoint.latitude);

            // haversine formula
            val ???? = toRadians(abs(forePoint.latitude - standPoint.latitude));
            val a = sin(???? / 2) * sin(???? / 2) + cos(??1) * cos(??2) * sin(???? / 2) * sin(???? / 2);
            val c = 2 * atan2(sqrt(a), sqrt(1 - a)); //angular distance in radians

            return EARTH_RADIUS * c;
        }
    }

    public static class vincenty {
        /**
         * Calculate distance, (azimuth) bearing and final bearing between standPoint and forePoint.
         *
         * @param standPoint The stand point
         * @param forePoint  The fore point
         * @return Vincenty object which holds all 3 values
         */
        private static Vincenty vincenty(Point standPoint, Point forePoint) {
            val ??1 = toRadians(standPoint.longitude);
            val ??2 = toRadians(forePoint.longitude);

            val ??1 = toRadians(standPoint.latitude);
            val ??2 = toRadians(forePoint.latitude);

            val a = 6_378_137D; // radius at equator
            val b = EARTH_RADIUS; // Using b to keep close to academic formula.
            val f = 1 / 298.257223563D; // flattening of the ellipsoid

            val L = ??2 - ??1;
            val tanU1 = (1 - f) * tan(??1);
            val cosU1 = 1 / sqrt((1 + tanU1 * tanU1));
            val sinU1 = tanU1 * cosU1;

            val tanU2 = (1 - f) * tan(??2);
            val cosU2 = 1 / sqrt((1 + tanU2 * tanU2));
            val sinU2 = tanU2 * cosU2;

            double ?? = L, ????, iterationLimit = 100, cosSq??, ??, cos2??M, cos??, sin??, sin??, cos??;
            do {
                sin?? = sin(??);
                cos?? = cos(??);
                val sinSq?? = (cosU2 * sin??) * (cosU2 * sin??) + (cosU1 * sinU2 - sinU1 * cosU2 * cos??) * (cosU1 * sinU2 - sinU1 * cosU2 * cos??);
                sin?? = sqrt(sinSq??);
                if (sin?? == 0) return Vincenty.CO_INCIDENT_POINTS;  // co-incident points
                cos?? = sinU1 * sinU2 + cosU1 * cosU2 * cos??;
                ?? = atan2(sin??, cos??);
                val sin?? = cosU1 * cosU2 * sin?? / sin??;
                cosSq?? = 1 - sin?? * sin??;
                cos2??M = cos?? - 2 * sinU1 * sinU2 / cosSq??;

                if (Double.isNaN(cos2??M)) cos2??M = 0;  // equatorial line: cosSq??=0
                val C = f / 16 * cosSq?? * (4 + f * (4 - 3 * cosSq??));
                ???? = ??;
                ?? = L + (1 - C) * f * sin?? * (?? + C * sin?? * (cos2??M + C * cos?? * (-1 + 2 * cos2??M * cos2??M)));
            } while (abs(?? - ????) > 1e-12 && --iterationLimit > 0);

            if (iterationLimit == 0) throw new IllegalStateException("Formula failed to converge");

            val uSq = cosSq?? * (a * a - b * b) / (b * b);
            val A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
            val B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));
            val ???? = B * sin?? * (cos2??M + B / 4 * (cos?? * (-1 + 2 * cos2??M * cos2??M) -
                    B / 6 * cos2??M * (-3 + 4 * sin?? * sin??) * (-3 + 4 * cos2??M * cos2??M)));

            val distance = b * A * (?? - ????);

            var initialBearing = atan2(cosU2 * sin??, cosU1 * sinU2 - sinU1 * cosU2 * cos??);
            initialBearing = (initialBearing + 2 * PI) % (2 * PI); //turning value to trigonometric direction

            var finalBearing = atan2(cosU1 * sin??, -sinU1 * cosU2 + cosU1 * sinU2 * cos??);
            finalBearing = (finalBearing + 2 * PI) % (2 * PI);  //turning value to trigonometric direction

            return new Vincenty(distance, toDegrees(initialBearing), toDegrees(finalBearing));
        }

        public static double distance(Point standPoint, Point forePoint) {
            return vincenty(standPoint, forePoint).distance;
        }

        /**
         * Returns (azimuth) bearing at Vincenty formula.
         *
         * @param standPoint The stand point
         * @param forePoint  The fore point
         * @return (azimuth) bearing in degrees to the North
         * @see <a href="http://www.movable-type.co.uk/scripts/latlong.html"></a>
         */
        public static double bearing(Point standPoint, Point forePoint) {
            return vincenty(standPoint, forePoint).initialBearing;
        }

        /**
         * Returns final bearing in direction of standPoint???forePoint at Vincenty formula.
         *
         * @param standPoint The stand point
         * @param forePoint  The fore point
         * @return (azimuth) bearing in degrees to the North
         * @see <a href="http://www.movable-type.co.uk/scripts/latlong.html"></a>
         */
        public static double finalBearing(Point standPoint, Point forePoint) {
            return vincenty(standPoint, forePoint).finalBearing;
        }

        @AllArgsConstructor
        private static class Vincenty {
            final static Vincenty CO_INCIDENT_POINTS = new Vincenty(0, 0, 0);

            /**
             * distance is the distance in meter
             * initialBearing is the initial bearing, or forward azimuth (in reference to North point), in degrees
             * finalBearing is the final bearing (in direction p1???p2), in degrees
             */
            final double distance, initialBearing, finalBearing;
        }
    }
}
