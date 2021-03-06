package fakeit

import java.time.{LocalDateTime, OffsetDateTime, ZoneId, ZonedDateTime}
import java.util.Date

import scala.collection.Iterable
import scala.util.Random

trait Faker[T] {
  def getNext: T
}

object Faker {
  implicit def optionFaker[T](implicit nonOptionFaker: Faker[T]) = new Faker[Option[T]] {
    override def getNext =
      if(Random.nextBoolean()) {
        Some(nonOptionFaker.getNext)
      } else {
        None
      }
  }

  implicit def iterableFaker[T](implicit nonOptionFaker: Faker[T]) = new Faker[Iterable[T]] {
    override def getNext = (0 to Random.nextInt(100)).map(_ => nonOptionFaker.getNext)
  }

  implicit def listFaker[T](implicit nonOptionFaker: Faker[T]) = new Faker[List[T]] {
    override def getNext = (0 to Random.nextInt(100)).map(_ => nonOptionFaker.getNext).toList
  }

  implicit def seqFaker[T](implicit nonOptionFaker: Faker[T]) = new Faker[Seq[T]] {
    override def getNext = listFaker(nonOptionFaker).getNext
  }

  implicit val intFaker: Faker[Int] =  new Faker[Int] {
    override def getNext = Random.nextInt
  }

  implicit val stringFaker: Faker[String] =  new Faker[String] {
    override def getNext = (1 to 40).map(_ => Random.nextPrintableChar()).mkString.filter(_.isLetterOrDigit).take(10)
  }

  implicit val bigDecimalFaker: Faker[BigDecimal] = new Faker[BigDecimal] {
    override def getNext: BigDecimal = BigDecimal(Random.nextDouble())
  }

  implicit val booleanFaker: Faker[Boolean] = new Faker[Boolean] {
    override def getNext: Boolean = Random.nextBoolean()
  }

  implicit val byteFaker: Faker[Byte] = new Faker[Byte] {
    override def getNext: Byte = Random.nextInt(Byte.MaxValue).toByte
  }

  implicit val shortFaker: Faker[Short] = new Faker[Short] {
    override def getNext: Short = Random.nextInt(Short.MaxValue).toShort
  }

  implicit val longFaker: Faker[Long] = new Faker[Long] {
    override def getNext: Long = Random.nextLong()
  }

  implicit val floatFaker: Faker[Float] = new Faker[Float] {
    override def getNext: Float = Random.nextFloat()
  }

  implicit val doubleFaker: Faker[Double] = new Faker[Double] {
    override def getNext: Double = Random.nextDouble()
  }

  implicit val localDateTimeFaker: Faker[LocalDateTime] = new Faker[LocalDateTime] {
    override def getNext: LocalDateTime = LocalDateTime.now.plusSeconds(Random.nextLong() % 999999999L)
  }

  implicit val offsetDateTimeFaker: Faker[OffsetDateTime] = new Faker[OffsetDateTime] {
    override def getNext: OffsetDateTime = OffsetDateTime.now.plusSeconds(Random.nextLong() % 999999999L)
  }

  implicit val zonedDateTimeFaker: Faker[ZonedDateTime] = new Faker[ZonedDateTime] {
    override def getNext: ZonedDateTime = ZonedDateTime.now.plusSeconds(Random.nextLong() % 999999999L)
  }

  implicit val dateFaker: Faker[Date] = new Faker[Date] {
    override def getNext: Date = Date.from(
      LocalDateTime.now.plusSeconds(Random.nextLong() % 999999999L).atZone(ZoneId.systemDefault()).toInstant)
  }
}
