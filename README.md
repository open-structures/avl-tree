# Sequence

An ordered sequence of elements with fast element look-up and insert. 
Having [AVL tree](https://en.wikipedia.org/wiki/AVL_tree) under the hood allows these operations to have logarithmic performance.

```xml
<dependency>
    <groupId>org.open-structures</groupId>
    <artifactId>sequence</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Quick start

Let's create a sequence of integers

    Sequence<Integer, Integer> intSequence = new InMemorySequence<>(String::compareTo);

and add few elements:

    intSequence.insert(1);
    intSequence.insert(5);
    intSequence.insert(15);

Now we can check if the element is part of the sequence and what elements it's surrounded by:

    intSequence.equalTo(7); // returns null
    intSequence.lessThan(7); // returns 5
    intSequence.greaterThan(7); // returns 15 
    intSequence.greaterThan(15); // returns null

The `Sequence` could be used for more complicated data types. For example let's say we have a `CalendarEvent` class that implements `Timeslot` interface: 

    public interface TimeSlot {
        Instant getStart();
        Instant getEnd();
    }

This sequence will contain calendar events:

    Sequence<CalendarEvent, Timeslot> eventsSequence = new InMemorySequence<>(timeslotComparator);

Let's say our sequence contains some events. Now we can look up events using timeslots:

    eventsSequence.equalTo(twelveToFive); // anything booked for this timeslot? 
    eventsSequence.greaterThan(lunch); // what's after lunch?


