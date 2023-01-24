git clone git@github.com:ups2201/homework_4.git
cd homework_4
currentPath=$PWD

docker run --rm -7777:8080 --name wiremock -v $currentPath/wiremock/mappings:/home/wiremock/mappings wiremock/wiremock:2.35.0 --verbose