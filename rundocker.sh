#!/bin/zsh
docker build -t qrgen .;
docker run --name qrgen -p 8085:8080 --restart=always -d qrgen;
