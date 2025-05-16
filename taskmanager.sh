docker run \
    --rm \
    --name=taskmanager \
    -v "$(pwd)/app/build/libs:/opt/flink/usrlib" \
    --network flink-network \
    --env FLINK_PROPERTIES="jobmanager.rpc.address: jobmanager" \
        flink:1.17.1 taskmanager