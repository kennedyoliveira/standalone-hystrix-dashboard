# Standalone Hystrix Dashboard
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
When i first tried hystrix and hystrix-dashboard, i had some problems testing the examples, not only me but other people had problems too,
i think that hystrix-dashboard is soo awesome that shouldn't take more than a single file run to be able to use it, so i built this little adaptation
to provide that, and help people that want to get started using hystrix and it's modules, and help advanced users that just need to run a dashboard
more easily.
 
 
## Download
The standalone-hystrix-dashboard is available at `Maven Central`, `BinTray`.