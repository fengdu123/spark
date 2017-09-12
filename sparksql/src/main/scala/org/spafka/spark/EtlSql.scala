package org.spafka.spark

class EtlSql {


//  drop table if exists table ods_tmp_referurl;
//  create table ods_tmp_referurl as
//    SELECT a.*,b.*
//  FROM ods_weblog_origin a LATERAL VIEW parse_url_tuple(regexp_replace(http_referer, "\"", ""), 'HOST', 'PATH','QUERY', 'QUERY:id') b as host, path, query, query_id;
//
//
//
//  insert into table ods_web_log.ods_weblog_detail partition(datestr='2018-08-28')
//  select c.remote_addr,c.remote_user,c.time_local,
//  substring(c.time_local,0,10) as daystr,
//  substring(c.time_local,12) as tmstr,
//  substring(c.time_local,6,2) as month,
//  substring(c.time_local,9,2) as day,
//  substring(c.time_local,11,3) as hour,
//  c.request,c.status,c.body_bytes_sent,c.http_referer,c.ref_host,c.ref_path,c.ref_query,c.ref_query_id,c.http_user_agent
//  from
//  (SELECT
//   a.remote_addr,a.remote_user,a.time_local,
//  a.request,a.status,a.body_bytes_sent,a.http_referer,a.http_user_agent,b.ref_host,b.ref_path,b.ref_query,b.ref_query_id
//  FROM ods_web_log.ods_weblog_origin a LATERAL VIEW parse_url_tuple(regexp_replace(http_referer, "\"", ""), 'HOST', 'PATH','QUERY', 'QUERY:id') b as ref_host, ref_path, ref_query, ref_query_id) c


}
