.PHONY: build clean worker run

build:
	@mvn clean install

clean:
	@mvn clean -q

worker:
	@mvn compile exec:java -Dexec.mainClass="com.masorange.temporal.hackathon.Main"
