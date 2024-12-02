A basic task and workflow execution service.

Note for those looking at this - I got a bit wrapped up in functionality, and rather than you having to wait any longer I've decided to share it in the current format. Its missing lots and lots of tests  (hopefully I'll add a few more before you get round to reviewing the code).
There are also lots of things that need re-wroking or refactoring - I probably should have tried to limit the functionality slightly and polish it up more.

Mainly tried to show how AWS services might be utilised (this should all run locally with the localstack setup) and the async processing that we can achieve through the sqs. Also added some aop to handle background logging and notificaitons of execution.

