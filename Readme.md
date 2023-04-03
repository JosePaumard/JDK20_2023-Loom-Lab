# JavaOne 2023 Loom Hands on Lab

This lab covers three aspects of the Loom project: virtual threads, structured concurrency, and scoped values.

You need a JDK 20 to run it. Virtual threads are a preview feature of the JDK 20, so you need to enable preview features both at compile time and at run time. Structured concurrency and Scoped values are incubator features of the JDK 20 so you need to enable them as well.

## What is This Lab About?

This lab is about exploring Loom and what this project is bringing to the JDK. It focuses on three features of Loom: Virtual threads (https://openjdk.org/jeps/436), Structured Concurrency (https://openjdk.org/jeps/437), and Scoped Values (https://openjdk.org/jeps/429). These features are still preview features or incubator features, meaning that there are available for testing and evaluation, but are subject to change before becoming as final features. One of the differences between preview features and incubator features is that the package of a class part of an incubator feature will move to another package when it makes it as a final feature.

What can you expect from this lab?
1. A good understanding of what a virtual threads are, how you can launch and create them, and what do they bring to the concurrent programming model.
2. Organize our asynchronous tasks using the Structured Concurrency API, and more precisely the `StructuredTaskScope` class.
    1. you will launch tasks using instances of the `StructuredTaskScope` class, and get the results from them,
    2. you will handle time-outs,
    3. you will override this class to implement your own business logic.
3. Set scoped values, in the same way as you set thread local variables.

## Running the Exercises

### Running the Virtual Threads Exercises

You can compile your code with the following command, assuming you are using the JDK 20:

```shell
$ java --enable-preview --release 20 Main.java
```

You can run this `Main` class with the following command:

```shell
java --enable-preview Main
```

You can learn more about preview features here: https://dev.java/learn/using-the-preview-features-available-in-the-jdk/.

### Running the Structured Concurrency and the Scoped Values Exercises

The classes you need for the Structured Concurrency exercises are available in an incubator module of the JDK 20, that you need to import explicitly.

You can compile your code with the following command:

```shell
$ java --enable-preview --release 20 --add-modules jdk.incubator.concurrent Main.java
```

You can run this `Main` class with the following command:

```shell
java --enable-preview --add-modules jdk.incubator.concurrent Main
```

## Getting the Solutions

If you get stuck at some point, you can still check the solutions of the exercises in the `solutions/java` folder. You can even declare this folder as a source folder in your IDE, to load these classes directly, check them and execute them.

The solutions should be seen as "possible solutions". There may be other ways to solve these exercises.

## The Virtual Threads Exercises

### A_VersionCheck

This first class is just there to make sure that you are running the right version of Java. Also, make sure that you have enabled to preview features. You just need to run this class.

### B_StartingThreads

You can just run this class and see what it prints. The main thread is a platform thread. What is the number of this thread?

### C_StartingVirtualThreads

Let us see now how to create and launch virtual threads.

First, Loom brings new pattern to start platform threads. Platform threads and virtual threads are running tasks, instances of `Runnable`.

So let us create a first task, that just prints the current thread. The current thread is the thread that is running this task.

Loom also brings new patterns to create and launch platform threads. You need to call `join()` to make sure that this thread has enough time to run. Check the code of this class to see this pattern.

Then try to find a similar pattern to start a virtual thread. Create a new virtual thread, with a name "virtual", similar to the previous platform thread, and start it.

Do not forget to call `join()` on this virtual thread. 

You need to configure your IDE so that the preview features of your JDK 20 are enabled.

How can you tell that the thread you have created is a virtual thread?

What platform thread is used to run this virtual thread?

You can also explore these two patterns and see how you can customize the threads you are launching.
- Can a virtual thread be a daemon thread?
- How can a platform thread or a virtual thread handle thread local variables?

### D_YieldingVirtualThreads

Let us now create a bunch of virtual threads and see how they can jump from one platform thread to the other. This is a feature that is unique to Loom virtual threads.

You need to create an unstarted virtual thread in the code. The code you are given defines a task that currently does nothing. You need to modify it so that it does the following:
- if the index is 0, then print the current thread,
- then go to sleep for a few milliseconds, 20 is enough.
- then repeat the process: if the index is 0, then print the current thread again,
- then go to sleep again for 20ms,
- and then if the index is 0, print the current thread one last time.

In a nutshell, you are launching a task that prints the current thread, then you put it to sleep, and you repeat this process 3 times.

What is the number of the virtual thread? Does it change during the process?

What is the number of the platform thread running this virtual thread? Does it change during the process?

Does blocking a virtual thread blocks a platform thread?

Try to decrease the number of virtual threads you are creating. Do you still see this behavior?

Because blocking a Loom virtual thread is so cheap, trying to pool them becomes useless.

### E_HowManyPlatformThreads

Let us discover how many platform threads you need to run your virtual threads.

There are two concurrent sets created in this class, one to store the name of the pools of platform threads that are used by default, and the other to store the name of the platform threads.

Just call the two methods `readThreadPoolName()` and `readPlatformThreadName()` in that thread and add the pool name and the platform thread name in the corresponding sets.

How many pools is Loom using?

How many platform threads have been used for your virtual threads?

You can try to increase the number of virtual threads, to see if this number varies. You can also try to run this code on a machine with a different number of cores, to see how this number is changing.

## The Structured Concurrency Exercises

### A_VersionCheck

This first class is just there to make sure that you are running the right version of Java. Also, make sure that you have enabled to preview features. You just need to run this class.

### B_FirstScope

Let us first explore the `StructuredTaskScope` class. This class is your main entry point for structured concurrency with Loom. Go ahead and visit the `Weather` record, and follow the instructions to implement the `readWeather()` method.

1. First, create an instance of the `StructuredTaskScope` class. This class implements `AutoCloseable`, so you should create it in a
   _try-with-resources_ pattern. Because this scope will produce a `Weather` object, you should create an instance of `StructuredTaskScope<Weather>`. There is no trick here: this class has an empty constructor that you can call.

2. Second, create and submit a task to this scope. This task is an instance of `Callable<Weather>`, because your scope is parameterized by `Weather`. You can create this `Callable` by simply calling `readWeatherFromA()`. It will produce a (not so) random `Weather` instance, after a little (random) delay. Submitting a task to a scope is done by calling its `fork()` method. It gives you a future object that you can put in a variable.

3. Third, once you have submitted tasks to your scope, you should call its `join()` method. This call is blocking: `join()` will return when all your tasks have complete. Not calling `join()` will make your code fail with an exception when you call `get()` on your `Future` objects. You can experiment that in this lab.

4. Fourth and last point: you can get the result of your future by calling its classical `get()` method, or even better, its new `resultNow()` method. This last method should be called only if you know that your future is complete, which is the case after you called `scope.join()`.

Once you are done with this, you can run this `B_FirstScope` class. Do not forget to add the following options to run this main method: `--enable-preview --add-modules jdk.incubator.concurrent`.

Everytime you submit a callable to a structured task scope, it creates a virtual thread and use it to execute this callable. Because it is cheap to create and block virtual threads, there is no need to pool them. Even if a structured task scope looks like an executor service, it works in a different way. An executor service pools platform threads, a structured task scope creates them on demand, and let them die once they are not needed any more.

### C_ShutdownOnSuccessScope

At this point, you should have a working `Weather.readWeather()` method that is querying one (fake) weather forecast server.

What you want to do is query more than one server, and because all results are equivalent (weather forecast should always be the same), you would like to get the first result, and interrupt the others requests. Fortunately, there is a special scope to do that: the `StructuredTaskScope.ShutdownOnSuccess`.

1. Modify the code you wrote in the previous section, and create an instance of this scope instead of the previous `StructuredTaskScope`. The `ShutdownOnSuccess` class takes a parameter, that is `Weather` in this case.
2. Submit more than one query, by calling `fork()` on your scope object and putting the returned future in a variable. You have more methods for that: `readWeatherFromB()`, `readWeatherFromC()`, `readWeatherFromD()`, etc... They have been written so that they will provide a `Weather` instance with a random delay.
3. Do not forget to call `scope.join()`, this is still mandatory.
4. Then you can call `scope.result()`, that returns the first `Weather` instance it gets. Moreover, all the other callables you have submitted have been cancelled, and the corresponding virtual threads have been interrupted. You can check that by printing the state of all the futures on the console. The `Future` interface has a new method: `state()`, to do that. Note that it may happen that a future was not cancelled because the scope did not have the time to interrupt the corresponding thread.
5. One you are done observing how these future have been cancelled, you can remove them from your code, you will not be needing them anymore.

### D_ExtendingScope

Extending a scope can be done to implement your own behavior. In this exercise, you will be querying several quotation servers, each giving you its own price for a given travel. What you want is the lowest price. So you need to wait for all the answers from these servers, and then get the best quotation.

1. Add some code in the `Quotation` record. This code should query some (fake) quotation servers, using the same pattern as the one you used to query the weather servers, and a regular `StructuredTaskScope<Quotation>`.  Store each future in a variable.
2. After your call `scope.join()`, add some code to get the best quotation, the one with the least price. You can use a Stream pattern to find this quotation, or a for loop. The code you have written is working and is getting the best quotation.

Let extend the `StructuredTaskScope` class to implement this business logic (the computation of the best quotation from all the available quotations) with this class.

3. Create a `QuotationScope` class, that extends `StructuredTaskScope<Weather>`, and modify your `readQuotation()` method so that it forks its task using this scope rather than the plain `StructuredTaskScope<Weather>`. You can create it as a local class of the `Quotation` record.
4. Everytime a task completes, Loom calls a callback method, `handleComplete()` with the corresponding completed future. You can now override this method. The behavior of this method depends on the state of the future it processes. The state may have four values, you can create a switch to handle them.
- `RUNNING` : this case should never happen. The future that is passed to this method should not be in that state.
- `SUCCESS` : you can save the quotation in a collection for future use. Let us call it `quotations`, for future reference.
- `FAILED` : something went wrong, no quotation could be got, an exception has been thrown. You can save it in a collection for future use. Let us call it `exceptions`.
- `CANCELLED` : the task has been cancelled or interrupted. In that case, no result and no exception has been produced.

Be careful that the `handleComplete()` method is called in the virtual thread that executed your task. This method should be thread safe, as well as the collections that are receiving the quotations and the exceptions.

Once all the tasks you submitted are complete, then your call to `join()` returns. At this point the two collections you created: one for the quotations and the other for the exceptions, contain the results of your queries. You can now analyze them.

5. Create a `bestQuotation()` method that analyzes the quotations you put in your collection, and returns the one with the least price.
6. Create an `exceptions()` method that takes all the exceptions you put in the other collection, and add them as suppressed exceptions to a single exception. You can create a special class to do that, `QuotationException` for instance.
7. You may be wondering what would happen if your `quotations` collection is empty. Well in that case, you may have exceptions in your `exceptions` collection. So maybe you can design your `bestQuotation()` method to make sure that it would fail with the right exception.

Just some quick notes about the class you just wrote. First, it encapsulates your business logic. How do you need to aggregate your business data (in this example, the quotations). The aggregation logic is written in the `bestQuotation()` method, so it's easy to review it. Second, it processes one future at a time, so again, reviewing the code is simple: it's all in one place.

Writing unit tests for this class is also easy. Because all your code is synchronous, you can easily create completed futures to call your `handleComplete()` method, and check that it is doing the right thing. And the same goes for the `bestQuotation()` method. It is also a synchronous method, that is very easy to unit test.

### E_BuildingTravelPage

Now that you have a quotation and a weather, what about you build a travel page? The `TravelPage` record is there for that: it has a `quotation`component and a `weather` component.

Before you start building your travel page, let us think about these two components.
- The quotation component is critical: if you do not have a quotation, then you cannot sell anything to your customer.
- The weather component, on the other hand, is nice to have, but if you do not have it, you can still sell something. Moreover, you do not want the weather to take to long to get and see your customer go away because of that.

1. The first thing you need to implement is a timeout on the weather. If something goes wrong, and you get an exception, or if getting the weather takes to long, then you want to give a default value instead of making your whole process fail. The `StructuredTaskScope` class has just the right method for that: `joinUntil()`. If you want to set up a 100ms timeout, then you can pass `Instant.now().plusMillis(100)` to this `joinUntil()` method call. You will then have to handle a `TimeoutException`, that you can use to return your default value for the weather.
2. Now you can create a `readTravelPage()` factory method in the `TravelPage` record, following what you did for `Weather` and `Quotation`. This method can create a scope and submit two callables to it: one to read the weather, and the other to read the quotation. You can create your own `TravelPageScope`, by extending `StructuredTaskScope`. To do that, you need to find a type parameter for this scope, that is a super type to all the objects this scope has to handle: `Weather` and `Quotation`. So far, this type is `Object`. What about you create an interface, `PageComponent` and make `Weather` and `Quotation` implement it? Then `TravelPageScope` can extend `StructuredTaskScope<PageComponent>`.
3. From there, you can handle the weather and the quotation with futures in the `readTravelPage()` method. This will work but it will make your code hard to unit test. If you decide to override the `handleComplete()` method, your business logic is written in its own method, that you can easily test.
4. Here are some hints to override `handleComplete()`.

    1. What you get is a `Future<PageCompoment>`, that can carry a value, or an exception. So there are two things you need to do: check the state of this future, and check the type of the value it carries. Checking the state first is your best choice.
    2. You can follow what you did for the `QuotationScope` class, and handle the `RUNNING` and `CANCELLED` cases in the exact same way.
    3. Handling the `SUCCESS` case is interesting. You need to check if the produced value is a `Weather` or a `Quotation`. You can do that with a switch on types, another preview feature of the JDK 20. Because you are working with Loom, preview features are already enabled, so you can use the switch on type with no further configuration. You can even go one step further, and seal the `PageComponent` interface, only permitting `Weather` and `Quotation` to implement it. Your switch is now exhaustive without having to add a default case. You can now save the produced quotation and weather is instance fields of this scope. Make sure that they are volatile, because they need to be visible.
    4. Handling the `FAILURE` case can be done in the same way. You may need to check if the exception is an instance of `Quotation.QuotationException` and handle it specifically, and catch all the other exceptions.


## The Scoped Value Exercices

### A_FirstScopedValue

This first exercise shows you how scoped values are working, and the several methods you need to know to use them. 

First, create a scoped value of `String`. Scoped values are created using the `newInstance()` factory method.

You have two methods to read the value bound to a scoped value. The first one is `isBound()`, that checks if there is a value bound, and `get()` to read the value bound. Create a task of type `Runnable` that prints the value bound to this scope value if there is one, and `UNBOUND` if there is none. Execute this runnable by calling its `run()` method. What result does it print? 

You can run a task in the context of a bound scope value with the following pattern:

```java
Runnable task = ...;
ScopedValue<String> scopedValue = ...;
ScopedValue.where(scopedValue, "AAA").run(task);
```

You can set as many scoped valued as you need by chaining the calls to `where()`. You can only call `run(Runnable)` or `call(Callable)` once.  

Run this code with the task you have created. What is now the value bound to your scoped value? 

Note that you did not create any thread in this code. Scoped values are set for the running of the task you pass. This task can be a `Runnable` or a `Callable`. All the tasks that are spawned by your first task, executed in other threads or not, have access to the scoped values you set when running the first task. 

Note also that when the `run(Runnable)` or `call(Callable)`, then your scoped value is not bound anymore.

### B_ScopedTravelPage

Consider the Travel Page example again. Suppose that accessing the Quotation server is in fact not free, and requires a licence key. This is a typical example where using scoped values can be interesting. 

1. Create a scoped value in the main method of one of your solutions. This scoped value should be visible from any other class, putting it in a public static field may be a good solution. 
2. Run the construction of your travel page in the context of this scoped value. You can bind it to your licence key. 
3. Then check if this scoped value has been properly bound when creating your Quotation objects. Remember that you can add your validation rules to the compact constructor of a record. If no licence key has been bound, or if the value is not the right one, then throw an exception to prevent the creation of this Quotation. 

## Wrapping up

You should now have a good understanding of how virtual threads are working, and how you can use them in the Structured Concurrency Patterns. You also worked with Scoped Values, the pattern brough by Loom to improve the Thread Local variables patterns. 