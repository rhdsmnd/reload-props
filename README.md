# Dynamic Property Reloading POC

Simple example showing how to reload Spring property values in a Spring application. Almost all required knowledge was obtained by dilligently reading the first three sections of the [Spring Core reference document](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#spring-core)

### Summary

Simple example which shows how to implement dynamic reloading of application config values.  Start with "mvn spring-boot:run", then make a request to "/" to see current value of configuration value "sample.prop".  To show dynamic reloading, change the "sample.prop" value in the application.properties file located in the "target/classes" directory.  Then, make another request to "/" to see the updated value.

### Components Used

ApplicationContext: Stores (Abstract)Environment, ResourceLoader, and implements dependency injection (autowire functionality).  Resolves property placeholders in beans.

AbstractEnvironment: Provides access to PropertySources

Property Sources/Property Sources: Object which Spring uses to store and resolve properties and ${...} placeholders

### Flow

1: Http Endpoint bean is initialized

* Spring Application context injects itself into bean
  1b:  Custom logic to identify name of application config property source object
  1c:  Spring Application context uses dependency injection and environment property sources to inject value of "sample.prop" into instance variable "sampleProp"

2: 	Request is made for reloaded property value

  2a:  Custom logic to make a new Properties object for updated app config file (located in target/classes directory) using the Application Context ResourceLoader
  2b:  The property source object with the old app config values is replaced with the new
	property source that contains the updated properties object
  2c:  Reload the injected value (instance variable "sampleProp") in the Http endpoint bean by using the Bean Factory method "autowireBeanProperties" with the current bean as the target for reloading
