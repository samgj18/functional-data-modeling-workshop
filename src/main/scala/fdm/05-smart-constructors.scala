package fdm

import java.net.Inet4Address
import java.security.Timestamp
import java.time.Instant

object newtypes {

  // Nominal typing

  /**
   * Constructors for the data type `always` belong to the companion object.
   * Whereas methods that operate on the data type belong to the class.
   *
   */
  final case class UserId(value: Int) {
    override def toString: String = s"UserId($value)"
  }

  object UserId {
    def fromString(s: String): Option[UserId] = ???
  }

  final case class UserName(value: String)

  final case class UserEmail(value: String)

  final case class User(id: UserId, name: UserName, email: UserEmail)

  // Wrapper types will have performance implications.
  // -- By extending AnyVal, we can avoid the boxing and unboxing overhead (but it is not recommended).
  // Example:
  final case class UserId2(value: Int) extends AnyVal

  // The bottom line is: In most applications the overhead won't matter. And if it does, there
  // are workarounds for every situation but Long.

}

/**
 * Sometimes we don't want to take the time to model data precisely. For example, we might want to
 * model an email address with a string, even though most strings are not valid email addresses.
 *
 * In such cases, we can save time by using a smart constructor, which lets us ensure we model
 * only valid data, but without complicated data types.
 */
object smart_constructors {

  sealed trait AccountActivity
  object AccountActivity {
    // start <= end
    final case class LoanGranted(start: java.time.Instant, end: java.time.Instant) extends AccountActivity
  }

  // This is how we could achieve precision with ADTs.
  // Very costly and it may not pay for itself.
  /*sealed trait CreditCard {
    def number: String
  }

  object CreditCard {
    // 12 - 18 digits (American Express, MasterCard, Visa), starting with 4 or 5
    final case class Visa(value: VisaDigits) extends CreditCard {
      def number: String = ???
    }
  }

  final case class VisaDigits(
      d1: Digit,
      d2: Digit,
      d3: Digit,
      d4: Digit,
      d5: Digit,
      d6: Digit,
      d7: Digit,
      d8: Digit,
      d9: Digit,
      d10: Digit,
      d11: Digit,
      d12: Digit,
      extra: VisaDigitExtra
  )

  sealed trait VisaDigitExtra

  object VisaDigitExtra {
    case object None extends VisaDigitExtra

    final case class One(d1: Int) extends VisaDigitExtra

    final case class Two(d1: Int, d2: Int) extends VisaDigitExtra

    final case class Three(d1: Int, d2: Int, d3: Int) extends VisaDigitExtra

    final case class Four(d1: Int, d2: Int, d3: Int, d4: Int) extends VisaDigitExtra

    final case class Five(d1: Int, d2: Int, d3: Int, d4: Int, d5: Int) extends VisaDigitExtra

    final case class Six(d1: Int, d2: Int, d3: Int, d4: Int, d5: Int, d6: Int) extends VisaDigitExtra
  }

  sealed trait Digit {
    def value: Int

    override def toString: String = value.toString
  }

  object Digit {
    case object _0 extends Digit {
      def value: Int = 0
    }

    case object _1 extends Digit {
      def value: Int = 1
    }

    case object _2 extends Digit {
      def value: Int = 2
    }

    case object _3 extends Digit {
      def value: Int = 3
    }

    case object _4 extends Digit {
      def value: Int = 4
    }

    case object _5 extends Digit {
      def value: Int = 5
    }

    case object _6 extends Digit {
      def value: Int = 6
    }

    case object _7 extends Digit {
      def value: Int = 7
    }

    case object _8 extends Digit {
      def value: Int = 8
    }

    case object _9 extends Digit {
      def value: Int = 9
    }

  }*/

  // Smart Constructors: When you can not afford to use ADTs for precision then model your data type with another type that's too big.
  // We must use sealed abstract and private keyword to make sure we don't accidentally use the wrong constructor.
  sealed abstract case class Email private (value: String)

  // Note: Methods that modify the original data structure should be placed in the case class itself. Methods that create a new data structure should be placed in the companion object.
  object Email {
    def fromString(email: String): Option[Email] =
      // This new Email(email) {} creates an anonymous class which is a subtype of Email (because you can't create an instance of a sealed abstract case class).
      if (email.matches("""/\w+@\w+.com""")) Some(new Email(email) {}) else None
  }

  // Overriding the apply method is not enough because the copy method will still be available (because of the case class).
  // The abstract prevents the class from being instantiated and deleting the copy method.
  // By making it sealed we can't make it final therefore we have to make it sealed.

  // Never call smart constructors `apply`.
  /**
   * EXERCISE 1
   *
   * Create a smart constructor for `NonNegative` which ensures the integer is always non-negative.
   */
  sealed abstract case class NonNegative private (value: Int)

  object NonNegative {
    def fromInt(i: Int): Option[NonNegative] =
      if (i >= 0) Some(new NonNegative(i) {}) else None
  }

  /**
   * EXERCISE 2
   *
   * Create a smart constructor for `Age` that ensures the integer is between 0 and 120.
   */
  // In scala3 is simpler, all we have to do is make the constructor private.
  sealed abstract case class Age private (value: Int)
  object Age {
    def fromInt(i: Int): Option[Age] =
      Option.when(i >= 0 && i <= 120)(new Age(i) {})
  }

  /**
   * EXERCISE 3
   *
   * Create a smart constructor for password that ensures some security considerations are met.
   */
  sealed abstract case class Password private (value: String)
  object Password {
    def fromString(password: String): Either[InvalidPassword, Password] =
      if (password.matches("""/\w{8,}/""")) Right(new Password(password) {})
      else if (password.length > 20) Left(InvalidPassword.TooLong("Password is too long"))
      else if (password.length < 8) Left(InvalidPassword.TooShort("Password is too short"))
      else if (password.matches("""/\d/""")) Left(InvalidPassword.NoNumber("Password has a digit"))
      else Left(InvalidPassword.InvalidCharacters("Password contains invalid characters"))
  }

  sealed trait InvalidPassword
  object InvalidPassword {
    final case class TooShort(message: String)          extends InvalidPassword
    final case class TooLong(message: String)           extends InvalidPassword
    final case class NoNumber(message: String)          extends InvalidPassword
    final case class InvalidCharacters(message: String) extends InvalidPassword

  }

  /**
 * How to choose? Choose the smallest possible data type that allows you to distinguish between all possible failure modes.
 * Constructors:
 *  Option[A]
 *  Either[A, B]
 *  Try[A]
 *  Validated[E, A]
 */
}

object applied_smart_constructors {

  /**
   * EXERCISE 1
   *
   * Identify the weaknesses in this data type, and use smart constructors (and possibly other
   * techniques) to correct them.
   */
  sealed abstract case class AccountId private (value: String)
  sealed abstract case class AccountName private (value: String)
  sealed trait AccountBalance
  object AccountBalance {
    final case class USD(dollar: BigDecimal, cents: BigDecimal) extends AccountBalance
    final case class EUR(euro: BigDecimal, cents: BigDecimal)   extends AccountBalance
  }

  final case class BankAccount(id: AccountId, name: AccountName, balance: AccountBalance, opened: java.time.Instant)

  /**
   * EXERCISE 2
   *
   * Identify the weaknesses in this data type, and use smart constructors (and possibly other
   * techniques) to correct them.
   */
  final case class Person(age: Age, name: Name, salary: Salary)
  sealed abstract case class Age private (value: Int)
  sealed abstract case class Name private (value: String)
  sealed abstract case class Salary private (value: Double)

  /**
   * EXERCISE 3
   *
   * Identify the weaknesses in this data type, and use smart constructors (and possibly other
   * techniques) to correct them.
   */
  final case class SecurityEvent(machine: Inet4Address, timestamp: Instant, eventType: EventType)

  sealed trait EventType {
    def toInt: Int
  }
  case object PortScanning extends EventType {
    def toInt = 0
  }
  case object DenialOfService extends EventType {
    def toInt = 1
  }

  case object InvalidLogin extends EventType {
    def toInt = 2
  }
}
