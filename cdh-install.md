## 1. 基本设置
vi /etc/sysconfig/network
vi /etc/sysconfig/selinux 
service iptables stop
chkconfig iptables off
## 2. 安装jdk 不要 /usr/local
mkdir -p /usr/java
tar -zxvf jdk-8u144-linux-x64.tar.gz  -C /usr/java/
echo 'export JAVA_HOME=/usr/java/jdk1.8.0_144' >> /etc/profile
echo 'export JRE_HOME=${JAVA_HOME}/jre' >> /etc/profile
echo 'export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib' >> /etc/profile
echo 'export PATH=${JAVA_HOME}/bin:$PATH' >> /etc/profile

source /etc/pprofile

## 3. 每台机器都要 的jar
mv mysql-connector-java.jar  /usr/share/java

## 安装mysql 
yum install -y mysql
yum install -y mysql-server

mysql -u root -p

 
mysql> GRANT ALL PRIVILEGES ON *.* TO root@"%" IDENTIFIED BY "root";
mysql> flush privileges;

create database hive DEFAULT CHARSET latin1; 
create database amon DEFAULT CHARSET utf8;
create database hue DEFAULT CHARSET utf8;
create database oozie default charset utf8;


## 免密登入
ssh 
echo '192.168.132.1 spafka' >> /etc/hosts 
echo '192.168.132.3  cdh01.spafka.com' >> /etc/hosts
echo '192.168.132.4  cdh02.spafka.com' >> /etc/hosts
echo '192.168.132.5  cdh03.spafka.com' >> /etc/hosts

ssh-keygen -t rsa 
ssh-copy-id cdh01.spafka.com


## ntp服务

[server]
ntpdate 0.centos.pool.ntp.org 首先与外部同步

ntpd 

vi /etc/ntp.conf
restrict default ignore   //默认不允许修改或者查询ntp,并且不接收特殊封包
restrict 127.0.0.1        //给于本机所有权限
restrict 192.168.132.0 mask 255.255.255.0 notrap nomodify  //给于局域网机的机器有同步时间的权限
server 127.127.1.0     # local clock
driftfile /var/lib/ntp/drift
fudge   127.127.1.0 stratum 10


chkconfig ntpd on

[client]
crontab -e 
1 * * * * root /usr/sbin/ntpdate cdh01.spafka.com >> /root/ntpdate.log 2>&1

## 安装CM
useradd --system --home=/opt/cm-5.7.5/run/cloudera-scm-server --no-create-home --shell=/bin/false --comment "Cloudera SCM User" cloudera-scm
usermod -a -G root cloudera-scm
echo USER=\"cloudera-scm\" >> /etc/default/cloudera-scm-agent
echo "Defaults secure_path = /sbin:/bin:/usr/sbin:/usr/bin" >> /etc/sudoers

tar -zxvf cloudera-manager-el6-cm5.7.5_x86_64.tar.gz -C /opt/


# 将Parcel相关的三个文件拷贝到/opt/cloudera/parcel-repo
cp CDH-5.7.5-1.cdh5.7.5.p0.45-el6.parcel /opt/cloudera/parcel-repo/
cp CDH-5.7.5-1.cdh5.7.5.p0.45-el6.parcel.sha1 /opt/cloudera/parcel-repo/
cp manifest.json /opt/cloudera/parcel-repo/
# 改名cd /ho  
mv /opt/cloudera/parcel-repo/CDH-5.7.5-1.cdh5.7.5.p0.45-el6.parcel.sha1 /opt/cloudera/parcel-repo/CDH-5.7.5-1.cdh5.7.5.p0.45-el6.parcel.sha
# 修改属主
chown -R cloudera-scm:cloudera-scm /opt/cloudera/
chown -R cloudera-scm:cloudera-scm /opt/cm-5.7.5/


 

vi /opt/cm-5.7.5/etc/cloudera-scm-agent/config.ini 

scp -r /opt/cm-5.7.5/ cdh03.spafka.com:/opt/ 
scp -r /opt/cm-5.7.5/ cdh02.spafka.com:/opt/ 

#每台机器 
chown -R cloudera-scm:cloudera-scm /opt/cloudera/
chown -R cloudera-scm:cloudera-scm /opt/cm-5.7.5/




#[server]
/opt/cm-5.7.5/share/cmf/schema/scm_prepare_database.sh mysql cm -h192.168.132.1 -uroot -p --scm-host 192.168.132.1 root root root

mkdir /var/lib/cloudera-scm-server
chown -R cloudera-scm.cloudera-scm /var/lib/cloudera-scm-server

/opt/cm-5.7.5/etc/init.d/cloudera-scm-server start
tail -f /opt/cm-5.7.5/log/cloudera-scm-server/cloudera-scm-server.log
# [agent]
mkdir /opt/cm-5.7.5/run/cloudera-scm-agent
chown cloudera-scm:cloudera-scm /opt/cm-5.7.5/run/cloudera-scm-agent
/opt/cm-5.7.5/etc/init.d/cloudera-scm-agent start


# 安装失败回滚
1> 删除Agent节点的UUID 
      # rm -rf /opt/cm-5.7.5/lib/cloudera-scm-agent/*
2>  清空主节点CM数据库
      进入主节点的MySQL数据库，然后drop database cm;
3> 删除Agent节点namenode和datanode节点信息
     # rm -rf /opt/dfs/nn/*
     # rm -rf /opt/dfs/dn/*
4> 在主节点上重新初始化CM数据库
     # /opt/cm-5.7.5/share/cmf/schema/scm_prepare_database.sh mysql cm -hlocalhost -uroot -p --scm-host localhost scm scm scm
5> 执行启动脚本
     主节点：# /opt/cm-5.7.5/etc/init.d/cloudera-scm-server start
     Agent节点：# /opt/cm-5.7.5/etc/init.d/cloudera-scm-agent start

# 启动
[server]
/opt/cm-5.7.5/etc/init.d/cloudera-scm-server start
tail -f /opt/cm-5.7.5/log/cloudera-scm-server/cloudera-scm-server.log
[agent]
/opt/cm-5.7.5/etc/init.d/cloudera-scm-agent start


# 测试MR 
# 添加用户和组
groupadd spafka
useradd -g spafka spafka
lid spafka

# hadoop创建相应的用户
sudo -u hdfs hdfs dfs -mkdir /user/spafka
sudo -u hdfs hdfs dfs -chown spafka:spafka /user/spafka

# 运行wordcount程序
su - spafka
echo "Hello world Bye world" > file0
echo "Hello hadoop Goodbye hadoop" > file1
hdfs dfs -mkdir -p /user/spafka/wordcount/input
hdfs dfs -put file* /user/spafka/wordcount/input
sudo -u hadoop jar /opt/cloudera/parcels/CDH/jars/hadoop-examples.jar wordcount wordcount/input wordcount/output
hdfs dfs -getmerge wordcount/output output.txt
cat output.txt
sudo -u hdfs spark-submit --class org.apache.spark.examples.SparkPi --master yarn --deploy-mode cluster SPARK_HOME/examples/lib/spark-examples.jar 10

#测试spark

sudo -u hdfs spark-submit --class org.apache.spark.examples.SparkPi --master yarn --deploy-mode cluster /tmp/spark-1.0-SNAPSHOT.jar 10

#安装spark2 @see http://blog.csdn.net/u010936936/article/details/73650417

#升级spark2
#要拷贝配置文件，否则报错误

#拷贝spark2官方的jar(apache) 到 

$SPARK_HOME=/opt/cloudera/parcels/SPARK2-2.2.0.cloudera1-1.cdh5.12.0.p0.142354/下

#设置yarn_conf_dir
vi $SPARK_HOME/conf/spark-env.sh => /etc/spark2/conf/spark-env.sh
export YARN_CONF_DIR=/etc/hadoop/conf.cloudera.yarn

sudo -u hdfs spark2-submit --class org.apache.spark.examples.SparkPi --master yarn --deploy-mode cluster /opt/cloudera/parcels/SPARK2-2.2.0.cloudera1-1.cdh5.12.0.p0.142354/lib/spark2/examples/jars 10

#saprksql && hive
[添加hive的配置文件]

vi /etc/spark2/spark-env.sh
export HADOOP_CONF=
export YARN_CONF_DIR=/etc/hadoop/conf.cloudera.yarn
export HADOOP_CONF_DIR=$HADOOP_CONF_DIR:/etc/hive/conf


[拷贝apache 下sbin/start-thriftserver.sh,bin/spark-sql 至SPARK_HOME/sbin:bin下]
sh start-thriftserver
sh spark-sql
即可 通过thfirt 连接hive


