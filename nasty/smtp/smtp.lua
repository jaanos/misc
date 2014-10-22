#!/usr/bin/lua
-- load namespace
local socket = require("socket")

-- config variables
local host = "*"
local port = 25
local maildir = '/mnt/ext/janos/private/mail/'

function send(file, client, str)
    file:write("S: "..str.."\n")
    client:send(str.."\r\n")
end

function smtp(client)
    local errcnt = 0
    local data = false
    
    local ip, port = client:getpeername()
    client:settimeout(0.1)
    
    file = io.open(maildir..ip, "a")
    file:write("\nSession start: "..os.date().."\n\n")

    send(file, client, "220 smtp.unsecured.wifi SMTP Postfucks")
    send(file, client, "214 WARNING: This is a fake server whose sole purpose")
    send(file, client, "214 WARNING: is eavesdropping on unsuspecting clients.")
    send(file, client, "214 WARNING: No mail will be forwarded, all mail")
    send(file, client, "214 WARNING: will be saved. You have been warned.")

    while 1 do
        local line, err = client:receive()
        
        if err then
            errcnt = errcnt+1
            if errcnt > 500 then
                err = 'end'
            end
        else
            errcnt = 0
            file:write("C: "..line.."\n")
            
            if data then
                if line == '.' then
                    data = false
                    send(file, client, "550 I'm now just being nice enough to tell your client that this message will never be delivered to the intended recipient.")
                end
            else
                line = line:gsub("^%s+", "")
                line = line:gsub("%s+$", "")
                line = line:gsub("%s+", " ")
                if line ~= '' then
                    verb = line:match("^(%w+)")
                    
                    if verb ~= nil then
                        verb = verb:upper()
                        line = line:gsub("^%w+ ?", "")
                    end
                    
                    if verb == 'QUIT' then
                        send(file, client, "221 Adios, motherfucker.")
                        err = 'end'
                    elseif verb == 'HELO' or verb == 'EHLO' then
                        if line == '' then
                            send(file, client, "501 Don't you think it's appropriate to introduce yourself?")
                        else
                            send(file, client, "250 Well hello there "..line..". Where are you not sending mail from today?")
                        end
                    elseif verb == 'MAIL' then
                        from = line:match("^[Ff][Rr][Oo][Mm]: ?<([^>]*)>")
                        if from == nil then
                            send(file, client, "501 RFC2821 is your friend. RTFM.")
                        elseif from == '' then
                            send(file, client, "250 No return path? Funky!")
                        else
                            send(file, client, "250 So, "..from..", who are you not sending your mail to?")
                        end
                    elseif verb == 'RCPT' then
                        to = line:match("^[Tt][Oo]: ?<([^>]*)>")
                        if to == nil then
                            send(file, client, "501 RFC2821 is your friend. RTFM.")
                        elseif to == '' then
                            send(file, client, "501 Well, there must be someone you'd like to send this mail to.")
                        else
                            send(file, client, "250 I take it that you realize that "..to.." will never receive this mail?")
                        end
                    elseif verb == 'DATA' then
                        send(file, client, "354 Speak your soul. Nobody will read this anyway... or not?")
                        data = true
                    elseif verb == 'RSET' then
                        send(file, client, "250 Were you under an impression that this was actually needed?")
                    elseif verb == 'NOOP' then
                        send(file, client, "250 I'm not doing anything anyway.")
                    elseif verb == 'VRFY' or verb == 'EXPN' then
                        if line == '' then
                            send(file, client, "501 Who you gonna verify?")
                        else
                            send(file, client, "252 Sure, anything goes.")
                        end
                    elseif verb == 'HELP' then
                        send(file, client, "214 I'm not really trying to be helpful.")
                    elseif verb == 'TURN' or verb == 'SEND' or verb == 'SAML' or verb == 'SOML' then
                        send(file, client, "502 You shouldn't be using this shit anyway.")
                    elseif verb == 'AUTH' or verb == 'STARTTLS' then
                        send(file, client, "502 This is an unsecured network, we provide no security.")
                    else
                        send(file, client, "500 Umm... what?")
                    end
                end
            end
        end
        if coroutine.yield(err) then
            file:write("\nSession end: "..os.date().."\n\n")
            client:close()
            file:close()
            return
        end
        file:flush()
    end
end

-- create a TCP socket and bind it to the host and port
local server = assert(socket.bind(host, port))
server:settimeout(0.1)

-- print a message informing what's up
print("SMTP server listening on " .. host .. ":" .. port)

c = {}

-- loop forever waiting for clients
while 1 do
    for i, t in pairs(c) do
        r, err = coroutine.resume(t, false)
        if err and err ~= 'timeout' then
            coroutine.resume(t, true)
            r = false
        end
        if not r then
            c[i] = nil
        end
    end

    -- wait for a connection from any client
    local client = server:accept()
    
    if client ~= nil then
        cr = coroutine.create(smtp)
        table.insert(c, cr)
        coroutine.resume(cr, client)
    end
end