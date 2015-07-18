# Spam classifier application created in Clojure.

# Instaling instructions
Running this application is fairly simple. First you'll need to download it, in either way by cloning this repo to desktop or by downloading zip file. Read this instruction until the end, to find the most convinient way for you to run this application.

First, you will need a database. For this application, I've used mongoDB, which is NoSQL database, and presents cross-platform document-oriented database. To start it, get mongo database from this [link](http://www.mongodb.org), install it, then navigate to ../bin/mongodb.exe and wait until it has made connection to the localhost.

After that, you'll need to download and install [Leiningen](http://leiningen.org). Leiningen presents dependency management tool for configuration of projects written in the CLojure. It enables its user to create, bulid, test and deploy projects. If your machine is running on Microsoft Windows, than you can handy get win instaler, which will do all dirty work with PATH variable and similar stuff for you. After you have installed Leiningen open the command prompt, navigate to project folder and type lein run.

