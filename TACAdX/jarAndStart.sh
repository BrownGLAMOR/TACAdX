#!/bin/bash

jar cf bin/brown-adnet.jar bin/brown
mv -f bin/brown-adnet.jar lib
./runAgent.sh


