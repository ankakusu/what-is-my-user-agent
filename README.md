[whatismyuseragent.com](http://whatismyuseragent.com/) is a public service
where the [User-Agent](http://en.wikipedia.org/wiki/User_agent) of the
visitor is resolved through [Apache DeviceMap](http://devicemap.apache.org/).
You are also allowed to submit your enhancements to the returned device
profile if it does not match the attributes you expected.

Usage
=====

You can directly browse to [whatismyuseragent.com](http://whatismyuseragent.com/).

Installation
============

You need to have Maven and Java 8 installed on your local machine. The
service creates a new GitHub issue under the hood for each new User-Agent
submission request. Hence, for GitHub authentication, you need to setup your
`~/.github` configuration file as follows:

    login=vy
    password=012345678

Then you can safely run the server on your local machine via typing
`mvn exec:java` in the project root.

Contributors
============

- [Volkan Yazıcı](volkan.yazici@gmail.com) [(vy)](https://github.com/vy) -- backend mechanics
- [Roland Franke](mail@rolandfranke.nl) [(ROL4ND909)](https://github.com/ROL4ND909) -- frontend design

License
=======

The source code and the supplementary data provided by the user submissions
are licensed under BSD 3-Clause software license. See `LICENSE` file for
details.
