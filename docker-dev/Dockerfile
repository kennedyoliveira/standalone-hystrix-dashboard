FROM ubuntu:16.04
MAINTAINER Kennedy Oliveira <kennedy.oliveira@outlook.com>

ENV GIT_REPO https://github.com/kennedyoliveira/standalone-hystrix-dashboard.git
ENV HYSTRIX_DASHBOARD_HOME /opt/standalone-hystrix-dashboard/

# Install dependencies
RUN apt-get update && apt-get install -y \
        openjdk-8-jdk-headless \
        curl \
        git

RUN mkdir -p $HYSTRIX_DASHBOARD_HOME && \
    cd $HYSTRIX_DASHBOARD_HOME && \
    # clone repo
    git clone $GIT_REPO && \
    # build
    cd standalone-hystrix-dashboard && \
    ./gradlew -Prelease.version=dev fatJar && \
    cd .. && \
    # copy the final artifact
    cp standalone-hystrix-dashboard/build/libs/standalone-hystrix-dashboard-dev-all.jar ./

WORKDIR $HYSTRIX_DASHBOARD_HOME
EXPOSE 7979
ENTRYPOINT exec java -jar $JVM_ARGS standalone-hystrix-dashboard-dev-all.jar