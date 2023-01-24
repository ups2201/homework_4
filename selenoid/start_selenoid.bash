
docker pull selenoid/chrome:107.0
docker pull selenoid/chrome:108.0
docker pull selenoid/opera:91.0
docker pull selenoid/opera:90.0

docker network create selenoid_1
docker network create selenoid_2

docker run -d --name selenoid_1 -p 4445:4444 --net=selenoid_1 -v /var/run/docker.sock:/var/run/docker.sock -v /home/ups/homework/selenoid:/etc/selenoid:ro aerokube/selenoid -limit=12 -capture-driver-logs -max-timeout=0h30m0s -container-network=selenoid_1
docker run -d --name selenoid_2 -p 4446:4444 --net=selenoid_2 -v /var/run/docker.sock:/var/run/docker.sock -v /home/ups/homework/selenoid:/etc/selenoid:ro aerokube/selenoid -limit=12 -capture-driver-logs -max-timeout=0h30m0s -container-network=selenoid_2

docker run -d --name ggr -v /home/ups/homework/selenoid/ggr/grid-router:/etc/grid-router:ro --net host aerokube/ggr:latest-release
docker run -d --name ggr-ui -p 8888:8888 -v /home/ups/homework/selenoid/ggr/grid-router/quota:/etc/grid-router/quota:ro --net host aerokube/ggr-ui:latest-release
docker run -d --name selenoid-ui --net host -p 8080:8080 aerokube/selenoid-ui --selenoid-uri=http://127.0.0.1:8888
