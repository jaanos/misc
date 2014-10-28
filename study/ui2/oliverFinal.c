//*!!Sensor,    S1,                sonar, sensorSONAR9V,      ,                  !!*//
//*!!Sensor,    S2,               touchR, sensorTouch,      ,                    !!*//
//*!!Sensor,    S3,               touchL, sensorTouch,      ,                    !!*//
//*!!                                                                            !!*//
//*!!Start automatically generated configuration code.                           !!*//
const tSensors sonar                = (tSensors) S1;   //sensorSONAR9V      //*!!!!*//
const tSensors touchR               = (tSensors) S2;   //sensorTouch        //*!!!!*//
const tSensors touchL               = (tSensors) S3;   //sensorTouch        //*!!!!*//
//*!!CLICK to edit 'wizard' created sensor & motor configuration.                !!*//

#pragma fileExtension("rtm")
#pragma platform("NXT")

typedef struct
{
   int x = 0;
   int y = 0;
   int angle = -1;
   int r13 = -1;
   int maxd = -1;
   int maxa = -1;

} Location;

Location location;
bool run;

const int MIN_DIST_FOR_CERTAIN_WALL = 50;
const int sh_theta_binsize = 2;

const int MIN_DIST_FOR_WALL = 0;
const int MAX_DIST_FOR_WALL = 200;
const int MIN_POINTS_FOR_WALL = 10;
const int bh_theta_binsize = 10;
const int bh_r_binsize = 5;

int mod(int n, int m) {
	if (n < 0) {
		n += (1 + (-n)/m) * m;
	}
	return n - n / m * m;
}

int abs2(int n) {
	if (n < 0) {
		return -n;
	}
	return n;
}

int min(int val1, int val2) {
	if (val1 < val2) {
		return val1;
	}
	return val2;
}

int min(int val1, int val2, int val3, int val4) {
	return min(min(val1, val2), min(val3, val4));
}

int atan2(float x, float y) {
	if (x == 0) return 90*sgn(y);
	if (y == 0 && x < 0) return 180;
	int phi = radiansToDegrees(atan(abs(y/x)));
	if (x < 0) phi = 180 - phi;
	return phi*sgn(y);
}

void rotateSonar(int nDegrees)
{
	if (nDegrees == 0) return;
	nxtDisplayTextLine(1, "rotateSonar(%d)", nDegrees);

	int sign = 1;
	if (nDegrees < 0)
	{
		sign = -1;
	}
	nDegrees = abs(nDegrees);
	nMotorEncoderTarget[motorA] = nDegrees * 1; // incremental amount to move motor
	motor[motorA]               = sign * 40;    // motor speed

	while (nMotorRunState[motorA] != runStateIdle)
	{
		wait1Msec(1);
	}

	nxtDisplayTextLine(1, "");
}

void Go_Straight(int speed)
{
	nxtDisplayTextLine(1, "Go_Straight(%d)", speed);
	nSyncedMotors = synchBC;
	nSyncedTurnRatio = 100;
  //nSyncedMotors = synchNone;
	motor[motorB] = speed;   // motor forward at 50% of full power
	//motor[motorC] = speed;
  //nSyncedMotors = synchNone;

	nxtDisplayTextLine(1, "");
}

void stop() {
	nxtDisplayTextLine(1, "stop()");
	nSyncedMotors = synchNone;
	motor[motorB] = 0;
  motor[motorC] = 0;
}

void rotateRight(int degrees, int speed)
{
	if (degrees == 0) return;
	if (SensorValue[touchL] || SensorValue[touchR]) {
		Go_Straight(-100);
		wait10Msec(100);
		stop();
	}
	nxtDisplayTextLine(1, "rotateRight(%d, %d)", degrees, speed);
	bFloatDuringInactiveMotorPWM = false;
	int sign;
	if (degrees > 0) {
		sign = 1;
	} else {
		sign = -1;
	}
	//degrees = abs(degrees);
	int nMotorDegrees = degrees * 2.48;
	nSyncedMotors = synchBC;
	nSyncedTurnRatio = -100;
	nMotorEncoder[motorB] = 0;
	nMotorEncoderTarget[motorB] = nMotorDegrees; // incremental amount to move motor
	motor[motorB]               = sign * speed;

	while (nMotorRunState[motorB] != runStateIdle)
	{

	}

	nSyncedTurnRatio = 100;
	nSyncedMotors = synchNone;

	nxtDisplayTextLine(1, "");
}

void TurnRight() {
	nxtDisplayTextLine(1, "TurnRight()");
	nSyncedMotors = synchNone;
	motor[motorB] = 35;
  motor[motorC] = 50;
  wait10Msec(30);
  motor[motorB] = 50;
  motor[motorC] = 100;
  wait10Msec(100);
  motor[motorB] = 35;
  motor[motorC] = 50;
  wait10Msec(20);
  motor[motorB] = 50;
  motor[motorC] = 35;
  wait10Msec(10);

  nxtDisplayTextLine(1, "");
}

void TurnLeft() {
	nxtDisplayTextLine(1, "TurnLeft()");
	nSyncedMotors = synchNone;
	motor[motorB] = 50;
  motor[motorC] = 35;
  wait10Msec(30);
  motor[motorB] = 90;
  motor[motorC] = 50;
  wait10Msec(100);
  motor[motorB] = 50;
  motor[motorC] = 35;
  wait10Msec(20);
  motor[motorB] = 35;
  motor[motorC] = 50;
  wait10Msec(10);

  nxtDisplayTextLine(1, "");
}

void SharpTurnRight() {
	nxtDisplayTextLine(1, "SharpTurnRight()");
	nSyncedMotors = synchNone;
	motor[motorB] = -70;
  motor[motorC] = 100;
  wait10Msec(5);
  motor[motorB] = -100;
  motor[motorC] = 100;
  wait10Msec(25);
  motor[motorB] = -70;
  motor[motorC] = 50;
  wait10Msec(5);
  motor[motorB] = 50;
  motor[motorC] = -70;
  wait10Msec(5);
  motor[motorB] = 0;
  motor[motorC] = 0;

  nxtDisplayTextLine(1, "");
}

void SharpTurnLeft() {
	nxtDisplayTextLine(1, "SharpTurnLeft()");
	nSyncedMotors = synchNone;
	motor[motorB] = 50;
  motor[motorC] = -70;
  wait10Msec(5);
  motor[motorB] = 90;
  motor[motorC] = -100;
  wait10Msec(25);
  motor[motorB] = 50;
  motor[motorC] = -70;
  wait10Msec(5);
  motor[motorB] = -70;
  motor[motorC] = 50;
  wait10Msec(5);
  motor[motorB] = 0;
  motor[motorC] = 0;

  nxtDisplayTextLine(1, "");
}

void whereAmI(bool hough) {
	if (SensorValue[touchL] || SensorValue[touchR]) {
		Go_Straight(-100);
		wait10Msec(100);
		stop();
	}

	// Stores distances.
	int distances[360];
	int distance = SensorValue[sonar];
	int direction = mod(nMotorEncoder[motorA], 360);
	if (distance < 255) {
		distances[direction] = distance;
	}
	location.maxd = distance;
	location.maxa = direction;
	for (int i = 360; i > 0; i--) {
		nMotorEncoderTarget[motorA] = 1;
		motor[motorA]               = -25;

		while (nMotorRunState[motorA] != runStateIdle) {
			wait1Msec(1);
		}

		distance = SensorValue[sonar];
		direction = mod(nMotorEncoder[motorA], 360);
		nxtDisplayBigStringAt(15, 20, "%3d %3d", distance, direction);
		if (distance < 255) {
			distances[direction] = distance;
		} else {
			distances[direction] = 0;
		}
		if (distance > location.maxd) {
			location.maxd = distance;
			location.maxa = direction;
		}
	}

	nMotorEncoderTarget[motorA] = 360; // incremental amount to move motor
	motor[motorA]               = 40;    // motor speed

	if (hough) {
		// Computes original x and y.
		nxtDisplayTextLine(1, "Computing x, y...");
		int xy[360][2];
		for (int angle = 0; angle < 360; angle++) {
			xy[angle][0] = - distances[angle] * cosDegrees(angle);
			xy[angle][1] = - distances[angle] * sinDegrees(angle);
		}

		// Estimates corner direction.
		nxtDisplayTextLine(1, "Estimating corner direction...");
		int max = -1;
		int bin;
		int small_hough[300 / sh_theta_binsize];
		for (int i=0; i < 250 / sh_theta_binsize; i++) small_hough[i] = 0;
	 	for (int theta = 0; theta < 360; theta++) {
	 		if (distances[theta] > MIN_DIST_FOR_CERTAIN_WALL) {
	      int val = ++small_hough[distances[theta] / sh_theta_binsize];
	      if (val > max) {
	      	max = val;
	      	bin = distances[theta] / sh_theta_binsize;
	      }
			}
		}
		int tmp[360];
		for (int theta = 0; theta < 360; theta++) {
			if (distances[theta] > MIN_DIST_FOR_CERTAIN_WALL && distances[theta] >= bin * sh_theta_binsize && distances[theta] <= (bin + 1) * sh_theta_binsize) {
				tmp[theta] = 1;
			} else {
				tmp[theta] = 0;
			}
		}
		int corner_directions[360];
		for (int theta = 0; theta < 90; theta++) {
			corner_directions[0] += tmp[theta];
		}
		for (int theta = 1; theta < 270; theta++) {
	  	  corner_directions[theta] = corner_directions[theta - 1] - tmp[theta - 1] + tmp[theta + 90];
		}
		for (int theta = 270; theta < 360; theta++) {
		 	  corner_directions[theta] = corner_directions[theta - 1] - tmp[theta - 1] + tmp[theta - 270];
		}
		max = -1;
		int corner_direction;
		for (int theta = 0; theta < 360; theta++) {
			if (tmp[theta] == 0) {
				corner_directions[theta] = 0;
			}
			else if (corner_directions[theta] > max) {
	      	max = corner_directions[theta];
	      	corner_direction = theta;
			}
		}

		// Computes new x and y.
		nxtDisplayTextLine(1, "Computing new x, y...");
		for (int theta = 0; theta < 360; theta++) {
	    int orientation = mod(abs(corner_direction - theta + 45), 90);
	    if (orientation > 45) {
	        orientation = 90 - orientation;
	    }
	    orientation /= 45;

	    int tmp = 1 + 0.3 * orientation^2;
	    xy[theta][0] *= tmp;
	    xy[theta][1] *= tmp;
		}

		// Estimates walls.
		nxtDisplayTextLine(1, "Estimating walls...");
		int big_hough[300 / bh_r_binsize][360 / bh_theta_binsize];
		/*for (int bin = 0; bin < 360 / bh_theta_binsize; bin++) {
			for (int r = 0; r < 250 / bh_r_binsize; r++) {
	   		big_hough[r][bin] = 0;
		  }
	    int bin_theta = bh_theta_binsize * bin;
	    for (int theta = 0; theta < 360; theta++) {
	    	if (distances[theta] > 0) {
	    		int bin_r = cosDegrees(bin_theta) * xy[theta][0] + sinDegrees(bin_theta) * xy[theta][1];
	        if (bin_r > MIN_DIST_FOR_WALL && bin_r < MAX_DIST_FOR_WALL) {
	        	big_hough[bin_r / bh_r_binsize][bin]++;
	        }
	      }
	    }
	    for (int r = 0; r < 250 / bh_r_binsize; r++) {
	    	if (big_hough[r][bin] < MIN_POINTS_FOR_WALL) {
	    		big_hough[r][bin] = 0;
	  	  }
		  }
		}*/
		for (int r = 0; r < 250 / bh_r_binsize; r++) {
			for (int bin = 0; bin < 360 / bh_theta_binsize; bin++) {
	   		big_hough[r][bin] = 0;
		  }
		}
	  int bin_theta;
	  for (int theta = 0; theta < 360; theta++) {
	  	bin_theta = 0;
	  	for (int bin = 0; bin < 360 / bh_theta_binsize; bin++) {
	    	if (distances[theta] > 0) {
	    		int bin_r = cosDegrees(bin_theta) * xy[theta][0] + sinDegrees(bin_theta) * xy[theta][1];
	        if (bin_r > MIN_DIST_FOR_WALL && bin_r < MAX_DIST_FOR_WALL) {
	        	big_hough[bin_r / bh_r_binsize][bin]++;
	        }
	      }
	      bin_theta += bh_theta_binsize;
	    }
	  }
	  for (int r = 0; r < 250 / bh_r_binsize; r++) {
	  	for (int bin = 0; bin < 360 / bh_theta_binsize; bin++) {
	    	if (big_hough[r][bin] < MIN_POINTS_FOR_WALL) {
	    		big_hough[r][bin] = 0;
	  	  }
		  }
		}

		// Computes walls.
		nxtDisplayTextLine(1, "Computing walls...");
		int value1 = -1;
		int theta1;
		int r1;
		for (int r = 0; r < 250 / bh_r_binsize; r++) {
			for (int theta = 0; theta < 360 / bh_theta_binsize; theta++) {
	    	if (big_hough[r][theta] > value1) {
	    		value1 = big_hough[r][theta];
	    		theta1 = theta;
	    		r1 = r;
	    	}
		  }
		}
		theta1 *= bh_theta_binsize;
		r1 *= bh_r_binsize;

		int theta2 = mod(theta1 + 90, 360);
		int theta3 = mod(theta1 + 180, 360);
		int theta4 = mod(theta1 + 270, 360);

		int value2 = -1;
		int r2;
	  for (int r = 0; r < 250 / bh_r_binsize; r++) {
	  	if (big_hough[r][theta2 / bh_theta_binsize] > value2) {
	    	value2 = big_hough[r][theta2 / bh_theta_binsize];
	    	r2 = r;
	    }
		}
		r2 *= bh_r_binsize;

		int value3 = -1;
		int r3;
	  for (int r = 0; r < 250 / bh_r_binsize; r++) {
	  	if (big_hough[r][theta3 / bh_theta_binsize] > value3) {
	    	value3 = big_hough[r][theta3 / bh_theta_binsize];
	    	r3 = r;
	    }
		}
		r3 *= bh_r_binsize;

		int value4 = -1;
		int r4;
	  for (int r = 0; r < 250 / bh_r_binsize; r++) {
	  	if (big_hough[r][theta4 / bh_theta_binsize] > value4) {
	    	value4 = big_hough[r][theta4 / bh_theta_binsize];
	    	r4 = r;
	    }
		}
		r4 *= bh_r_binsize;

		/*
		int minvalue = min(value1, value2, value3, value4);
		if (minvalue == value4) {
			// do nothing
		}
		else if (minvalue == value3) {
			r3 = r2;
	    theta3 = theta2;
			// value3 = value2;
	    r2 = r1;
	    theta2 = theta1;
			// value2 = value1;
	    r1 = r4;
	    theta1 = theta4;
			// value1 = value4;
		}
		else if (minvalue == value2) {
	    r2 = r4;
	    theta2 = theta4;
			// value2 = value4;
	    r4 = r1;
	    theta4 = theta1;
	    r1 = r3;
	    theta1 = theta3;
	    r3 = r4;
	    theta3 = theta4;
		}
		else {
			// cannot happen
		}
		*/
		// Checks walls.
		nxtDisplayTextLine(1, "Checking walls...");
		int theta1_180 = mod(theta1, 180);
		int theta2_180 = mod(theta2, 180);
		int theta3_180 = mod(theta3, 180);
		int theta1_3_180 = abs2(theta1_180 - theta3_180);
		int theta1_2_180 = abs2(theta1_180 - theta2_180);
		int theta2_3_180 = abs2(theta2_180 - theta3_180);
		int tmp2 = abs2(theta1_2_180 - 90);
		int tmp3 = abs2(theta2_3_180 - 90);
		location.r13 = r1;
		location.r13 += r3;
		/*if (r13 < 180 || theta1_3_180 > bh_theta_binsize || tmp2 > bh_theta_binsize || tmp3 > bh_theta_binsize) {
			location.x = 0;
			location.y = 0;
			location.angle = -1;
		}
	 	else {*/
	  //location.x = (r3 - r1) / 2;
		//location.y = 100 - r2;
		location.angle = theta1;
		//location.angle = theta2;
	 	//}
	}

	while (nMotorRunState[motorA] != runStateIdle)
	{
		wait1Msec(1);
	}
}

task main()
{
	nxtDisplayTextLine(1, "Oliver ready");
	nMotorEncoder[motorA] = 0;
	bool lost = true;
	bool center = false;
	run = true;
	int distance, i;
	int maxhough = 5; //kolikokrat se bo trudu s houghom


	whereAmI(false);

	if (location.maxa >= 90 && location.maxa < 270) {
		//Rotacija v desno
		rotateRight(270 - location.maxa, 100);
	} else {
		//Rotacija v levo
		rotateRight(-mod(90 + location.maxa, 360), 100);
	}

	for (i=0; i < location.maxd; i++) {
		Go_Straight(100);
		if (SensorValue[touchL] || SensorValue[touchR]) {
			wait10Msec(10);
		}
		if (SensorValue[touchL] && SensorValue[touchR]) {
			nxtDisplayTextLine(2, "touchL, touchR");
			stop();
			rotateSonar(-270-nMotorEncoder[motorA]);
			Go_Straight(100);
			do {
				distance = SensorValue[sonar];
				nxtDisplayTextLine(2, "distance: %3d", distance);
				wait10Msec(50);
			} while (distance < SensorValue[sonar]);
			Go_Straight(-100);
			wait10Msec(100);
			stop();
			center = false;
			break;
		} else if (SensorValue[touchL]) {
			nxtDisplayTextLine(2, "touchL");
			Go_Straight(-50);
			wait10Msec(20);
			SharpTurnRight();
			TurnLeft();
		} else if (SensorValue[touchR]) {
			nxtDisplayTextLine(2, "touchR");
			Go_Straight(-50);
			wait10Msec(20);
			SharpTurnLeft();
			TurnRight();
		} else {
			wait10Msec(1);
		}
	}

	stop();
	rotateSonar(-nMotorEncoder[motorA]);
	whereAmI(true);

	if (location.angle >= 90 && location.angle < 270) {
		//Rotacija v desno
		rotateRight(270 - location.angle, 100);
	} else {
		//Rotacija v levo
		rotateRight(-mod(location.angle + 90, 360), 100);
	}

	Go_Straight(100);
	i = 0;
	do {
		wait10Msec(10);
		i++;
	} while (SensorValue[touchL] || SensorValue[touchR]);
	//Go_Straight(-100);
	nxtDisplayTextLine(1, "L: %d, R: %d", SensorValue[touchL], SensorValue[touchR]);
	wait10Msec(500);
	//stop();

	if (i > 900) goto end;

	SharpTurnRight();
	do {
		stop();
		rotateSonar(-nMotorEncoder[motorA]);
		Go_Straight(50);
		for (i=0; i < 100; i++) {
			if (SensorValue[touchL] || SensorValue[touchR]) break;
			distance = SensorValue[sonar];
			wait10Msec(20);
			rotateRight((distance - SensorValue[sonar])*0.45, 100);
			Go_Straight(50);
		}
		if (SensorValue[touchL] && SensorValue[touchR]) {
			nxtDisplayTextLine(2, "touchL, touchR");
			stop();
			rotateSonar(-270);
			Go_Straight(100);
			do {
				distance = SensorValue[sonar];
				nxtDisplayTextLine(2, "distance: %3d", distance);
				wait10Msec(50);
			} while (distance < SensorValue[sonar]);
			Go_Straight(-50);
			wait10Msec(100);
			SharpTurnRight();
			TurnLeft();
		} else if (SensorValue[touchL]) {
			nxtDisplayTextLine(2, "touchL");
			Go_Straight(-50);
			wait10Msec(20);
			SharpTurnRight();
			TurnLeft();
		} else if (SensorValue[touchR]) {
			nxtDisplayTextLine(2, "touchR");
			Go_Straight(-50);
			wait10Msec(20);
			SharpTurnLeft();
			TurnRight();
		}
		SharpTurnLeft();
		rotateSonar(-270 - nMotorEncoder[motorA]);
		i = 0;
		Go_Straight(100);
		do {
			distance = SensorValue[sonar];
			wait10Msec(50);
			i++;
		} while (distance < SensorValue[sonar]);
		if (i >= 15) break;
		Go_Straight(-100);
		wait10Msec(100);
		SharpTurnRight();
	} while (run);

	end:
	nxtDisplayTextLine(2, "Zmaga!");
	rotateRight(1800, 100);
	rotateSonar(-nMotorEncoder[motorA]);
	return;
}
