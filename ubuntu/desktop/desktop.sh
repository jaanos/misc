#!/bin/bash

# resolucija laptopa
LAPW=1440
LAPH=900

# outputi
LVDS=LVDS1
VGA=VGA1

# default vrednosti
DEBUG=""
STARTUP=0
FORCE=0

while true; do
    case "$1" in
        debug)
            # debug mode
            DEBUG="echo";;
        startup)
            # startup mode
            STARTUP=1;;
        force)
            # force mode
            FORCE=1;;
        *)
            break;;
    esac
    shift
done        

# na zacetku pocakaj 3 sekunde
if [ "$STARTUP" -eq 1 ]; then
    eval "$DEBUG sleep 5"
fi

# podatki o trenutnih nastavitvah
XRANDR=`xrandr`
CONN=`echo "$XRANDR" | egrep "\+| connected"`
CURW=`echo "$XRANDR" | grep "current" | cut -d " " -f 8`
CURH=`echo "$XRANDR" | grep "current" | cut -d " " -f 10 | tr -d ,`

# autodetect
if [ -z "$1" ]; then
    if echo "$CONN" | grep "1920x1200" > /dev/null; then
        PRESET=24
    elif echo "$CONN" | grep "1680x1050" > /dev/null; then
        PRESET=22
    elif echo "$CONN" | grep "1280x1024" > /dev/null; then
        PRESET=dual
    else
        PRESET=laptop
    fi
else
    PRESET=$1
fi

# nastavitve za presete
case "$PRESET" in
    24)
        WIDTH=1920
        HEIGHT=1200
        OUTPUT=${VGA}
        PIC=desktop.bmp
    ;;
    22)
        WIDTH=1680
        HEIGHT=1050
        OUTPUT=${VGA}
        PIC=desktop.bmp
    ;;
    dual)
        WIDTH=1280
        HEIGHT=1024
        OUTPUT=${VGA}
        PIC=desktop-dual.bmp
    ;;
    proj)
        WIDTH=1024
        HEIGHT=768
        OUTPUT=${VGA}
        PIC=desktop-dual.bmp
    ;;
    *)
        WIDTH=$LAPW
        HEIGHT=$LAPH
        OUTPUT=${LVDS}
        OFF1=${VGA}
        PIC=desktop.bmp
    ;;
esac

typeset -i TLOFF
WOFF=0
if [ "$OUTPUT" = "${LVDS}" ]; then
    # single screen
    TOTW=$WIDTH
    TOTH=$HEIGHT
    LOFF=0
    # nastavi single screen
    eval "$DEBUG xrandr --output ${LVDS} --primary --auto --panning ${LAPW}x${LAPH}+${LOFF}+0 --output ${OFF1} --off --fb ${WIDTH}x${HEIGHT}"
    # prestavi panele
    #eval "$DEBUG gconftool-2 --type int --set /apps/panel/toplevels/top_panel_screen0/monitor 0"
    #eval "$DEBUG gconftool-2 --type int --set /apps/panel/toplevels/panel_0/monitor 0"
else
    # dual screen
    TOTH=$(($HEIGHT+$LAPH))
    LOFF=$((${WIDTH}-${LAPW}))
    if [ -n "$2" ]; then
        TLOFF=$2
        if [ ${TLOFF} -ge 0 -a ${TLOFF} -le ${LOFF} ]; then
            LOFF=${TLOFF}
        fi
    fi
    if [ ${LOFF} -le 0 ]; then
        WOFF=$((-${LOFF}))
        LOFF=0
    fi
    TOTW=$((${WIDTH}+${WOFF}))
    # nastavi dual screen
    eval "$DEBUG xrandr --output ${LVDS} --auto --panning ${LAPW}x${LAPH}+${LOFF}+${HEIGHT} --output ${OUTPUT} --primary --mode ${WIDTH}x${HEIGHT} --panning ${WIDTH}x${HEIGHT}+0+0 --fb ${TOTW}x${TOTH}"
    # prestavi panele
    #eval "$DEBUG gconftool-2 --type int --set /apps/panel/toplevels/top_panel_screen0/monitor 1"
    #if [ ${WIDTH} -lt ${LAPW} ]; then
    #    eval "$DEBUG gconftool-2 --type int --set /apps/panel/toplevels/panel_0/monitor 0"
    #else
    #    eval "$DEBUG gconftool-2 --type int --set /apps/panel/toplevels/panel_0/monitor 1"
    #fi
fi

# prestavi spodnji panel
#eval "$DEBUG gconftool-2 --type int --set /apps/panel/toplevels/bottom_panel_screen0/monitor 0"

#if [ "$FORCE" -eq 1 -o "$TOTW" -ne "$CURW" -a "$TOTH" -ne "$CURH" ]; then
    # restartaj compiz in gnome-panel, ce je potrebno
    #eval "$DEBUG eval \"(compiz --replace &) > /dev/null 2> /dev/null\""
    #eval "$DEBUG pkill -9 gnome-panel"
    #eval "$DEBUG eval \"(gnome-panel &) > /dev/null 2> /dev/null\""
#fi

# ponastavi prosojnost panelov
#eval "$DEBUG gconftool-2 --type int --set /apps/panel/toplevels/bottom_panel_screen0/background/opacity 32767"
#eval "$DEBUG gconftool-2 --type int --set /apps/panel/toplevels/bottom_panel_screen0/background/opacity 32768"
#eval "$DEBUG gconftool-2 --type int --set /apps/panel/toplevels/top_panel_screen0/background/opacity 32767"
#eval "$DEBUG gconftool-2 --type int --set /apps/panel/toplevels/top_panel_screen0/background/opacity 32768"
#eval "$DEBUG gconftool-2 --type int --set /apps/panel/toplevels/panel_0/background/opacity 32767"
#eval "$DEBUG gconftool-2 --type int --set /apps/panel/toplevels/panel_0/background/opacity 32768"

# nastavi sliko za ozadje
#eval "$DEBUG gconftool-2 --type string --set /desktop/gnome/background/picture_filename /home/${USER}/Slike/${PIC}"

# ob zagonu startaj terminal
if [ "$STARTUP" -eq 1 ]; then
    eval "$DEBUG gnome-terminal --geometry=80x24+0+$(($TOTH-$LAPH)) &"
    eval "$DEBUG sleep 10"
    if [ "$OUTPUT" != "${LVDS}" ]; then
        eval "$DEBUG wmctrl -r \"${USER}@${HOSTNAME}: ~\" -b add,maximized_vert,maximized_horz"
    fi
    eval "$DEBUG wmctrl -r \"${USER}@${HOSTNAME}: ~\" -b add,sticky"
fi
