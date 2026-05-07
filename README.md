Talk 2 Me
---------

This is a simple scala socket that returns a file to the connecting
client

I created this as a need in a team I was working with where
a team-mate needed a authentication token that expires every 20 mins


## How it works

```mermaid
sequenceDiagram
    participant Client as Client (curl)
    participant OS as OS Kernel
    participant SS as ServerSocket<br/>(port 9999)
    participant App as Talk2Me<br/>(single thread)
    participant FS as File System<br/>(application.conf → share.file1)

    App->>SS: new ServerSocket(9999)
    SS->>OS: socket() → bind(9999) → listen(backlog=50)
    OS-->>App: server ready

    loop for every incoming connection (blocking, one at a time)
        App->>SS: server.accept()  [blocks]
        Client->>OS: TCP SYN → SYN-ACK → ACK
        OS-->>SS: connection enqueued in accept queue
        SS-->>App: clientConnection: Socket

        App->>OS: clientConnection.getInputStream (read HTTP request)
        OS-->>App: raw HTTP bytes

        App->>FS: Source.fromFile(config share.file1)
        FS-->>App: file contents as String

        App->>OS: clientConnection.getOutputStream (write response)
        Note over App,OS: HTTP/1.1 200 + Content-Type + file data
        OS-->>Client: TCP segments (response bytes)

        App->>OS: clientConnection.close()
        OS-->>Client: TCP FIN → connection torn down
    end
```

> **Note:** the `while(true)` loop is single-threaded and blocking — each
> connection is fully handled before the next `accept()` call, so concurrent
> clients queue up in the OS accept backlog.


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

