#!/bin/sh
set -e

CONNECT_URL="${CONNECT_URL:-http://kafka-connect:8083}"
CONNECTOR_NAME="${CONNECTOR_NAME:-postgres-connector}"

echo "Waiting for Kafka Connect at ${CONNECT_URL}..."
until curl -s "${CONNECT_URL}/connectors" > /dev/null; do
    sleep 2
done

echo "Registering connector '${CONNECTOR_NAME}' ..."
curl -s -X PUT \
    -H "Content-Type: application/json" \
    --data @/connector-config.json \
    "${CONNECT_URL}/connectors/${CONNECTOR_NAME}/config"

sleep 2
echo "Connector status:"
curl -s "${CONNECT_URL}/connectors/${CONNECTOR_NAME}/status"