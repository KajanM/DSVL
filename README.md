[![Total alerts](https://img.shields.io/lgtm/alerts/g/KajanM/DSVL.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/KajanM/DSVL/alerts/)
[![Language grade: JavaScript](https://img.shields.io/lgtm/grade/javascript/g/KajanM/DSVL.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/KajanM/DSVL/context:javascript)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/KajanM/DSVL.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/KajanM/DSVL/context:java)


## To build and run the application

Instructions to build with an IDE is described [here](CONTRIBUTING.md).

1. Ensure you have `Java 8` installed
1. Open terminal or command line and navigate to the `flood` directory
1. Run `./gradlew bootJar` (it is not necessary to have [Gradle](https://gradle.org/) installed in your system)
1. You will get an output similar to below
    ```$terminal
    Starting a Gradle Daemon (subsequent builds will be faster)
    
    BUILD SUCCESSFUL in 6s
    3 actionable tasks: 3 up-to-date
    ```
1. Run `java -jar build/libs/flood-0.0.1-SNAPSHOT.jar --node.port=44444 --server.port=8080`
1. If the node starts correctly you will see log messages like below
    ```terminal
    2018-10-19 16:29:11.787  INFO 15771 --- [           main] com.dsvl.flood.FloodApplication          : Started FloodApplication in 3.403 seconds (JVM running for 3.948)
    2018-10-19 16:29:11.789  INFO 15771 --- [           main] com.dsvl.flood.Registrant                : Attempting to register with the bootstrap server
    2018-10-19 16:29:11.794  INFO 15771 --- [           main] com.dsvl.flood.UdpHelper                 : Sent UDP message to 127.0.0.1:55555 0029 REG 127.0.0.1 44445 dsvl
    2018-10-19 16:29:11.796  INFO 15771 --- [           main] com.dsvl.flood.UdpHelper                 : Received UDP message from 127.0.0.1:55555 0044 REGOK 2 127.0.0.1 45555 127.0.0.1 44444
    2018-10-19 16:29:11.796  INFO 15771 --- [           main] c.d.f.service.impl.RegisterServiceImpl   : Successfully registered with the bootstrap server
    ```
1. If you want to start multiple node instance, run the same command with different `node.port` and `server.port` values


 
