import com.typesafe.sbt.packager.docker._

enablePlugins(JavaServerAppPackaging)

dockerEntrypoint ++= Seq(
  """-Dakka.remote.netty.tcp.hostname="$(eval "echo $AKKA_REMOTING_BIND_HOST")"""",
  """-Dakka.cluster.http.management.hostname="$(eval "echo $AKKA_REMOTING_BIND_HOST")""""
// THIS WOULD ONLY BE NEEDED IF WE USED THE ASYNC-DNS CODE
//,
//  "-Dakka.io.dns.resolver=async-dns",
//  "-Dakka.io.dns.async-dns.resolve-srv=true",
//  "-Dakka.io.dns.async-dns.resolv-conf=on"
)

dockerCommands :=
  dockerCommands.value.flatMap {
    case ExecCmd("ENTRYPOINT", args @ _*) => Seq(Cmd("ENTRYPOINT", args.mkString(" ")))
    case v => Seq(v)
  }

version := "1.3.3.7" // we hard-code the version here, it could be anything really

dockerUsername := Some("ktoso")

// use += to add an item to a Sequence
dockerCommands += Cmd("USER", "root")

// use ++= to merge a sequence with an existing sequence
//
// ENABLE THESE IF YOU WANT TO MANUALLY DO DNSLOOKUPS IN THE CONTAINER (FOR DEBUGGING)
//dockerCommands ++= Seq(
//  ExecCmd("RUN", "apt-get", "update"),
//  ExecCmd("RUN", "apt-get", "install", "-y", "dnsutils")
//)
