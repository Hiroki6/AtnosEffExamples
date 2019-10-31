package examples.monads

object OptionComposition {
  def hogeOption(a: Int): Option[Int] = Some(a)

  hogeOption(1).flatMap(a => hogeOption(a).map(b => a + b))
  // Some(2)

  for {
    a <- hogeOption(1)
    b <- hogeOption(a)
  } yield (a + b)
  // Some(2)
}
