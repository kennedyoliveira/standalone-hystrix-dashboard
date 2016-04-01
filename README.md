# Standalone Hystrix Dashboard

[![Build Status](https://travis-ci.org/kennedyoliveira/standalone-hystrix-dashboard.svg?branch=master)](https://travis-ci.org/kennedyoliveira/standalone-hystrix-dashboard)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/ee8e2b298bf24109b642cde4a4df8635)](https://www.codacy.com/app/kennedy-oliveira/standalone-hystrix-dashboard)
[![Dependency Status](https://www.versioneye.com/user/projects/56fcad23905db1003b29956c/badge.svg?style=flat)](https://www.versioneye.com/user/projects/56fcad23905db1003b29956c)
[![Download](https://api.bintray.com/packages/kennedyoliveira/maven/standalone-hystrix-dashboard/images/download.svg) ](https://bintray.com/kennedyoliveira/maven/standalone-hystrix-dashboard/_latestVersion)

Standalone *hystrix dashboard* that can be started using a single fatJar and is very lightweight and fast!

## Features
 - Extremely fast startup (About half a sec)
 - Extremely lightweight, the fat jar is less than 4 MBs
 - Easy to start, just run the JAR
 - Doesn't need a servlet container
 - Non Blocking
 - Implemented with [Vert.x](http://vertx.io/)
 - Compression enable (Saves alot of bandwidth)
 
 
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
