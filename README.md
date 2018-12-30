# Scala CNF Remote Converter

Example of Spray serialization usage for custom domain via remote 
Akka Actors. 

## Usage

### Warmup

`sbt "runMain Warmup"`

### JSON Serialization

Located in `domain` and `json` packages.  

For running tests

`sbt test`

### Algebraic transformation with Akka Actors

Run in one terminal

`sbt "runMain ServerApp"`

to start server. Then in another terminal

`sbt "runMain ClientApp"`

## References

[CNF conversion](http://cs.jhu.edu/~jason/tutorials/convert-to-CNF)  

[Akka Remoting](https://doc.akka.io/docs/akka/2.5/remoting.html)  

[Spray repo](https://github.com/spray/spray-json#providing-jsonformats-for-other-types)