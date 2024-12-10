#!/bin/zsh
docker build -t qrgen .;
docker rm -f qrgen;
docker run --name qrgen -p 8083:8080 --network tunnel --restart=always -d qrgen;
