#!/usr/bin/env sh

cd docker
docker build --force-rm --no-cache -t kennedyoliveira/hystrix-dashboard:dev .