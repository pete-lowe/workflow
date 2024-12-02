A basic task and workflow execution service.

Note for those looking at this - I got a bit wrapped up in functionality, and rather than you having to wait any longer I've decided to share it in the current format. Its missing lots and lots of tests  (hopefully I'll add a few more before you get round to reviewing the code).
There are also lots of things that need re-wroking or refactoring - I probably should have tried to limit the functionality slightly and polish it up more.

Mainly tried to show how AWS services might be utilised (this should all run locally with the localstack setup) and the async processing that we can achieve through the sqs. Also added some aop to handle background logging and notificaitons of execution.

Things left to do:

1. Finish unit testing - not a huge amount of complex logic so would be straightforward enough to have 100% coverage
2. Integration testing the following:

    - Controllers
    - db operations (this can run against local stack)
    - queue operations (similar to above, ensure that messages can be written and de-serialized from the queue)
3. fix child parent task behaviour - the idea here would be that parent tasks should trigger execution of the child tasks
      - i.e. parent task executes and has reference to its dependent tasks - these definitions could then be fetched from db and executed. Infinite depth possible so would need to flatten recursively and maintain the parent child relationship (probably just parent to child, since its executing async)
4. extend task types to be customisable rather than the hard coded enums currently in use
5. Add scheduling - could do this a few ways, either field on task which is continuously read from a queue and then executed when time is within range, although this is probably rather wasteful in terms of queue operations. Could maybe do something a bit novel using the time to live in dynamo db and db streams (e.g. have a record for each scheduled task where the time to live is the scheduled execution date, when dynamo sweeps these records a dynamo db stream will be sent which can then trigger the task fetch and execution - useful point here is that ttl sweeps are free so you save on db reads although if it relates to a time sensitive task this would not be appropriate as ttl cleans are not guaranteed to be accurate  