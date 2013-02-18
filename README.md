Groovy Dependency Injection
===

Overview
---
GDI is a simple dependency injection platform for Groovy that employs compile-time transformations to assist with class-level dependency injection.

Compilation
---
`./gradlew clean install`

Artifact
---
After compiling and installing, you can use it likeso:

  ## Build.gradle
```groovy
dependencies {
   compile 'groovyx.gdi:gdi:0.1-SNAPSHOT'
}
```

  ## pom.xml
```XML
<dependency>
   <groupId>groovyx.gdi</groupId>
   <artifactId>gdi</artifactId>
   <version>0.1-SNAPSHOT</version>
</dependency>
```

Usage
---
GDI requires a static `scope` field to be added to classes that will be injection candidates. The `scope` is an enum instance of type `InjectionScope`. There are two valid options `SINGLETON` and `PROTOTYPE` and these values will dictate how the ObjectRegistrar handles the class' injection into consumer classes.

    ## `SINGLETON`
    Classes scoped as `SINGLETON` will be instantiated once and that same instance will be given to classes who have it injected.

    ## `PROTOTYPE`
    Classes scoped as `PROTOTYPE` will be instantiated for each instance of a class.

A static `injected` field is required in a class that is intending to inject dependencies. The `injected` field is a key value pair of `String` and `Class` types. The `String` key of the map will dictate how the class will reference the injected object. Dependencies are always injected by name. The `Class` value is the injection candidate.

Classes must be registered with the `ObjectRegistrar`. The `ObjectRegistrar` class has a static `register` method that takes a single `Class` parameter. A present limitation of the GDI framework is that it can handle only one type each class. Future revisions will include the ability to inject by name and therein allow different instances of the same type to be injected.

Sample
---
Here is an example usage of GDI:

  ## Controller
```groovy
    package com.danveloper.gdi.examples

    class MyController {
       static scope = InjectionScope.PROTOTYPE
       static injected = [myService: MyService]

       void saySomethingNice() {
          getMyService().tellMeImPretty()
       }
    }
```
  ## Service
```groovy
    package com.danveloper.gdi.examples

    class MyService {
       static scope = InjectionScope.SINGLETON

       void tellMeImPretty() {
          println "You're pretty :)"
       }
    }
```
  ## Runner
```groovy
    package com.danveloper.gdi.examples

    class Main {
       static injected = [myController: MyController]

       public static void main(String[] args) {
          ObjectRegistrar.register(MyController)
          ObjectRegistrar.register(MyService)

          def main = new Main()
          def controller = main.getMyController()
          controller.saySomethingNice() // "You're Pretty :)"
       }
    }

```

License
---
Free for all. Contact me if you need help g(daniel.p.woods@gmail.com) && t(@danveloper)
