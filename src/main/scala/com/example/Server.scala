package com.example

import cats.effect.IO
import endpoints4s.http4s.server.{Endpoints, JsonEntitiesFromSchemas}
import org.http4s.HttpRoutes
import cats.effect._

object Server extends Endpoints[IO] with CounterEndpoints with JsonEntitiesFromSchemas with ResourceApp.Forever {

  override def run(args: List[String]): Resource[IO,Unit] =
    for {
      ref <- Resource.eval(Ref.of[IO, Counter](Counter(0)))


    } yield ()


  def currentValue(ref: Ref[IO, Counter]) =
    currentValue.implementedByEffect(_ => ref.get)


  def http4sRoute(ref: Ref[IO, Counter])

  def route2(ref: Ref[IO, Counter]) =
    increment.implementedByEffect(increment => ref.update(c => c.copy(c.value + increment.step)))


 //val routes = HttpRoutes.of(routesFromEndpoints())

}
