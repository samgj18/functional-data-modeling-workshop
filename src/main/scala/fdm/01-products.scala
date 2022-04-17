package fdm

/**
 * Scala supports tuples, which are a generic way to store two or more pieces of information at
 * the same time. For example, the tuple `("Alligator", 42)` stores both a string, as the first
 * component in the tuple, and an integer, as the second component in the tuple.
 *
 * Tuples come with lots of free functionality, including constructors, deconstructors, equality
 * and hash code, string representation for debugging, and a copy method.
 *
 * The elements of a tuple are accessed "by index". For example, `("a", 1)._1` retrieves the first
 * element of the tuple, which is the string `"a"`.
 *
 * Tuples are immutable: once constructed, they cannot change. However, there are simple ways to
 * created a new tuple from an existing tuple, in which some element has been "changed".
 *
 * Scala supports tuples of any "arity": that is, tuples can have as many elements as necessary.
 *
 * Tuples are examples of "anonymous products": they are types formed using "product composition"
 * of other types.
 *
 * val x: Int <- This is called type abscription or type annotation = 1 ; x is an element of type Int (the set of all integers)
 */
object tuples {

  /**
   * EXERCISE 1
   *
   * Using both a type alias, and Scala's tuple types, construct a type called `Person` that can
   * hold both the name of a `Person` (as a `String`), together with the age of the `Person` (as an
   * `Int`).
   */
  type Person = (String, Int)

  /**
   * EXERCISE 2
   *
   * Using the `Person` type alias that you just created, construct a value that has type `Person`,
   * whose name is "Sherlock Holmes", and whose age is 42.
   */
  lazy val sherlockHolmes: Person = ("Sherlock Holmes", 42)

  /**
   * EXERCISE 3
   *
   * Using both a type alias, and Scala's tuple types, construct a type called `CreditCard` that can
   * hold a credit card number (as a `String`), a credit card expiration date (as a
   * `java.time.YearMonth`), a full name (as a `String`), and a security code (as a `Short`).
   */
  type CreditCard = (String, java.time.YearMonth, String, Short)

  /**
   * EXERCISE 4
   *
   * Using the `CreditCard` type alias that you just created, construct a value that has type
   * `CreditCard`, with details invented by you.
   */
  lazy val creditCard: CreditCard = ("1234-5678-9012-3456", java.time.YearMonth.now(), "John Doe", 123)
}

/**
 * Scala supports case classes, which are a generic way to store two or more pieces of
 * information at the same time, where each piece of information can have a user-defined label
 * associated with it. Case classes are nicer than tuples because the elements of the tuple can
 * be accessed by identifiers, instead of by indices. Like tuples, case classes come with lots of
 * free functionality, including constructors, deconstructors, equality and hash code, string
 * representation for debugging, and a copy method.
 *
 * Case classes can be thought of as "records" that have zero or more "fields"; or they can be
 * thought of as defining "tables" that have zero or more "columns".
 *
 * Case classes are immutable: once constructed, they cannot change. However, there are simple ways
 * to created a new value from an existing value, in which some field has been "changed".
 *
 * Case classes are examples of "labeled products": they are types formed using "product
 * composition"  of other types, where each term of the product can be accessed by a user-defined
 * (unique) label.
 *
 */
object case_class_basics {

  /**
   * EXERCISE 1
   *
   * Using case classes, construct a type called `Person` that can hold both the name of a `Person`
   * (as a `String` stored in a field called `name`), together with the age of the `Person` (as an
   * `Int` stored in a field called `age`).
   */
  final case class Person(name: String, age: Int) // Should always be prefixed with "final"

  // final case class Record(name: String, year: Int)

  // case class also provides equals, hashCode, toString, copy, etc. equals & hashCode are consistent
  // and operate term-by-term, whereas toString is a string representation of the whole object.

  // Why should we use the final keyword?
  // The definition of equals and hashCode is inherited from the parent class, therefore, they would be
  // poorly defined if the class was not final.
  // class OtherPerson extends Person(name, age) <-- this is not allowed

  // The class or trait keyword is the one who introduces Nominal Type. Which means that the class or
  // trait is the "type". That is, The type of a Person object is Person, and the type
  // of a Record is a Record (even though the fields are isomorphic).

  /**
   * EXERCISE 2
   *
   * Using the `Person` case class that you just created, construct a value that has type `Person`,
   * whose name is "Sherlock Holmes", and whose age is 42.
   */
  lazy val sherlockHolmes: Person = Person("Sherlock Holmes", 42)

  /**
   * EXERCISE 3
   *
   * Using case classes, construct a type called `CreditCard` that can hold a credit card number (as
   * a `String` stored in a field called `number`), a credit card expiration date (as a
   * `java.time.YearMonth` stored in a field called `expDate`), a full name (as a `String` stored in
   * a field called `name`), and a security code (as a `Short` in a field called `securityCode`).
   */
  final case class CreditCard(number: String, expDate: java.time.YearMonth, name: String, securityCode: Short)

  /**
   * EXERCISE 4
   *
   * Using the `CreditCard` case class that you just created, construct a value that has type
   * `CreditCard`, with details invented by you.
   */
  lazy val creditCard: CreditCard = CreditCard("1234-5678-9012-3456", java.time.YearMonth.now(), "John Doe", 123)
}

/**
 * Scala's case classes come equipped with useful functionality that all "data classes" should
 * have. In particular, they have equality and hash code built in, and it does exactly what you
 * would expect: operate on the fields of the case class.
 *
 * In addition, case classes have built in copy methods, which can be used to create new values
 * that are modified in some way with respect to an original value.
 */
object case_class_utilities {
  final case class Person(name: String, age: Int)
  // How to remove the copy method?
  // 1. Make the class sealed abstract
  // 2. Make the constructor private
  // 3. Make an apply method that returns a new instance of the class
  // 4. Since is an abstract class, we can't instantiate it directly. We need to use the apply method and return a new instance.

  sealed abstract case class Record private (name: String, year: Int)

  object Record {
    def apply(name: String, year: Int): Record = new Record(name, year) {}
  }
  Record("John", 1990)

  /**
   * EXERCISE 1
   *
   * Construct and compare two values of type `Person` to see if they are equal to each other.
   * Compare using the `==` method, which is available on every value of type `Person`.
   */
  lazy val comparison: Boolean = Person("John Doe", 42) == Person("John Doe", 42) // The double equals
  // will check if every term of the Person object is equal to the other. To check for equality, based on
  // memory address, use the eq keyword, Person("John Doe", 42).eq(Person("John Doe", 42)) // false.

  /**
   * EXERCISE 2
   *
   * Construct and compute the hash codes of two values of type `Person` to see if they are equal to
   * each other. By law, if two values are equal, their hash codes must also be equal. Compute the
   * hash code of the `Person` values by calling the `hashCode` method, which is available on every
   * value of type `Person`.
   */
  lazy val hashComparison: Boolean = Person("John Doe", 42).hashCode() == Person("John Doe", 42).hashCode() // true.

  /**
   * EXERCISE 3
   *
   * Create a copy of the `sherlockHolmes` value, but with the age changed to be 10 years less than
   * it is currently. Create the copy using the `copy` method, which is available on every value
   * of type `Person`. Note that using named parameters, you need only specify the field you wish
   * to change in the copy operation.
   */
  lazy val sherlockHolmes: Person = Person("Sherlock Holmes", 42)
  lazy val youngerHolmes: Person  = sherlockHolmes.copy(age = sherlockHolmes.age - 10)
}

/**
 * Both tuples and case classes can be used in pattern matching. Pattern matching can be used to
 * pull out fields from the products and store them in named variables, as well as to selectively
 * match for specific patterns of information stored in a value.
 */
object product_patterns {
  final case class Person(name: String, age: Int)

  lazy val sherlockHolmes: Person = Person("Sherlock Holmes", 42)

  /**
   * EXERCISE 1
   *
   * In this pattern match on the value `sherlockHolmes`, the name is being extracted and stored
   * into a variable called `name`, while the age is being extracted and stored into a variable
   * called `age`. On the right hand side of the arrow (`=>`), use a Scala `println` statement to
   * print out the name and the age of the specified person.
   */
  def example1 =
    sherlockHolmes match {
      case Person(name, age) => println(s"$name is $age years old")
    }

  /**
   * EXERCISE 2
   *
   * Pattern match on this tuple and extract out the name of the product into a variable called
   * `name`, and the price of the product into a variable called `price`. Then use a Scala
   * `println` statement to print out the name and price of the product.
   */
  def example2 =
    ("Suitcase", 19.95)

  example2 match {
    case (name, price) => println(s"$name costs $price")
  }

  final case class Employee(name: String, address: Address)
  final case class Address(street: String, number: Int)

  val dilbert = Employee("Dilbert", Address("Baker", 221))

  /**
   * EXERCISE 3
   *
   * Pattern match on `dilbert` and extract out and print the address number. This will involve
   * using a nested pattern match.
   */
  dilbert match {
    // Nested pattern match
    case Employee(name, Address(street, number)) => println(s"$name lives at $street $number")

    // Shallow pattern match
    /* case Employee(name, address) =>
      address match {
        case Address(street, number) => println(s"$name lives at $street $number")
      } */
  }

  /**
   * EXERCISE 4
   *
   * Pattern matches can contain literal values, in which case those slots in the pattern are
   * matched using the `equals` method of the value. Pattern match on `dilbert` again, but this
   * time, with two cases: the case where the name is equal to `"Dilbert"`, and all other cases.
   * Print out the name in each case. Note the ordering of case evaluation, which proceeds from top
   * to bottom.
   */
  dilbert match {
    // There's a linear order of evaluation. The first case is evaluated first, and if it matches,
    // the second case is not evaluated.
    case Employee("Dilbert", _) => println("Found it") // Specifically matching the name
    case Employee(name, _)      => println(name)       // Matching everything else
  }

  // Pattern matching is also used in things like flatMap, map, among others and the reason behind it
  // is that Scala adapt the pattern block it defines functions from the input type to the output type.
  /** e.g.
    List(1,2).map{
      case 1 => "one"
      case 2 => "two"
    }

    Here the input is 1 and 2, and the output is "one" and "two".
    So Scala defines two functions from Int (1, 2) to String("one", "two").
   */
  /**
   * EXERCISE 5
   *
   * Every case of a pattern match may contain a conditional expression, in which case the pattern
   * is matched only if both the base pattern matches, and the boolean expression evaluates to true.
   *
   * Pattern match on `dilbert` again and have two cases: the first one matches any address name
   * that starts with the string `"B"`, and a catch all case that matches all patterns. In both
   * cases, print out the name of the street.
   */
  dilbert match {
    // There's a linear order of evaluation. The first case is evaluated first, and if it matches,
    // the second case is not evaluated.
    case Employee(_, Address(street, _)) if street.startsWith("B") => println(street) // Conditional pattern match
    case Employee(_, Address(street, _))                           => println(street)
  }

  // Same as above, but with a more concise syntax
  dilbert match {
    // There's a linear order of evaluation. The first case is evaluated first, and if it matches,
    // the second case is not evaluated.
    case Employee(_, Address(street, _)) => if (street.startsWith("B")) println(street) else println("Not found")
  }

  /**
   * EXERCISE 6
   *
   * Any piece of a pattern match may be captured and placed into a new variable by using the as-
   * pattern syntax `x @ ...`, where `x` is any legal variable name. The variable introduced by an
   * as-pattern may be used both in any conditional, or inside the case of the pattern match.
   *
   * In this exercise, pattern match on dilbert, and give a name `a` to the inner `Address`, and
   * then print out that `a` in the case expression.
   */
  dilbert match {
    case Employee(name, a @ Address(street, _)) => println(s"$name lives at $street, and $a")
  }

  /**
   * EXERCISE 7
   *
   * Using the `|` symbol, you can match against two alternatives, providing neither introduces
   * new variables. In the context of case classes, this symbol provides a nice way to look for
   * one among a small number of constant values.
   *
   * In this exercise, match for the name "Dilbert" or the name "dilbert", and print out the
   * address of the employee.
   */
  dilbert match {
    case Employee("Dilbert" | "dilbert", address) =>
      println(s"Dilbert lives at ${address.street}") // It is not possible to use "|" in pattern fragments
    // introduce variables. Meaning you can only use if there are no variables only literals.

    case Employee(name, address) =>
      println(s"$name lives at ${address.street}")

  }

  // Destructuring assignment | We're simultaneously destructuring and assigning to name and address
  val Employee(name, address) = dilbert

  // Also works for tuples
  val (first, second) = ("foo", "bar")
}

/**
 * Scala's case classes can be generic, which means the types defined by case classes may have
 * generic type parameters. This allows building general-purpose and versatile data structures
 * that can have different types "plugged" into them in different contexts.
 */
object case_class_generics {

  /**
   * EXERCISE 1
   *
   * Convert this non-generic case class into a generic case class, by introducing a new type
   * parameter, called `Payload`, and use this type parameter to define the type of the field called
   * `payload` already defined inside the case class.
   */
  // Generic are also called parametric polymorphic data types.
  final case class Event[A](id: String, name: String, time: java.time.Instant, payload: A)

  /**
   * EXERCISE 2
   *
   * Construct a type alias called `EventString`, which is an `Event` but with a `String` payload.
   */
  type EventString = Event[String]

  /**
   * EXERCISE 2
   *
   * Construct an event that has a payload type of `Int`.
   */
  lazy val eventInt: Event[Int] = Event("id", "name", java.time.Instant.now(), 1)

  /**
   * EXERCISE 3
   *
   * Convert this non-generic class into a generic class, by introducing a new type parameter,
   * called `Body`, which represents the body type of the request, and use this type parameter to
   * define the type of the field called `body` already defined inside the case class.
   */
  final case class Request[A](body: Event[A], sender: String)
}
