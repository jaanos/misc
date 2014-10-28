function [Xout Yout] = rot2d(X, Y, phi)
% ROT2D Rotate the image plane by the angle phi.
    Xout = Y*sin(phi) + X*cos(phi);
    Yout = Y*cos(phi) - X*sin(phi);
end