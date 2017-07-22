Talk 2 Me
---------

This is a simple scala socket that returns a file to the connecting
client

I created this as a need in a team I was working with where
a team-mate needed a authentication token that expires every 20 mins


run server
----------

```bash
sbt run
```

talk to me
----------

```bash
curl -XGET 10.11.208.248:9999
```

