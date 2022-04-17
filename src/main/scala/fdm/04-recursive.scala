package fdm

/**
 * Scala data types constructed from enums and case classes may be *recursive*: that is, a top-
 * level definition may contain references to values of the same type.
 */
object recursive {

  // Recursive data types allows the user to create unbounded trees of data.
  sealed trait List[+A]

  object List {
    case object Nil extends List[Nothing]

    case class Cons[+A](head: A, tail: List[A]) extends List[A]

    case class ::[+A](head: A, tail: List[A]) extends List[A]
  }

  /**
   * EXERCISE 1
   *
   * Create a recursive data type that models a user of a social network, who has friends; and
   * whose friends may have other friends, and so forth.
   */
  // We do () => A to indicate that the type parameter A is lazy.
  // Is cyclic if A is a recursive type.
  // Is acyclic if A is a non-recursive type.
  final case class User(name: String, friends: () => Set[User])

  /**
   * EXERCISE 2
   *
   * Create a recursive data type that models numeric operations on integers, including addition,
   * multiplication, and subtraction.
   */
  sealed trait NumericExpression

  object NumericExpression {
    final case class Literal(value: Int) extends NumericExpression

    final case class Addition(left: NumericExpression, right: NumericExpression) extends NumericExpression

    final case class Multiplication(left: NumericExpression, right: NumericExpression) extends NumericExpression

    final case class Subtraction(left: NumericExpression, right: NumericExpression) extends NumericExpression
  }

  /**
   * EXERCISE 3
   *
   * Create a `EmailTrigger` recursive data type which models the conditions in which to trigger
   * sending an email. Include common triggers like on purchase, on shopping cart abandonment, etc.
   */
  sealed trait EmailTrigger

  object EmailTrigger {
    case object OnPurchase extends EmailTrigger

    case object OnShoppingCartAbandonment extends EmailTrigger

    final case class Both(left: EmailTrigger, right: EmailTrigger) extends EmailTrigger
  }
}

/**
 * As Scala is an eager programming language, in which expressions are evaluated eagerly, generally
 * from left to right, top to bottom, the tree-like data structures created with case classes and
 * enumerations do not contain cycles. However, with some work, you can model cycles. Cycles are
 * usually for fully general-purpose graphs.
 */
object cyclically_recursive {
  // If we don't do () => A, we will get null as at creation time, vals are filled with nulls.
  // def snake will give stack overflow error.
  final case class Snake(food: () => Snake)

  /**
   * EXERCISE 1
   *
   * Create a snake that is eating its own tail. In order to do this, you will have to use a
   * `lazy val`.
   */
  lazy val snake: Snake = Snake(() => snake)

  /**
   * EXERCISE 2
   *
   * Create two employees "Tim" and "Tom" who are each other's coworkers. You will have to change
   * the `coworker` field from `Employee` to `() => Employee` (`Function0`), also called a "thunk",
   * and you will have to use a `lazy val` to define the employees.
   */
  final case class Employee(name: String, coworker: () => Employee)

  lazy val tim: Employee = Employee("Tim", () => tom)
  lazy val tom: Employee = Employee("Tom", () => tim)

  /**
   * EXERCISE 3
   *
   * Develop a List-like recursive structure that is sufficiently lazy, it can be appended to
   * itself.
   */
  sealed trait LazyList[+A] extends Iterable[A]

  // A DAG is a directed acyclic graph, they are trees.
  // A recursive structure that is not lazy is an Acyclic Graph (AG).
  // A recursive structure that is lazy COULD be a Cyclic Graph (CG).
  object LazyList {
    final case class ::[A](h: () => A, t: () => LazyList[A]) extends LazyList[A] {
      def iterator: Iterator[A] = Iterator(h()) ++ t().iterator
    }

    case object Nil extends LazyList[Nothing] {
      override def iterator: Iterator[Nothing] = Iterator.empty
    }

    def apply[A](el: A): LazyList[A] = ::(() => el, () => Nil)

    // The syntax `=>` means a "lazy parameter". Such parameters are evaluated wherever they are
    // referenced "by name".

    // Whatever parameter you pass "by name"
    def concat[A](left: => LazyList[A], right: => LazyList[A]): LazyList[A] =
      left match {
        case Nil    => right
        case h :: t => ::(h, () => concat(t(), right))
      }
  }

  // The syntax `=>` means a "lazy parameter". Such parameters are evaluated wherever they are referenced "by name".
  // It is called by name because if I refer to the name of the variable on the code at any point in time, it will be evaluated.
  def byName(byName: => Any): Unit = ()
  // byName(throw new Exception) // Nothing will happen.

  // Before scala calls the function it computes the value of the parameter.
  def byValue(byValue: Any): Unit = ()
  // byValue(throw new Exception) Will throw an exception before the function is called.

  lazy val infiniteList: LazyList[Int] = LazyList.concat(LazyList(1), infiniteList)
}
