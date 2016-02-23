package models.modelsunscanned

import slick.driver.H2Driver.api._

/**
  * Created by trodrigues on 2/15/16.
  */
abstract class TableWithId[T](tableTag: Tag, tableName: String) extends Table[T](tableTag,tableName) {
  def id: Rep[Long]
}
