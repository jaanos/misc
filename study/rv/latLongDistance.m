function [dist,vec]=latLongDistance(pos1,pos2,earth_radius)
% LATLONGDISTANCE Calculate distance between two points.
%    [dist,vec] = LATLONGDISTANCE(pos1,pos2,earth_radius) converts
%        geographical coordinates according to the WGS84 datum to Cartesian
%        coordinates and calculates the vector and distance between two
%        given points.
% Author: Dušan Omerčević 2008
    if nargin < 3
        earth_radius = 6325314.4; 
    end

    pos1(1:2)=(pos1(1:2)/180)*pi;
    pos2(1:2)=(pos2(1:2)/180)*pi;

    dlong=earth_radius*cos((pos1(2)+pos2(2))/2)*sin(pos2(1)-pos1(1));
    dlat=earth_radius*sin(pos2(2)-pos1(2));
    dalt=pos2(3)-pos1(3);

    dist=sqrt(dlong^2+dlat^2+dalt^2);
    vec=[dlong;dlat;dalt];
end
