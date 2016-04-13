# FatBoy


## What is it?

FatBoy is a fixture generation tool created with the aim of reducing the boilerplate required when making test fixtures.

## How to use it?

```
new FatBoy().create(MyClass.class)
```

Will create a new instance of MyClass, including subclasses

## Configuring it

Once you have a fatboy instance, configuring it is simple

### Custom factories

To add a factory for a specific field within a class

```java
    // add a factory for the 'fieldName' field
    fatBoy.addFieldFactory(MyClass.getDeclaredField("fieldName"), () -> new SomeClass("values here"))
    // add a constant value for the 'fieldName' field
    fatBoy.addFieldConstant(MyClass.class, "fieldName", new WhateverTheFieldClassIs("values here"))
```

Class factories are added similarly. A class factory can be a generic class factory, a simple one or a constant value

```java
    // add a fully implemented class factory, make sure it implements GenericClassFactory 
    //if you intend to have it handle generic types
    fatBoy.addClassFactory(new MyClassFactoryInstance())

    // Add a classFactory that is called back with the in-use instance of FatBoy
    fatBoy.addClassConstant(new MyClass())

    // add factory returning a new MyClass()
    fatBoy.addClassFactory(MyClass.class, () -> new MyClass())

    // Make all instances of MyClass have a constant value
    fatBoy.addClassConstant(new MyClass())
```

FatBoy provided factories are a utility factory used when you want to create a class your way, but want some values created by fatboy

```java
    fatBoy.addFatBoyProvidedFactory(MyClass.class, (fatBoy) -> new MyClass("Value 1", "Value 2", fatBoy
        .create(someClass.class))
```

### Custom overrides

You can add overrides for fields in the top level of the object you're creating by passing a map to the create method

````java
    Map<String, Object> overrides = new HashMap<>() {{ add("FieldName", new SomeRandomClass() }} 

    fatBoy.create(MyClass.class, overrides);
````
