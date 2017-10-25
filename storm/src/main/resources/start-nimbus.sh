#!/usr/bin/env bash
cd /root/apache-storm-0.9.7
cd bin

nohup ./storm nimbus &
nohup ./storm ui &
nohup /root/apache-storm-0.9.7/bin/storm supervisor &
