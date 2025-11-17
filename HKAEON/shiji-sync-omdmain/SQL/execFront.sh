#!/bin/bash

# 香港UAT和PRD的地址
Port="3308"
User="root"
FrontDbPassword="ULP3Z68ejCbY4pGgwasN"
Databaise="omdmain"
SSHPassword="Vm@host001"

#UAT环境
#IDCDBPassword="Cxzh7qlwyLtdoAFbmMUv"
declare -A frontIPMap=(["002"]="192.4.21.235")

#PRD环境
#declare -A frontIPMap=(["001"]="192.4.25.232" ["002"]="192.4.36.232" ["004"]="192.4.24.232" ["005"]="192.4.32.232" ["006"]="192.4.23.232" ["007"]="192.4.29.232"
#["008"]="192.4.30.232" ["009"]="192.4.31.232" ["010"]="192.4.34.232" ["026"]="192.4.22.232" ["203"]="192.4.39.232" ["210"]="192.4.40.232" )

echo -e "\n【前置机执行的SQL】:\n $1"
for key in ${!frontIPMap[*]}; do
  frontIp=${frontIPMap[$key]}
  result=$(sshpass -p${SSHPassword} ssh -o StrictHostKeyChecking=no root@${frontIp} "mysql -h${frontIp} -u${User} -p${FrontDbPassword} -P${Port} ${Databaise} -Ne \"
$1\"")
  for ((i=0; i<${#result[@]}; i=i+1))
  do
    echo -e "【前置机门店(${key}):${frontIp}】 返回的结果：\n${result}"
  done
done

# 80的前置地址

#Port="3306"
#User="root"
#FrontDbPassword="123456"
#Databaise="omdmain"
#SSHPassword="1q2wazsx"
#
##UAT环境
##IDCDBPassword="Cxzh7qlwyLtdoAFbmMUv"
#declare -A frontIPMap=(["002"]="172.17.13.80")
#
##PRD环境
##declare -A frontIPMap=(["001"]="192.4.25.232" ["002"]="192.4.36.232" ["004"]="192.4.24.232" ["005"]="192.4.32.232" ["006"]="192.4.23.232" ["007"]="192.4.29.232"
##["008"]="192.4.30.232" ["009"]="192.4.31.232" ["010"]="192.4.34.232" ["026"]="192.4.22.232" ["203"]="192.4.39.232" ["210"]="192.4.40.232" )
#
#echo -e "\n【前置机执行的SQL】:\n $1"
#for key in ${!frontIPMap[*]}; do
#  frontIp=${frontIPMap[$key]}
#  result=$(sshpass -p${SSHPassword} ssh -o StrictHostKeyChecking=no ztroot@${frontIp} "source /etc/profile; mysql -h${frontIp} -u${User} -p${FrontDbPassword} -P${Port} ${Databaise} -Ne \"
#$1\"")
#  for ((i=0; i<${#result[@]}; i=i+1))
#  do
#    echo -e "【前置机门店(${key}):${frontIp}】 返回的结果：\n${result}"
#  done
#done