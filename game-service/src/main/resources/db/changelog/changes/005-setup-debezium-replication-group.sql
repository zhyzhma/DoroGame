DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'replication_group') THEN
        CREATE ROLE replication_group;
    END IF;
END
$$;
GRANT replication_group TO CURRENT_USER;
GRANT replication_group TO "${debeziumUser}";
GRANT USAGE, CREATE ON SCHEMA public TO replication_group;
ALTER TABLE outbox_events OWNER TO replication_group;
