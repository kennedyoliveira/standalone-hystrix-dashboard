FROM kennedyoliveira/java8
MAINTAINER Kennedy Oliveira <kennedy.oliveira@outlook.com>

ENV STANDALONE_HYSTRIX_VERSION 1.5.6
ENV HYSTRIX_DASHBOARD_HOME /opt/standalone-hystrix-dashboard/

RUN apk add --update curl
RUN mkdir -p $HYSTRIX_DASHBOARD_HOME && \
    cd $HYSTRIX_DASHBOARD_HOME && \
    curl -L https://bintray.com/kennedyoliveira/maven/download_file?file_path=com/github/kennedyoliveira/standalone-hystrix-dashboard/${STANDALONE_HYSTRIX_VERSION}/standalone-hystrix-dashboard-${STANDALONE_HYSTRIX_VERSION}-all.jar \
    -o standalone-hystrix-dashboard-${STANDALONE_HYSTRIX_VERSION}-all.jar && \
    rm -rf /var/cache/apk/*

WORKDIR $HYSTRIX_DASHBOARD_HOME
EXPOSE 7979
ENTRYPOINT exec java -jar $JVM_ARGS standalone-hystrix-dashboard-$STANDALONE_HYSTRIX_VERSION-all.jar