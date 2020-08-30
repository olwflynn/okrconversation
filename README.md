# okrconversation

1. Make sure you have docker and docker-compose downloaded
2. Open terminal inside the application directory and run command "docker-compose up --build" to build the application and
    start the elasticsearch instance
3. Check elasticsearch instance is running in a browser using "http://localhost:9200/_cat/health?v&pretty"
4. If successful, in another terminal window, run command "docker exec -it okrconversation_app_1 /bin/bash" to bash into the
    container
5. Type command "java -jar target/okrconvmaven-1.0-SNAPSHOT.jar" to start the application
6. Type "-n {name of contributor}" to a new contributor for your first conversation


Keywords/Instructions
* -n {name of contributor} === add new contributor
* -c {name of contributor} === add new conversation using contributor name
* -cid {id of contributor} === add new conversation using contributor id
* -gconv {id of conversation} === get conversation using conversation id