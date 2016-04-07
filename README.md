# Standalone Hystrix Dashboard

[![Build Status](https://travis-ci.org/kennedyoliveira/standalone-hystrix-dashboard.svg?branch=master)](https://travis-ci.org/kennedyoliveira/standalone-hystrix-dashboard)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/ee8e2b298bf24109b642cde4a4df8635)](https://www.codacy.com/app/kennedy-oliveira/standalone-hystrix-dashboard)
[![Dependency Status](https://www.versioneye.com/user/projects/56fcad23905db1003b29956c/badge.svg?style=flat)](https://www.versioneye.com/user/projects/56fcad23905db1003b29956c)
[![Download](https://api.bintray.com/packages/kennedyoliveira/maven/standalone-hystrix-dashboard/images/download.svg) ](https://bintray.com/kennedyoliveira/maven/standalone-hystrix-dashboard/_latestVersion)

Standalone *hystrix dashboard* that can be started using a single fatJar and is very lightweight and fast!

## Features
 - Extremely fast startup (About half a sec)
 - Extremely lightweight, the fat jar is around 5 MBs
 - Easy to start, just run the JAR
 - Doesn't need a servlet container
 - Non Blocking
 - Implemented with [Vert.x](http://vertx.io/)
 - Compression enable (Saves alot of bandwidth)
 - Docker Image to easily deploy

 
## Another hystrix-dashboards web app?
It's the same hystrix-dashboard app as the Netflix one, 
that can be found here [netflix-hystrix-dashboard](https://github.com/Netflix/Hystrix/tree/master/hystrix-dashboard), 
the only difference is that this one isn't servlet based, 
so doesn't need a servlet container, nor any other configuration, 
it's just a single jar that you can run and you are read to start monitoring you hystrix enabled services. 


## Motivations
When i first tried hystrix and hystrix-dashboard, i had some problems testing the examples, not only me but other people had problems too, i think that hystrix-dashboard is soo awesome that shouldn't take more than a single file run to be able to use it, so i built this little adaptation to provide that, and help people that want to get started using hystrix and it's modules, and help advanced users that just need to run a dashboard more easily.
 
 
## Download
The standalone-hystrix-dashboard is available at `Maven Central`, `BinTray`. 

Click on "download" blue badge in the top to go to bintray.

The maven link will be available once it gets published.


## Run fatJar
Generate the fatJar from source or download it and simple do the following:
```
java -jar standalone-hystrix-dashboard-{VERSION}-all.jar
```
it should start the dashboard on default port `7979`.

## Run on background
### Starting the application
Generate the fatJar from source or download it and simple do the following:
```
java -jar standalone-hystrix-dashboard-{VERSION}-all.jar start
```
it should start the dashboard on default port `7979` and it will print an UUID.

### Stopping the application
After starting it, the startup process will print a UUID that you can use it to stop the application,
if you don't remember the UUID you can check the running instances using the following commands:
```
java -jar standalone-hystrix-dashboard-{VERSION}-all.jar list
```

With the UUID you can stop the running instance with the following command:
```
java -jar standalone-hystrix-dashboard-{VERSION}-all.jar stop UUDI
```

## Run from source
To run the project from source simple do the following:
```
git clone https://github.com/kennedyoliveira/standalone-hystrix-dashboard.git
cd standalone-hystrix-dashboard
./gradlew runDashboard
```

it should start the dashboard on default port `7979`.

## Generate fatJar from source
To generate the fatJar from source simple do the following:
```
git clone https://github.com/kennedyoliveira/standalone-hystrix-dashboard.git
cd standalone-hystrix-dashboard
./gradlew fatJar
```
and your fatJar should be in `build/libs/standalone-hystrix-dashboard-{VERSION}-all.jar`.


## Docker Image
There is a docker image available that you can run by:
```
docker run --rm -ti -p 7979:7979 kennedyoliveira/hystrix-dashboard
```

You can pass configuration or jvm params by using the `ENV VAR` `JVM_ARGS` as with the example below:
```
docker run --rm -ti -p 7979:7979 -e JVM_ARGS='-Xmx2048m' kennedyoliveira/hystrix-dashboard
```

The docker images will have tags equal to hystrix-dashboard and hystrix-core versions, so if you need a especific version you can check there, starting with 1.5.1+


## Configurations

You can pass configuration parameters using the `-Dconfiguration=value` parameter, the available configurations are listened in the table below.

Example:

```
java -jar -DserverPort=8080 -DbindAddress=192.168.1.100 standalone-hystrix-dashboard-{VERSION}-all.jar
```

| Configuration | Description | Default |
|---------------|-------------|---------|
| `serverPort` | The port that the server will listen to. | `7979` |
| `bindAddress` | The address that the server will bind to. | `0.0.0.0` |
| `disableCompression` | Flag to disable compression support for the metrics stream | `enabled` | 

## Demonstration
![Demonstration](https://dl.dropboxusercontent.com/u/17155314/standalone-hystrix-dashboard-example.gif)
