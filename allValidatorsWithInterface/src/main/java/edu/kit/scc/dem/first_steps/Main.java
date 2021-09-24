package edu.kit.scc.dem.first_steps;

import edu.kit.scc.dem.first_steps.validators.impl.MailAddressValidator;
import edu.kit.scc.dem.first_steps.validators.impl.PhoneNumberValidator;
import edu.kit.scc.dem.first_steps.validators.impl.RegexNumberValidator;
import java.util.Scanner;

/**
 * This class manages the communication between the user and the validator
 * classes.
 *
 * @author maximilianiKIT
 */

public class Main {

  public static void main(String[] args) {
    try {
      // Maybe a commandline parser would help: see https://stackoverflow.com/questions/367706/how-do-i-parse-command-line-arguments-in-java
      // You may take a look at jCommander.
      // Put your input(s) (of same type) in an array and process it in a loop.
      if (args == null || args.length < 1 || args.length > 3 || args[0] == null) {
        Scanner input = new Scanner(System.in);
        System.out.println("Hello, there were no/invalid arguments given. Which validator do you want to use?");
        System.out.println("If you want to know something about the arguments use '-h'.");
        System.out.println();
        System.out.println("Please enter the type of validator you wish (regexPhone, googlePhone, mail): ");
        String type;
        try {
          type = input.nextLine();
        } catch (Exception e) {
          System.out.println("No input given!");
          throw new IllegalArgumentException();
        }
        ValidatorInterface validator = null;
        switch (type) {
          case "regexPhone":
            // Asking x-times for input with different Strings may be annoying.
            // Don't repeat these lines over and over again. 
            // make it part of the interface!
            // You may also split the interface (first only validate input) 
            // the default method (see ValidatorInterface) could also be done in a generic class Validator<T extends ValidatorInterface> 
            // See also: https://docs.oracle.com/javase/tutorial/java/generics/types.html
//                        System.out.println("Please enter a German national number to validate: ");
//            String regexNumber = input.nextLine();
            validator = new RegexNumberValidator();
//                        RegexNumberValidator regexValidator = new RegexNumberValidator();
//                        regexValidator.processIsValid(new String[]{regexNumber});
            break;
          case "googlePhone":
            System.out.println("Please enter a countrycode (e.g. DE, NL, ...): ");
            String countryCode = input.nextLine();
            System.out.println("Please enter a international phone number: ");
            System.out.println("Please use only an international notation and '+' or '00' as traffic elimination digit.");
            String googleNumber = input.nextLine();
            PhoneNumberValidator googleValidator = new PhoneNumberValidator();
            googleValidator.processIsValid(new String[]{countryCode, googleNumber});
            break;
          case "mail":
            System.out.println("Please enter a mail address to validate: ");
            String mail = input.nextLine();
            MailAddressValidator mailValidator = new MailAddressValidator();
            mailValidator.processIsValid(new String[]{mail});
            break;
          default:
            System.out.println("Invalid type!");
            throw new IllegalArgumentException();
        }
        // In case of multiple inputs do it in a loop.
        do {
          validator.askForInputAndValidate();
        } while (ask for more input );
      } else if (args[0].equals("-r")) {
        RegexNumberValidator validator = new RegexNumberValidator();
        validator.processIsValid(new String[]{args[1]});
      } else if (args[0].equals("-g")) {
        PhoneNumberValidator validator = new PhoneNumberValidator();
        try {
          validator.processIsValid(new String[]{args[1], args[2]});
        } catch (ArrayIndexOutOfBoundsException e) {
          System.out.println("Not all arguments given");
          throw new IllegalArgumentException();
        }
      } else if (args[0].equals("-m")) {
        MailAddressValidator validator = new MailAddressValidator();
        validator.processIsValid(new String[]{args[1]});
      } else if (args[0].equals("-h")) {
        System.out.println("Hello, this little java script validates phone numbers and e-mail addresses.");
        System.out.println();
        System.out.println("Syntax:     java Main.java [type|-h] [input1] [input2]");
        System.out.println();
        System.out.println("There are three types of validators: '--regexPhone', '--googlePhone' and '--mail'.");
        System.out.println("    - regexPhone ('-r'): validates only national German telephone numbers");
        System.out.println("        parameter: [input1] needs a number in national format without special characters and spaces");
        System.out.println("    - googlePhone ('-g'): validates international telephone numbers");
        System.out.println("        parameters:[input1] needs a number in international format.");
        System.out.println("                   [input2] needs a country code from where you want to call.");
        System.out.println("    - mail ('-m'): validates email addresses and checks if the domain has an A or AAAA record");
        System.out.println("        parameter: [input1] needs the email address");
        System.out.println();
      }
    } catch (Exception e) {
      System.out.println("oops! An error occurred...");
      System.out.println();
      e.printStackTrace();
      throw new IllegalArgumentException();
    }

  }
}
