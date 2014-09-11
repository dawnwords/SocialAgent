#!/bin/bash
cd ./bundles
for fl in sa.service.gateway.jar sa.service.resolution.jar sa.service.worker.jar sa.service.resolution.local.jar sa.service.manager.jar sa.social.circle.jade.jar sa.service.rsi.dispatcher.jar sa.service.example.hello.jar
do
 echo "pushing $fl"
 adb push $fl /data/data/edu.fudan.sa.view/files/felix/newbundle
 sleep 2s
done
