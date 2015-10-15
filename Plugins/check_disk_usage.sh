#!/bin/bash

help() {
cat << END
Usage :
        check_disk.sh -p [Path] -w [VALUE] -c [VALUE]

        OPTION          DESCRIPTION
        ----------------------------------
        -h              Help
        -w [VALUE]      Warning Threshold
        -c [VALUE]      Critical Threshold
        -p [Path]       Mount Point Name
        ----------------------------------
Note : [VALUE] must be an integer.
END
}

if [ $# -ne 6 ]
then
        help;
        exit 3;
fi

while getopts "p:w:c:" OPT
do
        case $OPT in
        p) MNT="$OPTARG" ;;
        w) WARN="$OPTARG" ;;
        c) CRIT="$OPTARG" ;;
        *) help ;;
        esac
done

if [ $WARN -ge $CRIT ]
then
        echo "ERROR : Warning threshold must be less than Critical threshold."
        exit 3
fi


# NAGIOS DISPLAY DATA

DF=`df -lh $MNT | grep -iv "filesystem"`
TOTAL=`echo $DF | awk '{print $2}'`
USED=`echo $DF | awk '{print $3}'`
AVAIL=`echo $DF | awk '{print $4}'`
PER=`echo $DF | awk '{print $5}' | tr -d "%"`

OUTPUT=`echo $MNT : Total Space= "$TOTAL", Used Space= "$USED", Available Space= "$AVAIL" i.e. ${PER}% Usage`

if [ $PER -le $WARN ]
then
        STATUS="OK";
        EXITSTAT=0;
elif [ $PER -ge $WARN ]
then
        if [ $PER -ge $CRIT ]
        then
                STATUS="CRITICAL";
                EXITSTAT=2;
        else
                STATUS="WARNING";
                EXITSTAT=1;
        fi
else
        STATUS="UNKNOWN";
        EXITSTAT=3;
fi

echo "$STATUS- $OUTPUT | 'Usage'=${PER};$WARN;$CRIT;0;100" && exit $EXITSTAT