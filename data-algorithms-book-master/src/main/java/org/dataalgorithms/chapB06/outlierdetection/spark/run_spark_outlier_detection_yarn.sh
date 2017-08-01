# define the installation dir for hadoop
export HADOOP_HOME=/Users/mparsian/zmp/zs/hadoop-2.5.0
export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
export HADOOP_HOME_WARN_SUPPRESS=true

export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.7.0_60.jdk/Contents/Home
export BOOK_HOME=/Users/mparsian/zmp/github/data-algorithms-book
export SPARK_HOME=/Users/mparsian/spark-1.5.0
#export SPARK_MASTER=spark://localhost:7077
export SPARK_JAR=$BOOK_HOME/lib/spark-assembly-1.5.0-hadoop2.6.0.jar
export APP_JAR=$BOOK_HOME/dist/data_algorithms_book.jar
# defines some environment for hadoop
source $HADOOP_CONF_DIR/hadoop-env.sh
#
# build all other dependent jars in OTHER_JARS
JARS=`find $BOOK_HOME/lib -name '*.jar'  ! -name '*spark-assembly-1.5.0-hadoop2.6.0.jar' `
OTHER_JARS=""
for J in $JARS ; do 
   OTHER_JARS=$J,$OTHER_JARS
done
#

# define input for Hadoop/HDFS
K=10
INPUT=/outlierdetection/input
#
driver=org.dataalgorithms.chapB06.outlierdetection.spark.OutlierDetection
$SPARK_HOME/bin/spark-submit --class $driver \
	--master yarn-cluster \
	--jars $OTHER_JARS \
    --conf "spark.yarn.jar=$SPARK_JAR" \
	$APP_JAR $K $INPUT
