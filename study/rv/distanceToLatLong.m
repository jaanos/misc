function pos=distanceToLatLong(origin,offset,earth_radius)
% Author: Dušan Omerčević 2008

    if nargin < 3
        earth_radius = 6325314.4; 
    end
   
    origin(1:2)=(origin(1:2)/180)*pi;
    pos=zeros(3,1);

    pos(3)=origin(3)+offset(3);
    pos(2)=origin(2)+asin(offset(2)/earth_radius);
    pos(1)=origin(1)+asin((offset(1)/earth_radius)/cos((origin(2)+pos(2))/2));

    pos(1:2)=(pos(1:2)/pi)*180;
end
