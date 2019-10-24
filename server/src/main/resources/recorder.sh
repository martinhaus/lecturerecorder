#!/bin/bash

CAMERA_IP=$1
AUDIO_SOURCE=$2
OUTPUT_FILE=$3
DURATION=$4

echo $CAMERA_IP
echo $AUDIO_SOURCE
echo $OUTPUT_FILE
echo $DURATION

COMMAND="arecord -f cd -r 48000 -D "$AUDIO_SOURCE" | ffmpeg -thread_queue_size 1024  -i - -thread_queue_size 1024 -i rtsp://"$CAMERA_IP" -t $DURATION -c:v copy -c:a aac -strict -2 "$OUTPUT_FILE""
echo $COMMAND
eval $COMMAND
//exit 0