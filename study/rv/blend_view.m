function out=blend_view(i1, i2, f, roll, fov, dist, Xout, Yout)
% BLEND_VIEW Create an intermediate view.
%    out = BLEND_VIEW(i1, i2, f, roll, fov, dist, Xout, Yout) creates an
%        intermediate view from the images with indices i1 and i2 from a
%        point on the line between the viewpoints of the two images, such
%        that the distance between the virtual viewpoint and the viewpoint
%        of the first image is f times the distance between the two actual
%        viewpoints. The parameters are:
%        i1 - index of the first image
%        i2 - index of the second image
%        f - the position of the virtual view on the line between the
%            actual views, default: 0.5
%        roll - image plane rotation in radians, default: weighted average
%               of the calculated roll corrections
%        fov - field of view, default: 2*pi/3
%        dist - distance between the second viewpoint and the image plane
%               in meters, default: 50
%        Xout - output image width in pixels, default: depends on Yout and
%               the field of view (4*Yout/3 for default fovl and fovr)
%        Yout - output image height in pixels, default: 3*Xout/4 if Xout is
%               specified, otherwise 480
%
%        If no output parameter is specified, the resulting image is shown
%        on screen. Otherwise it is returned as output.
    global sName dX dY dZ dHeading dRoll dPitch
    close all;

    %default parameter values
    if nargin < 3
        f = 0.5;
    end
    if nargin < 5 || fov == 0
        fov = 2*pi/3;
    end
    if nargin < 6 || dist == 0
       dist = 50; 
    end
    if nargin < 8 || Yout == 0
        if nargin == 7 && Xout ~= 0
            Yout = 3*Xout/4;
        else
            Yout = 480;
        end
    end
    e = exp(fov);
    t = tan(asin((e-1)/(e+1)));
    
    %3*tan(asin((exp(2*pi/3)-1)/(exp(2*pi/3)+1))) = 3.7481
    if nargin < 7 || Xout == 0
        Xout = floor(4*Yout*t/3.7481);
    end
    
    %calculate the distance and vectors between viewpoints
    origin = (1-f)*[dX(i1); dY(i1); dZ(i1)] + f*[dX(i2); dY(i2); dZ(i2)];
    pos1 = [dX(i1); dY(i1); dZ(i1)];
    [dist1, vec1] = latLongDistance(origin, pos1);
    pos2 = [dX(i2); dY(i2); dZ(i2)];
    [dist2, vec2] = latLongDistance(origin, pos2);
    
    if f == 1
        vec2 = -vec1;
    elseif f == 0
        vec1 = -vec2;
    end
    
    %calculate appropriate pitch and roll
    yaw1 = (dHeading(i1) + atan2(vec1(1), vec1(2))) + pi/2;
    [pitch1 roll1] = getPitchRoll(yaw1, -dPitch(i1), dRoll(i1));
    yaw2 = (dHeading(i2) + atan2(vec2(1), vec2(2))) - pi/2;
    [pitch2 roll2] = getPitchRoll(yaw2, -dPitch(i2), dRoll(i2));
    
    %roll correction
    if nargin < 4 || isnan(roll)
        roll = (f-1)*roll1 - f*roll2;
    end
    
    %pitch correction
    dprojsq = vec1(1)^2 + vec1(2)^2;
    cpitch = acos(sqrt(dprojsq/(dprojsq + vec1(3)^2)));
    if vec1(3) < 0
        cpitch = -cpitch;
    end
    
    vc = dist2 + dist;
    
    %rectify the first image
    ve = vc/cos(fov/2.);
    ae = sqrt(dist1^2 + ve^2 + 2*dist1*ve*cos(fov/2.));
    ac = vc + dist1;
    fov1 = acos(ac/ae);
    [img_out, z] = rectify(['lj/' char(sName(i1))], yaw1, pitch1-cpitch, ...
        roll1+roll, fov1, fov1, 2., Xout, Yout);

    %rectify the second image if needed
    if f > 0
        vd = vc/cos(f*fov/2.);
        bd = sqrt(dist2^2 + vd^2 - 2*dist2*vd*cos(f*fov/2.));
        bc = vc - dist2;
        fov2 = acos(bc/bd);
        e2 = exp(fov2);
        x2 = floor(4*z*bc/ac*tan(asin((e2-1)/(e2+1))));
        fig2 = rectify(['lj/' char(sName(i2))], yaw2, pitch2-cpitch, ...
            roll2+roll, fov2, fov2, 2., x2, Yout);
        
        %show the intermediate result
        figure;
        imshow(imequal(fig2),[]);
        
        %radius of blending function
        dRadius = 0.9;

        %smooth paramaters
        iSize = floor(Yout/5); dSigma = iSize ./ 10;

        %generate a binary mask
        blend_coeffs = zeros(Yout,x2);
        [X,Y] = meshgrid(-x2/2:x2/2-1,-Yout/2:Yout/2-1);
        radii = sqrt(X.^2+Y.^2);
        dRadiusImg = x2 * dRadius / 2;
        blend_coeffs(radii < dRadiusImg) = 1;
        dims = size(fig2);
        blend_coeffs = repmat(blend_coeffs, [1 1 dims(3)]);

        %caclulate a smooth transition using a Gaussian filtering
        h = fspecial('gaussian',[iSize iSize],dSigma);
        smooth_blend_coeffs = imfilter(blend_coeffs,h);
        
        %show the intermediate result
        figure;
        imshow(imequal(img_out),[]);
        
        %blend the two images
        img_out(:,uint32((Xout-x2)/2+1):uint32((Xout+x2)/2),:) = ...
            fig2.*smooth_blend_coeffs + ...
            img_out(:,uint32((Xout-x2)/2+1):uint32((Xout+x2)/2),:) .* ...
            (1-smooth_blend_coeffs);
    end
    
    if nargout > 0
		out = img_out;
    else
        %show the result
        figure;
        img_out = imequal(img_out);
        imshow(img_out,[]);
    end
end