function out = parallel_views(i1, i2, origin, yaw, roll, fovl, fovr, ...
    d1, d2, Xout, Yout)
% PARALLEL_VIEWS Stitch two parallel views.
%    out = parallel_views(i1, i2, origin, yaw, roll, fovl, fovr, ...
%        d1, d2, Xout, Yout) creates a virtual view from a given point of
%        view by rectifying the images with indices i1 and i2 and stitching
%        them along a vertical line. The parameters are:
%        i1 - index of the first image
%        i2 - index of the second image
%        origin - the position of the virtual viewpoint in the form of
%                 [longitude; latitude; height], with longitude and
%                 latitude according to the WGS84 datum and the height in
%                 meters
%        roll - image plane rotation in radians, default: weighted average
%               of the calculated roll corrections
%        fovl - horizontal field of view to the left of the viewing
%               direction, default: pi/3
%        fovr - horizontal field of view to the right of the viewing
%               direction, default: equal to fovl
%        d1 - distance between the first viewpoint and the image plane in
%             meters, default: 10
%        d2 - distance between the second viewpoint and the image plane in
%             meters, default: equal to d1
%        Xout - output image width in pixels, default: depends on Yout and
%               the field of view (4*Yout/3 for default fovl and fovr)
%        Yout - output image height in pixels, default: 3*Xout/4 if Xout is
%               specified, otherwise 480
%
%        If no output parameter is specified, the resulting image is shown
%        on screen. Otherwise it is returned as output.
    global sName dX dY dZ dHeading dRoll dPitch
    close all;
    
    pos1 = [dX(i1); dY(i1); dZ(i1)];
    pos2 = [dX(i2); dY(i2); dZ(i2)];

    %default parameter values
    if nargin < 3
        origin = (pos1 + pos2)/2;
    end
    if nargin < 4
        yaw = 0;
    end
    if nargin < 6 || isnan(fovl)
        fovl = pi/3;
    end
    if nargin < 7 || isnan(fovr)
        fovr = fovl;
    end
    if nargin < 8 || d1 == 0
       d1 = 10; 
    end
    if nargin < 9 || d2 == 0
       d2 = d1;
    end
    if nargin < 11 || Yout == 0
        if nargin == 10 && Xout ~= 0
            Yout = 3*Xout/4;
        else
            Yout = 480;
        end
    end
    el = exp(2*fovl);
    er = exp(2*fovr);
    tl = tan(asin((el-1)/(el+1)));
    tr = tan(asin((er-1)/(er+1)));
    
    %3*tan(asin((exp(2*pi/3)-1)/(exp(2*pi/3)+1))) = 3.7481
    if nargin < 10 || Xout == 0
        Xout = floor(2*Yout*(tl+tr)/3.7481);
    end
    
    %calculate the distance and vectors between viewpoints
    [dist1, vec1] = latLongDistance(origin, pos1);
    [dist2, vec2] = latLongDistance(origin, pos2);
    
    b1 = atan2(vec1(1), vec1(2)) + yaw;
    if mod(b1, 2*pi) > pi
        vec1 = -vec1;
        dist1 = -dist1;
    end
    b2 = atan2(vec2(1), vec2(2)) + yaw;
    if mod(b2, 2*pi) > pi
        vec2 = -vec2;
        dist2 = -dist2;
    end
    
    %swap the viewpoints if the second viewpoint lies to the left of the
    %first one
    b1 = mod(b1, pi);
    b2 = mod(b2, pi);
    if b1 > b2
        [i1, i2] = swap(i1, i2);
        [dist1, dist2] = swap(dist1, dist2);
        [vec1, vec2] = swap(vec1, vec2);
        [b1, b2] = swap(b1, b2);
    end
    
    b1 = pi/2 - b1;
    b2 = b2 - pi/2;
        
    %calculate appropriate heading, pitch and roll
    yaw1 = dHeading(i1) - yaw;
    [pitch1 roll1] = getPitchRoll(yaw1, -dPitch(i1), dRoll(i1));
    yaw2 = dHeading(i2) - yaw;
    [pitch2 roll2] = getPitchRoll(yaw2, -dPitch(i2), dRoll(i2));
    
    %roll correction
    if nargin < 5 || isnan(roll)
        roll = -(dist2*roll1 + dist1*roll2)/(dist1 + dist2);
    end
    
    %pitch correction
    dprojsq1 = vec1(1)^2 + vec1(2)^2;
    cpitch1 = atan(sqrt(1-dprojsq1/(dist1^2))/cos(b1))*sign(vec1(3));
    dprojsq2 = sqrt(vec2(1)^2 + vec2(2)^2);
    cpitch2 = atan(sqrt(1-dprojsq2/(dist2^2))/cos(b2))*sign(vec2(3));
    cpitch = (cpitch1+cpitch2)/2;
    
    x1 = sqrt(dprojsq1)*cos(b1);
    x2 = sqrt(dprojsq2)*cos(b2);
    dist = (x1+d1+x2+d2)/2;
    
    %calculate the needed points
    A = x1*tan(b1);
    B = x2*tan(b2);
    C = dist*tan(fovl);
    D = dist*tan(fovr);
    y1 = sqrt(d1^2 + (C-A)^2);
    y2 = sqrt(d2^2 + (D-B)^2);
    z1 = sqrt(d1^2 + A^2);
    z2 = sqrt(d2^2 + B^2);
    
    %calculate the fields of view
    phi1 = acos(d1/y1);
    phi2 = acos(d2/y2);
    chi1 = acos(d1/z1);
    chi2 = acos(d2/z2);
    
    %smooth paramaters
    iSize = floor(sqrt((d1+d2)/2)*Xout/90); dSigma = iSize/2;
    if mod(iSize,2) == 1
        iSize = iSize + 1;
    end
    
    %compute the position of the stitching line
    el1 = exp(2*phi1);
    er1 = exp(2*chi1);
    tl1 = tan(asin((el1-1)/(el1+1)));
    tr1 = tan(asin((er1-1)/(er1+1)));
    el2 = exp(2*chi2);
    er2 = exp(2*phi2);
    tl2 = tan(asin((el2-1)/(el2+1)));
    tr2 = tan(asin((er2-1)/(er2+1)));
    t1 = tl1 + tr1;
    t2 = tl2 + tr2;
    Xl = floor(Xout*t1/(t1+t2));
    Xr = Xout - Xl;
    
    %add the blending area to the images
    tr = atan(tr1*(1+iSize/Xl));
    tl = atan(tl2*(1+iSize/Xr));
    chi1 = log((sin(tr)+1)./cos(tr));
    chi2 = log((sin(tl)+1)./cos(tl));
    
    %rectify both images
    fig1 = rectify(['lj/' char(sName(i1))], yaw1, pitch1+cpitch, ...
        roll1+roll, phi1, chi1, 2., Xl+iSize, Yout);
    fig2 = rectify(['lj/' char(sName(i2))], yaw2, pitch2+cpitch, ...
        roll2+roll, chi2, phi2, 2., Xr+iSize, Yout);
    dim1 = size(fig1);
    
    %generate a binary mask
    blend_coeffs = [ones(Yout, iSize) zeros(Yout, iSize)];
    blend_coeffs = repmat(blend_coeffs, [1 1 dim1(3)]);
    
    %caclulate a smooth transition using a Gaussian filtering
    h = fspecial('gaussian',[1 2*iSize],dSigma);
    smooth_blend_coeffs = imfilter(blend_coeffs,h, 'replicate');
    mx = max(smooth_blend_coeffs(:));
    mn = min(smooth_blend_coeffs(:));
    smooth_blend_coeffs = (smooth_blend_coeffs-mn)./(mx-mn);
    
    %blend the two images
    img_out = [fig1(:,1:Xl-iSize,:) ...
        (smooth_blend_coeffs.*fig1(:,Xl-iSize+1:Xl+iSize,:) + ...
        (1-smooth_blend_coeffs).*fig2(:,1:2*iSize,:)) ...
        fig2(:,2*iSize+1:Xr+iSize,:)];
    
    if nargout > 0
		out = img_out;
    else
        %show the intermediate results
        fig1 = imequal([fig1(:,1:Xl-iSize,:) ...
            smooth_blend_coeffs.*fig1(:,Xl-iSize+1:Xl+iSize,:)]);
        figure;
        imshow(fig1,[]);
        fig2 = imequal([(1-smooth_blend_coeffs).*fig2(:,1:2*iSize,:) ...
            fig2(:,2*iSize+1:Xr+iSize,:)]);
        figure;
        imshow(fig2,[]);
        
        %show the result
        img_out = imequal(img_out);
        figure;
        imshow(img_out,[]);
    end
end