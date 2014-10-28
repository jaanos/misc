function [out, zoom]=rectify(file, yaw, pitch, roll, fovl, fovr, dGamma,...
    Xout, Yout, sky, vp_yaw, vp_pitch)
% RECTIFY Rectify a panoramic image.
%    [out, zoom] = RECTIFY(file, yaw, pitch, roll, fovl, fovr, dGamma,...
%        Xout, Yout, sky, vp_yaw, vp_pitch) rectifies the image given by
%        the filename. The parameters are:
%        file - panoramic image filename
%        yaw - horizontal viewing direction: -pi for the left, pi for the
%              right of the panoramic image, default: 0
%        pitch - vertical viewing direction: -pi/2 for the top, pi/2 for
%                the bottom of the panoramic image, default: 0
%        roll - image plane rotation in radians, default: 0
%        fovl - horizontal field of view to the left of the viewing
%               direction, default: pi/3
%        fovr - horizontal field of view to the right of the viewing
%               direction, default: equal to fovl
%        dGamma - gamma correction, default: 2.0
%        Xout - output image width in pixels, default: depends on Yout and
%               the field of view (4*Yout/3 for default fovl and fovr)
%        Yout - output image height in pixels, default: 3*Xout/4 if Xout is
%               specified, otherwise 480
%        sky - if 1, correct spherical coordinates when they are out of
%              bounds so the sky will be rendered correctly, default: 0
%        vp_yaw - image plane rotation around its vertical axis, default: 0
%        vp_pitch - image plane rotation around its horizontal axis,
%                   default: 0
%
%        If no output parameter is specified, the rectified image is shown
%        on screen. Otherwise it is returned in the first output parameter,
%        while in the second one the zoom factor is returned.
    close all;
    
    tic;
    img = imread(file);
    iDim = size(img);
    
    %replicate color channels for a grayscale image
    if (size(iDim) < 3)
        img = repmat(img, [1 1 3]);
        iDim = size(img);
    end
    
    %default parameter values
    if nargin < 2
        yaw = 0.;
    end
    if nargin < 3
        pitch = 0.;
    end
    if nargin < 4
        roll = 0.;
    end
    if nargin < 5 || isnan(fovl)
        fovl = pi/3;
    end
    if nargin < 6 || isnan(fovr)
        fovr = fovl;
    end
    if nargin < 7
        dGamma = 2.;
    end
    if nargin < 9 || Yout == 0
        if nargin == 7 && Xout ~= 0
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
    if nargin < 8 || Xout == 0
        Xout = floor(2*Yout*(tl+tr)/3.7481);
    end
    if nargin < 10
        sky = 0;
    end
    if nargin < 11
        vp_yaw = 0;
    end
    if nargin < 12
        vp_pitch = 0;
    end
    
    %calculate the zoom factor
    zoom = Xout/(tl+tr);
    Xmax = floor(tr*zoom);
    Xmin = Xmax - Xout + 1;
    
    %apply gamma correction
    if dGamma > 0
        img = uint8(((double(img)./255 ).^(1./dGamma)) * 255);
    end
    
    %prepare the image plane
    [Xpa,Ypa] = meshgrid(1:iDim(2),1:iDim(1));
    [X,Y] = meshgrid(Xmin:Xmax,1-Yout/2:Yout/2);
    Z = zeros(Yout, Xout);
    
    %rotate the image plane
    [X,Y] = rot2d(X, Y, roll);
    ey = exp(pitch);
    Y = Y + zoom*tan(asin((ey-1)/(ey+1)));
    [X,Z] = rot2d(X, Z, vp_yaw);
    [Y,Z] = rot2d(Y, Z, vp_pitch);
    
    %project onto a sphere
    [Xp, Yp] = cart2sph(zoom+Z, X, Y);
    
    %apply the inverse Gudermannian function
    Xp = log((sin(Xp)+1)./cos(Xp));
    Yp = mod(log((sin(Yp)+1)./cos(Yp)) + pi/2, 2*pi);
    if sky
        m = Yp > pi;
        Yp = (1-2*m).*Yp + 2*pi*m;
        Xp = Xp + pi*m;
    end
    Yp = min(mod(Yp*iDim(1)/pi - 1, iDim(1)) + 1, iDim(1));
    Xp = min(mod((Xp+yaw)*iDim(2)/(2*pi) + 0.5*iDim(2) - 1, iDim(2)) + 1, iDim(2));
    
    img_out = zeros(Yout, Xout, iDim(3));
    for iChannel = 1:iDim(3)
        %get the final data by means of image interpolation
        img_out(:,:,iChannel) = interp2(Xpa,Ypa,double(img(:,:,iChannel)),Xp,Yp);
    end
    toc
	
    if nargout > 0
		out = img_out;
    else
        %show the result
        img_out = imequal(img_out);
        figure;
        imshow(img_out,[]);
    end
end