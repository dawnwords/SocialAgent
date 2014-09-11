#!/bin/bash
cd ./bundles
for filename in `ls`
do
 echo "dxing $filename"
 dx --dex --output=classes.dex $filename
 aapt add $filename classes.dex
 rm classes.dex
done
