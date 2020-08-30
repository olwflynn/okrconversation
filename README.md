# okrconversation

OKRs are a great framework for goal setting but we need to combine this with communication and teamwork to be able to execute on them.
In an office setting how do we encourage this? Through conversations. It’s important to have continuous conversations between leaders and contributors, solicit feedback and give recognition. This drives alignment, productivity, trust and ultimately helps achieve business goals.

It seems simple but it rarely are conversations effective. We have focussed on conversations between leaders and contributors in the work environment. Pain points include: Assuming other people have the same information as you Leaders and contributors are busy and so don’t prepare for conversations making them suboptimal Leaders and contributors don’t know what subjects to talk about or what they have already covered recently

There is much that could be written and already has been written on the subject. Instead we wanted to play around with some code and build a MVP as a solution to some of the questions below:

How might we make these conversations easier? How might we improve the productivity of conversations? How might we track or monitor the effectiveness of these conversations?

There is much more that could be done but wanted to get some early feedback on it to figure which direction people think it would be useful to go in.

The current product is a text-based program which guides leaders/managers through their 1on1 conversations with their contributors and allows them to easily reference past conversations and categorise new ones into the key conversation types.

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
