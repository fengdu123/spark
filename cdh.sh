# 启动
[server]
/opt/cm-5.7.5/etc/init.d/cloudera-scm-server start
tail -f /opt/cm-5.7.5/log/cloudera-scm-server/cloudera-scm-server.log
[agent]
/opt/cm-5.7.5/etc/init.d/cloudera-scm-agent start