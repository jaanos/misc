typeset -i N
N=$1
if  [ ${#DIR_HIST[@]} -eq 0 ]; then
    echo "No history yet..."
elif [ "$N" -lt 1 -o "$N" -gt ${#DIR_HIST[@]} ]; then
    echo "Usage: goto <1-${#DIR_HIST[@]}>"
else
    push
    DIR_POS=$1
    \cd "${DIR_HIST[$DIR_POS]}"
fi
