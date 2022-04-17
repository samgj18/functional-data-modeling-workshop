
sealed abstract case class Record private (name: String, year: Int)

object Record {
  def apply(name: String, year: Int): Record = new Record(name, year) {}
}

lazy val firstRecord = Record("John", 1990)

lazy val secondRecord = Record("John", 1990)

firstRecord == secondRecord

sealed case class Address(street: String, number: Int)
sealed case class Employee(name: String, address: Address)

val dilbert = Employee("Dilbert", Address("Baker", 221))

dilbert match {
  case Employee("Dilbert", _) => println("Found it") // Specifically matching the name
  case Employee(name, _)      => println(name)       // Matching everything else
}

dilbert match {
  case Employee(name, a @ Address(street, _)) => println(s"$name lives at $street, and $a")
}

final case class Item(name: String, desc: String, price: Double)

final case class Inventory(items: Map[String, Item]) {
  def add(item: Item): Inventory    = copy(items + (item.name -> item))
  def remove(item: Item): Inventory = copy(items = items - item.name)
}

val inventory = Inventory(Map("A" -> Item("A", "B", 1.0), "B" -> Item("B", "C", 2.0)))
println(inventory)