function out=panorama(X, Y, Z, heading, list)
    global sName dX dY dZ dHeading dRoll dPitch
    close all;
    
    origin = [X; Y; Z];
    width = 0;
    for i=list
        pos = [dX(i); dY(i); dZ(i)];
        [dist, vec] = latLongDistance(origin, pos);
        img = rectify(['lj/' char(sName(i))], (dHeading(i) + atan2(vec(1), vec(2)))/(2*pi) + 0.25, 0.5, 50+100*dist/3, 2., 320, 480);
        iDim = size(img);
        img_out(:, width+1:width+iDim(2), :) = img;
        width = width + iDim(2);
    end
    
    if nargout > 0
		out = img_out;
    else
        % and show the result
        figure;
        imshow(imequal(img_out),[]);
    end
end