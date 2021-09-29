package edu.kit.scc.dem.first_steps.validators.impl;

import edu.kit.scc.dem.first_steps.validators.ValidatorInterface;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * This class validates international phone numbers by using a Google library.
 *
 * @author maximilianiKIT
 */
public class PhoneNumberValidator implements ValidatorInterface {

    final Logger log = LoggerFactory.getLogger(PhoneNumberValidator.class);
    private static final PhoneNumberUtil util = PhoneNumberUtil.getInstance();
    private final String countryCode;

    /**
     * This constructor gets the country code by parameter.
     *
     * @param countryCode is the country code from which view the library is used.
     */
    public PhoneNumberValidator(String countryCode) {
        log.debug("Set country code to {}", countryCode);
        this.countryCode = countryCode;
    }

    /**
     * This constructor reads the users input for the country code.
     *
     * @throws ValidationException if no input was given.
     */
    public PhoneNumberValidator() throws ValidationException {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter a countrycode (e.g. DE, NL, ...): ");
        try {
            this.countryCode = input.nextLine();
            log.debug("Set country code to {}", this.countryCode);
        } catch (Exception e) {
            log.error("No country code provided.");
            throw new ValidationException("No country code provided!", new ValidationException());
        }
    }

    /**
     * This method validates international phone numbers by using a Google library.
     *
     * @param input [0] - number to validate in an international format
     *              [1] - country code from where to validate the number
     * @return true if the number is valid and possible
     * @throws IllegalArgumentException if the number is not possible or/and not valid
     */
    @Override
    public boolean isValid(String input) throws ValidationException {
        if (input == null || input.length() == 0) {
            log.error("Invalid input! ");
            throw new ValidationException("Invalid or no input!", new ValidationException());
        }
        try {
            Phonenumber.PhoneNumber number = util.parseAndKeepRawInput(input, countryCode);
            PhoneNumberUtil.ValidationResult possibleResult = util.isPossibleNumberWithReason(number);
            switch (possibleResult) {
                case IS_POSSIBLE:
                    log.info("The number {} is possible.", input);
                    System.out.println("This number is possible.");
                    break;
                case IS_POSSIBLE_LOCAL_ONLY:
                    log.warn("The number {} is only possible within a certain region and does not meet all the criteria of an international number.", input);
                    break;
            }
            return util.isValidNumber(number);
        } catch (NumberParseException e) {
            switch (e.getErrorType()) {
                case INVALID_COUNTRY_CODE:
                    log.error("Invalid country code: {}", countryCode);
                    break;
                case NOT_A_NUMBER:
                    log.error("The input {} does not meet the minimum requirements of a phone number!", input);
                    break;
                case TOO_SHORT_NSN:
                case TOO_SHORT_AFTER_IDD:
                    log.error("The number {} is too short!", input);
                    break;
                case TOO_LONG:
                    log.error("The number {} is too long!", input);
                    break;
            }
            throw new ValidationException("Invalid number!", e);
        }
    }
}
