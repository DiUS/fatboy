# FatBoy


## What is it?

FatBoy is a fixture generation tool created with the aim of reducing the boilerplate required when making test fixtures.

## How to use it?
```
new FatBoy().create(MyClass.class)
```

Will create a new instance of MyClass, including subclasses

## Configuring it


### Utility configuration methods

#### Passing configuration hints to class factories
 You can set configuration for a provider by using the ```setFactoryConfig``` method.
 Currently, the only factories that use a configuration object are the CollectionFactory and MapFactory

 To set the amount of items they a CollectionFactory should add to it's collection you can do
```java
    fatBoy.setFactoryConfig(CollectionFactory.class, FieldCount.random(0,5))
    // or for a constant value
    fatBoy.setFactoryConfig(CollectionFactory.class, FieldCount.constant(5))
```

The``` FieldCount``` is a subclass of ```FactoryConfig```. If you want to create a custom ```ClassFactory``` and pass it a configuration object you can do the same as above.

In the future more configuration objects will be added to allow defaults, range and constant setting for the primite factories (Strings, longs, ints etc).

#### Custom Factories
Once you have a fatboy instance, configuring it is simple. 

Lets say you have the following class

```java
    public class MyClass {
        private String one;
        private int two;
        private UUID reference;
        
		/* Add a constructor, or don't. FatBoy doesn't care. */
    }
```

Below are the ways you can configure FatBoy to create this class, or it's fields.

#### Class Factories

To add a factory for a specific class

```java
    fatBoy.registerClassFactory(MyClass.class, () -> new MyClass());
```

This will tell FatBoy to call the provided factory any time it needs a ```MyClass ```  instance

Generic class types are createable too, see the section at the end for more info

#### Constant values for a specific Class

You can also add a constant value for any occurence of a class. To do this with ```MyClass``` do the following:
```java
	fatBoy.setClassConstant(new MyClass())
```
#### Field factories

Using the ```MyClass``` defined above, to get FatBoy to call your factory when it encounters the ```reference``` field

```java
    fatBoy.registerFieldFactory(MyClass.class.getDeclaredField("reference"), () -> UUID.randomUUID())
    // or 
    fatBoy.registerFieldFactory(MyClass.class, "reference", () -> UUID.randomUUID())
```

Fields can also have constant values attached, in a similar fashion to how class constants are registered
```java
    fatBoy.setFieldConstant(MyClass.class, "reference", UUID.randomUUID())
    // or 
    fatBoy.setFieldConstant(MyClass.class.getDeclaredField("reference"), UUID.randomUUID())
```
#### Factories that receive an instance of FatBoy when they're called
For when you want to define the values for some fields in your class, but want FatBoy to create the others. For the purposes of this example, lets assume ```MyClass``` has a constructor with fields defined in the order they're declared in. Here's how that would look

```java
    fatBoy.addFatBoyProvidedFactory(MyClass.class, (fatBoy) -> new MyClass(fatBoy.create(String.class), 2, UUID.randomUUID())
```


#### Custom overrides

You can add overrides for fields in the top level of the object you're creating by passing a map to the create method

```java
    Map<String, Object> overrides = new HashMap<>() {{ add("FieldName", new SomeRandomClass() }} 

  fatBoy.create(MyClass.class, overrides);
```

#### Generic class factories

Lets assume ```MyGenericClass``` is defined as follows

```java
	public class MyGenericClass<T> {
	    public T someFoo;
	}
```

And lets assume ```MyClass``` is as follows
```java
	public class MyClass {
		public MyGenericClass<String> myGenericClassField;
	}
```

You can add a factory to handle multi-layer parametrized types if required. When registering a generic class factory, it will be called with 2 params: ```rawType``` which will be an instance of ```Class```, and actualType, which is an array of ```Type``` 's. For a full reference implementation, see either the CollectionFactory, or the MapFactory.

For our example, rawType will be ```MyGenericClass```, and ```actualType```  will be a type array of length one with the first and only element being the ```java.lang.String``` class.

Lets look at what our registered generic class factory looks like.
```java
        fatBoy.registerGenericClassFactory(MyGenericClass.class, (rawType, actualType) -> {
	         MyGenericClass genericClass = new MyGenericClass();
             genericClass.someFoo = fatBoy.createGeneric(actualType[0]);
             return genericClass;
        });
```

You will be able to create instances of ```MyClass``` directly, but attempting to create a direct instance of the unknown-generic--type ```MyGenericClass``` without the field metadata providing the actual type arguments will not work. Hence ``` fatBoy.create(MyClass.class)``` will work .

Generic field factories are similar, but easier to use because you don't have to try and determine the entire type of a paramtrized field yourself, instead a resolved version will be passed to you as the ```actualType``` param.
