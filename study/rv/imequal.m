function E = imequal(x)
% IMEQUAL Scale values of input image x to [0,1].
    x=double(x);
    m_min = min(x(:));
    m_max = max(x(:));
    E = (x-m_min)/(m_max-m_min);
end
