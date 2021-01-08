#!/bin/bash
#coding:utf-8
#定时分隔清理nohup日志
 
MY_DATE=`date -d -1day +%Y%m%d`
#日志存放路径
MY_PATH=/data/springboot
#实时日志
MY_SOURCE="$MY_PATH/nohup.out"
#历史日志
MY_TARGET="$MY_PATH/nohups/$MY_DATE.out"
#拷贝
cp $MY_SOURCE $MY_TARGET
#清空nohup.out
cat /dev/null > $MY_SOURCE
#清理旧文件
find "$MY_PATH/nohups" -mtime +30 -name "*.out" -exec rm -rf {} \;