FROM kennedyoliveira/java8
MAINTAINER Kennedy Oliveira <kennedy.oliveira@outlook.com>

ENV STANDALONE_HYSTRIX_VERSION 1.5.3
ENV HYSTRIX_DASHBOARD_HOME /opt/standalone-hystrix-dashboard/

RUN mkdir -p $HYSTRIX_DASHBOARD_HOME && cd $HYSTRIX_DASHBOARD_HOME
RUN wget https://bintray.com/artifact/download/kennedyoliveira/maven/com/github/kennedyoliveira/standalone-hystrix-dashboard/$STANDALONE_HYSTRIX_VERSION/standalone-hystrix-dashboard-$STANDALONE_HYSTRIX_VERSION-all.jar

EXPOSE 7979

ENTRYPOINT exec java -jar $JVM_ARGS standalone-hystrix-dashboard-$STANDALONE_HYSTRIX_VERSION-all.jar