CQL="CREATE KEYSPACE IF NOT EXISTS skellig WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : '1'};

CREATE TABLE IF NOT EXISTS skellig.EventChangedLog
(
    time   timestamp,
    before text,
    after  text,
    PRIMARY KEY (time)
);"

until echo $CQL | cqlsh; do
  echo "cqlsh: Cassandra is unavailable to initialize - will retry later"
  sleep 2
done &

exec /docker-entrypoint.sh "$@"