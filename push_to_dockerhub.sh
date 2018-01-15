#!/bin/bash

USERNAME=febpetroni #TODO: figure out how to handle this
HEAD_SHA1=$(git log origin/master | head -1 | cut -d" " -f2)

docker info
docker build -t $USERNAME/mockkid:$HEAD_SHA1 .
docker tag $USERNAME/mockkid:$HEAD_SHA1 $USERNAME/mockkid:latest
docker push $USERNAME/mockkid:$HEAD_SHA1
docker push $USERNAME/mockkid:latest