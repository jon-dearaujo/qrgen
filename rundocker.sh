#!/bin/zsh
docker build -t qrgen .;
docker rm -f qrgen;
docker run --name qrgen -p 5002:8080 --restart=always -d qrgen;
