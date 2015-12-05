# Introduction
This project represents back-end implementation of social queueing application. It's one of the projects implemented inhttp://www.codeforireland.com/

The purpose of the application is estimate how long client has to wait until he/she will be serviced based on his ticket number and currently serviced number. Both information are provided by users.

TODO:
1) Test new endpoints:
- @RequestMapping(value = "v2/queues/{queueId}/tickets/{ticketId}", method = RequestMethod.POST)
- @RequestMapping(value = "v2/queues/{queueId}/tickets/{ticketId}", method = RequestMethod.GET)

2) Update documentation to represent current implementation of estimator

Full documentation:
http://projects.appbucket.eu/myq.html
