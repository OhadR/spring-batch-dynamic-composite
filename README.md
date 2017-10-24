# spring-batch-dynamic-composite [![Build Status](https://travis-ci.org/OhadR/spring-batch-dynamic-composite.svg?branch=master)](https://travis-ci.org/OhadR/spring-batch-dynamic-composite)   [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.ohadr/spring-batch-dynamic-composite/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.ohadr/spring-batch-dynamic-composite)


This project is all about spring-batch' composite processor and writer. Currently, spring-batch' built-in composite processor and writer are not dynamic. Meaning, one set them in design time (and coding) and it cannot be changed during runtime. But there are cases that this is needed.

Without loss of generality, we say here "processor" but mean "reader" and "writer" as well.

The idea is to have the ability to replace a processor(s) at runtime. For example, in case of multiple processors (AKA composite-processor), to have the option to add/remove/replace/change-order of processors.

In the implementation, there is `DynamicCompositeItemProcessor` (which is a real `ItemProcessor`) that uses a manager to read the list of processors bean-names from the DB. Thus, the processors list can be modified and reloaded.

As mentioned, same for reader as well as for writer.
 
## why do we need this?

There are cases that processors are used as "filters", and it may occur that the business (the client) may change the requirements (yes, it is very annoying) and ask to switch among filters (change the priority). 

Other use case is having multiple readers, reading the data from different data warehouses, and again - the client changes the warehouse from time to time (integration phase), and I do not want my app to be restarted each and every time. 

There are many other use cases, of course.

## how to use it

The recommended way to use this module in your project is with a dependency management system (such as Maven). As it is available in [Maven Central Repository](http://search.maven.org/#search%7Cga%7C1%7Cspring-batch-dynamic-composite), the snippet below can be copied and pasted into your pom.xml:


```xml
		<dependency>
			<groupId>com.ohadr</groupId>
			<artifactId>spring-batch-dynamic-composite</artifactId>
			<version>1.0</version>
		</dependency>
```

In addition, you can fork/clone, of course.

## building the sources

This is a maven-based application. You can easily use 

`mvn clean install`.

## test application

you can find a test-app here: https://github.com/OhadR/spring-batch-dynamic-composite-test

It is a non-web spring-batch application, that uses this code as a maven-dependency, and uses the `DynamicCompositeItemProcessor` and writer. It configures a DB to show how to use the JAR, and in addition it configures another DB table to write the results it reads from a file. 

## reporting an issue

If there is an issue, [open an issue](https://github.com/OhadR/spring-batch-dynamic-composite/issues) here in GitHub.

## contributing

Contributions are welcome!