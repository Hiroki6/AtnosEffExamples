package app.services

import org.atnos.eff.Eff

trait CommandService[R, In, Out] {
  def execute(in: In): Eff[R, Out]
}
