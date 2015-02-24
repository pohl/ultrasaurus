# Ultrasaurus

## Introduction

Ultrasaurus is an online thesaurus, with the additional capability of giving [hypernyms](http://en.wikipedia.org/wiki/Hyponymy_and_hypernymy), [hyponyms](http://en.wikipedia.org/wiki/Hyponymy_and_hypernymy), and [holonyms](http://en.wikipedia.org/wiki/Holonymy) in addition to the usual synonyms and antonyms.
 
It resulted from a challenge I was given, to:

* Write a single-page web app, that
* Called at least one external service, 
* Displayed the retrieved information to the user, and
* Provided at least one user interaction with the fetched data,
* ...with 6 days to complete the project.

You can try it out online at [labs.screaming.org](http://labs.screaming.org), at least for the time being.

## Implementation

The back end exposes a single REST service `api/words/{word}` via [Jersey](https://jersey.java.net). Requests to this interface are handled with the asynchronous capability in Servlet 3.0, and the returned model objects are transformed into JSON via the [Jackson](http://jackson.codehaus.org) library. The data is sourced via REST client call to [wordsapi.com](https://www.wordsapi.com) using the [Unirest for Java](http://unirest.io/java.html) library. [HK2](http://hk2.java.net) is used for dependency injection, and the [Typesafe Config](https://github.com/typesafehub/config) library provides a configuration file in HOCON format. [Google Guava](http://code.google.com/p/guava-libraries/) is used for its `LoadingCache`.

The interface is a straight-forward application of [Bootstrap](http://getbootstrap.com), using [Backbone.js](http://backbonejs.org) for the routing, [Handlebars.js](http://handlebarsjs.com) for client-side templating, and a bit of [jQuery](http://jquery.com). 

## Running Locally

If you have JDK 8 and Apache Maven already installed, you can build a WAR with a simple `mvn install`.  Once completed, you can rename the resulting `target/ultrasaurus-0.0.1-SNAPSHOT.war` to `ROOT.war` and drop it in the deployment directory of a Servlet 3.0 capabale container like Apache Tomcat 7.x or later.

Once done, you should be able to hit `http://localhost:8080/`

I regret not having time to make this easier via Vagrant or Docker.
 
