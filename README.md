Groovy Dependency Injection
===

Overview
---
GDI is a simple, yet powerful, compile-time-assisted dependency injection framework for Groovy.

Disclaimer
---
This is a primitive first shot at doing this. I used the `groovyx.gdi` package namespace, and I can't guarantee that future revisions will preserve this. IOW, the code at this point should be treated as an extreme alpha release.

Compilation
---
`./gradlew clean install`

Artifact
---
After compiling and installing, you can use it likeso:

  ## build.gradle
```groovy
dependencies {
   compile 'groovyx.gdi:gdi:0.1a-SNAPSHOT'
}
```

  ## pom.xml
```XML
<dependency>
   <groupId>groovyx.gdi</groupId>
   <artifactId>gdi</artifactId>
   <version>0.1a-SNAPSHOT</version>
</dependency>
```

Usage
---
GDI requires a static `scope` field to be added to classes that will be injection candidates. The `scope` is an enum instance of type `InjectionScope`. The two valid types are `SINGLETON` and `PROTOTYPE`. The declared `scope` value will dictate how the ObjectRegistrar handles the class' injection into consumer classes.

## `SINGLETON`
    Classes scoped as `SINGLETON` will be instantiated once and that same instance will be given to all classes that have it injected.

## `PROTOTYPE`
    Classes scoped as `PROTOTYPE` will be instantiated for each instance of a class.

A static `injected` field is required in a class that is intending to inject dependencies. The `injected` field is a key value pair of `String` and `Class` types. The `String` key of the map will dictate how the class will reference the injected object. Dependencies are always injected by name. The `Class` value is the injection candidate.

Classes will be automatically registered with the `ObjectRegistrar`. The `ObjectRegistrar` class has a static `register` method that takes a single `Class` parameter that gets invoked in a `static` initialization block of the candidate class. This way, as a developer using the GDI framework, you don't have to take any additional steps to register the candidate class with the `ObjectRegistrar`. A present limitation of the GDI framework is that it can handle only one type each class. Future revisions will include the ability to inject by name and therein allow different instances of the same type to be injected.

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
          // calls "getMyService()"
          myService.tellMeImPretty()
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
          def main = new Main()
          def controller = main.myController // calls "main.getMyController()"
          controller.saySomethingNice() // "You're Pretty :)"
       }
    }

```

License
---
Free for all. Contact me if you need help g(daniel.p.woods@gmail.com) && t(@danveloper)
