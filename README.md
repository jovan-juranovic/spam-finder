# Spam classifier application created in Clojure.

  When the application is started, a user has to register or login. Initialy, two users, admin, with username: admin, and password: admin and regular user with username: user, and password: password are inserted in database, so you can log in with these credentials. After successful registration, Home page is shown, and you can test classifier by typing some message in input text area.
  
  If you log in with admin credentials, you'll be redirected ti administration page, that serves for managing users (CRUD operations). If you log in with user credentials you'll be redirected to editor page, where you can test spam classifier.
  
  When you type some input in text area, algorithm classifies input as one of the three categories: spam, ham or unsure with appropriate probability. If you want to classify input as spam or ham, just type message in text area, check appropriate option on top of the form and click train me. Next time when you want to classify that same message, algorithm will be closer to the right answer.
  
  Most of the client side scripting was done in [Knockout JS](http://knockoutjs.com/). Knockout is JavaScript library that makes complex things simple with features like declarative bindings, automatic UI refresh, dependancy tracking and templating. In combination with Selmer, client side development was easy and fun.

## Instaling instructions
  Running this application is fairly simple. First you'll need to download it, in either way by cloning this repo to desktop or by downloading zip file. Read this instruction until the end, to find the most convinient way for you to run this application.

  First, you will need a database. For this application, I've used mongoDB, which is NoSQL database, and presents cross-platform document-oriented database. To start it, get mongo database from this [link](http://www.mongodb.org), install it, then navigate to ../bin/mongodb.exe and wait until it has made connection to the localhost.

  After that, you'll need to download and install [Leiningen](http://leiningen.org). Leiningen presents dependency management tool for configuration of projects written in the CLojure. It enables its user to create, bulid, test and deploy projects. If your machine is running on Microsoft Windows, than you can handy get win instaler, which will do all dirty work with PATH variable and similar stuff for you. After you have installed Leiningen open the command prompt, navigate to project folder and type lein run.

Navigate to the application folder and type in the terminan 'lein run' to start application. When you see result of the training on datasets in console, application will be started.

## Libraries used

### [Ring](https://github.com/ring-clojure/ring) and [Compojure](https://github.com/weavejester/compojure)

  These libraries provide the native Clojure API for working with servlets. Ring acts as a wrapper around Java servlet and allows web applications to be constructed of modular components that can be shared among a variety of applications, web servers, and web frameworks. On the other hand, Compojure uses Ring to map request-handler functions to specific URLs. It is important to notice that Ring has become the most used tool for building web applications, and therefore it has a large user community, which can be very helpful during creating applications.

### [Selmer](https://github.com/yogthos/Selmer) 

  Selmer templates consist of plain text that contains embedded expression and filter tags. While Selmer is primarily meant for HTML generation, it can be used for templating any text.

  Selmer compiles the template files and replaces any tags with the corresponding functions for handling dynamic content. The compiled template can then be rendered given a context map.

### Other

[Incanter](https://github.com/incanter/incanter)

  Incanter is a Clojure-based, R-like statistical computing and graphics environment for the JVM. At the core of Incanter are the Parallel Colt numerics library, a multithreaded version of Colt, and the JFreeChart charting library, as well as several other Java and Clojure libraries. The motivation for creating Incanter is to provide a JVM-based statistical computing.

[Lib Noir](https://github.com/noir-clojure/lib-noir) presents set of utillities and helpers for handling common operations that can be found in web application.

## Literature

[Programming Clojure, Second Edition](http://www.amazon.com/Programming-Clojure-Stuart-Halloway/dp/1934356867)

  Second edition of this book presents some changes in Clojure, since it was first introduced in 2007. It has some good explanation about protocols, multimethods, and there is a whole chapter about Clojure's connections to Java.

[Clojure Programming](http://www.amazon.com/Clojure-Programming-Chas-Emerick/dp/1449394701/ref=pd_sim_b_1?ie=UTF8&refRID=0KCSHHVCSA3Z3YCX6JAF)

  Clojure programming is definetely the most comprehensive book about the topic. As Practical Clojure and Programming Clojure it contains explanations about functional languages in general, as well as explanations of Clojure functions and other features, but it goes much deeper into the core of the languge and explains all pros of working with CLojure. Although it is an excellent reference, I would strongly recommend to first read the two first books (and maybe [The Joy of Clojure](http://www.amazon.com/The-Joy-Clojure-Thinking-Way/dp/1935182641/ref=pd_sim_b_2?ie=UTF8&refRID=0KCSHHVCSA3Z3YCX6JAF)) before you start reading this book.   

[Web Development with Clojure](http://www.amazon.com/Web-Development-Clojure-Build-Bulletproof/dp/1937785645/ref=pd_sim_b_3?ie=UTF8&refRID=0KCSHHVCSA3Z3YCX6JAF)

  As its title says, this book is about developing a web applications using Clojure. It is written in a good, easy to understand manner, and for me it has excellent informations about vastly available libraries, tools and good practices out there, that can be used in bulding your application. This is definetely a must-read book about web development in CLojure, and I have used it a lot during bulding my application. Also, this book is published in 2014, so is definetely up to date, which is very important for a programming book.
  
[Programming Collective Intelligence: Building Smart Web 2.0 Applications](http://www.amazon.com/Programming-Collective-Intelligence-Building-Applications/dp/0596529325)
  
  This book is probably best for those of you who have read the theory, but are not quite sure how to turn that theory into something useful. Or for those who simply hunger for a survey of how machine learning can be applied to the web, and need a non-mathematical introduction.
  
[Clojure for Machine Learning](http://www.amazon.com/Clojure-Machine-Learning-Akhil-Wali/dp/1783284358)

  The same as Web Development in Clojure, this book has also appeared in 2014, and it covers a plenty of techniques and algorithms for machine learning and their implementations in Clojure. Among other things, in this book techniques are described for building neural networks, understanding linear regression, categorizing and clustering data, working with matrices, etc. Of course, in seperate chapter there is explanation of recommendation system, which is divided into two sections, first about content-based filtering and second about collaborative filtering which is used in my application.
  
##License

Distributed under the Eclipse Public License, the same as Clojure.
