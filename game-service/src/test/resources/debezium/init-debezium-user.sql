CREATE USER debezium_test WITH REPLICATION LOGIN PASSWORD 'debezium_test';
GRANT CONNECT ON DATABASE gamedb_test TO debezium_test;
GRANT CREATE ON DATABASE gamedb_test TO debezium_test;
