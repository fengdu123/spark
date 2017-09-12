tar -zxvf phoenix-4.8.0-cdh5.8.0.tar.gz  -C ./
mv phoenix-4.8.0-cdh5.8.0-server.jar /opt/cloudera/parcels/CDH/lib/hbase/lib/
scp 2 all regionserver


python $PHOENIX_HOME/bin/sqlline.py localhost:2181

!tables

create table person(id varchar primary key, name varchar);
upsert into person values ('111','jone');

select * from person;


@see http://www.jianshu.com/p/d862337247b1

test                                                                                                                                                                                                                                        
COLUMN FAMILIES DESCRIPTION                                                                                                                                                                                                                 
{NAME => 'col1', BLOOMFILTER => 'ROW', VERSIONS => '1', IN_MEMORY => 'false', KEEP_DELETED_CELLS => 'FALSE', DATA_BLOCK_ENCODING => 'NONE', TTL => 'FOREVER', COMPRESSION => 'NONE', MIN_VERSIONS => '0', BLOCKCACHE => 'true', BLOCKSIZE =>
 '65536', REPLICATION_SCOPE => '0'}                                                                                                                                                                                                         
{NAME => 'col2', BLOOMFILTER => 'ROW', VERSIONS => '1', IN_MEMORY => 'false', KEEP_DELETED_CELLS => 'FALSE', DATA_BLOCK_ENCODING => 'NONE', TTL => 'FOREVER', COMPRESSION => 'NONE', MIN_VERSIONS => '0', BLOCKCACHE => 'true', BLOCKSIZE =>
 '65536', REPLICATION_SCOPE => '0'}                                                                                                                                                                                                         
{NAME => 'col3', BLOOMFILTER => 'ROW', VERSIONS => '1', IN_MEMORY => 'false', KEEP_DELETED_CELLS => 'FALSE', DATA_BLOCK_ENCODING => 'NONE', TTL => 'FOREVER', COMPRESSION => 'NONE', MIN_VERSIONS => '0', BLOCKCACHE => 'true', BLOCKSIZE =>
 '65536', REPLICATION_SCOPE => '0'}   
 
 
 PERSON, {TABLE_ATTRIBUTES => {coprocessor$1 => '|org.apache.phoenix.coprocessor.ScanRegionObserver|805306366|', coprocessor$2 => '|org.apache.phoenix.coprocessor.UngroupedAggregateRegionObserver|805306366|', coprocessor$3 => '|org.apach
 e.phoenix.coprocessor.GroupedAggregateRegionObserver|805306366|', coprocessor$4 => '|org.apache.phoenix.coprocessor.ServerCachingEndpointImpl|805306366|', coprocessor$5 => '|org.apache.phoenix.hbase.index.Indexer|805306366|org.apache.ha
 doop.hbase.index.codec.class=org.apache.phoenix.index.PhoenixIndexCodec,index.builder=org.apache.phoenix.index.PhoenixIndexBuilder'}                                                                                                        
 COLUMN FAMILIES DESCRIPTION                                                                                                                                                                                                                 
 {NAME => '0', BLOOMFILTER => 'ROW', VERSIONS => '1', IN_MEMORY => 'false', KEEP_DELETED_CELLS => 'FALSE', DATA_BLOCK_ENCODING => 'FAST_DIFF', TTL => 'FOREVER', COMPRESSION => 'NONE', MIN_VERSIONS => '0', BLOCKCACHE => 'true', BLOCKSIZE 
 => '65536', REPLICATION_SCOPE => '0'}   
