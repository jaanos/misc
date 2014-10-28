function [pitch roll] = getPitchRoll(phi, psi, rho)
% GETPITCHROLL Calculate the appropriate pitch and roll.
%    [pitch roll] = GETPITCHROLL(phi, psi, rho) calculates the appropriate
%        pitch and roll for a given heading and the pitch and roll when the
%        heading is zero. The parameters are:
%        phi - the heading for which pitch and roll are to be calculated
%        psi - pitch when heading is zero
%        rho - roll when heading is zero
    phi = mod(phi, 2*pi);
    psi = mod(psi + pi, 2*pi) - pi;
    rho = mod(rho + pi, 2*pi) - pi;
    roll = acos(cos(rho)*cos(phi)^2 - sin(psi)*sin(rho)*sin(phi)*cos(phi) ...
        + cos(psi)*sin(phi)^2);
    pitch = acos(cos(psi)*cos(phi)^2 + sin(psi)*sin(rho)*sin(phi)*cos(phi) ...
        + cos(rho)*sin(phi)^2);
    
    if rho ~= 0
        roll = sign(rho)*roll;
    else
        roll = -sign(psi)*roll;
    end
    if psi ~= 0
        pitch = sign(psi)*pitch;
    else
        pitch = -sign(rho)*pitch;
    end
    
    d = mod(atan((sin(psi)*sin(rho))/(cos(psi)-cos(rho)))/2, pi);
    if cos(rho)*cos(d)^2 - sin(psi)*sin(rho)*sin(d)*cos(d) + ...
            cos(psi)*sin(d)^2 > cos(psi)*cos(d)^2 + ...
            sin(psi)*sin(rho)*sin(d)*cos(d) + cos(rho)*sin(d)^2
        droll = d;
        dpitch = mod(d + pi/2, pi);
    else
        droll = mod(d + pi/2, pi);
        dpitch = d;
    end
    
    if phi > droll && phi < droll + pi
       roll = -roll; 
    end
    if phi > dpitch && phi < dpitch + pi
       pitch = -pitch; 
    end
end