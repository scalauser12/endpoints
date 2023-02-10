package com.example

import cats.effect._
import cats.implicits._
import com.comcast.ip4s._
import endpoints4s.http4s.server._
import endpoints4s.openapi
import endpoints4s.openapi.model.Info
import endpoints4s.openapi.model.OpenApi
import org.http4s._
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router

import scala.concurrent.duration._

object Server
    extends Endpoints[IO]
    with CounterEndpoints
    with JsonEntitiesFromSchemas
    with ResourceApp.Forever {
  override def run(args: List[String]): Resource[IO, Unit] =
    Resource.eval(Ref.of[IO, Counter](Counter(0))).flatMap(server(_).void)

  private def currentValueRoute(ref: Ref[IO, Counter]) =
    currentValue.implementedByEffect(_ => ref.get)

  private def incrementRoute(ref: Ref[IO, Counter]) =
    increment.implementedByEffect(increment => ref.update(c => c.copy(c.value + increment.step)))

  private def counterRoutes(ref: Ref[IO, Counter]) =
    HttpRoutes.of(routesFromEndpoints(currentValueRoute(ref), incrementRoute(ref)))

  private def router(ref: Ref[IO, Counter]) =
    Router[IO]("/" -> counterRoutes(ref), "/" -> DocumentationServer.docRoutes).orNotFound

  private def server(ref: Ref[IO, Counter]) = EmberServerBuilder
    .default[IO]
    .withHost(ipv4"0.0.0.0")
    .withPort(port"8080")
    .withHttpApp(router(ref))
    .withShutdownTimeout(2.seconds)
    .build
}

object CounterDocumentation
    extends CounterEndpoints
    with openapi.Endpoints
    with openapi.JsonEntitiesFromSchemas {
  val api: OpenApi =
    openApi(Info(title = "API to manipulate a counter", version = "1.0.0"))(currentValue, increment)
}

object DocumentationServer extends Endpoints[IO] with JsonEntitiesFromEncodersAndDecoders {
  val docRoutes =
    HttpRoutes.of(
      endpoint(get(path / "documentation.json"), ok(jsonResponse[OpenApi]))
        .implementedBy(_ => CounterDocumentation.api)
    )
}
