CREATE ROLE replication_group;
GRANT replication_group TO CURRENT_USER;
GRANT replication_group TO "${debeziumUser}";
GRANT USAGE, CREATE ON SCHEMA public TO replication_group;
ALTER TABLE outbox_events OWNER TO replication_group;
