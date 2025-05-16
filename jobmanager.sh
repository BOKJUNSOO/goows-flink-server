docker run \
    --rm \
    --name=jobmanager \
    -v "$(pwd)/app/build/libs:/opt/flink/usrlib" \
    --network flink-network \
    --publish 8081:8081 \
    --env FLINK_PROPERTIES="jobmanager.rpc.address: jobmanager" \
    flink:1.17.1 jobmanager