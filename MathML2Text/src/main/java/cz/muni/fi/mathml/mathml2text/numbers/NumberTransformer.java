package cz.muni.fi.mathml.mathml2text.numbers;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.muni.fi.mathml.mathml2text.converter.Strings;

/**
 * Transforms all numbers (arabic numerals) in input data to their written form.
 * Takes into consideration inflection, e.g. in square roots, powers, etc.
 * 
 * @todo fix ordinal number transformation
 * 
 * @author Maros Kucbel Sep 13, 2012, 9:54:59 PM
 */
public final class NumberTransformer {

    private final ResourceBundle bundle;
    private final Logger logger = LoggerFactory.getLogger(NumberTransformer.class);

    private NumberFormat numberFormat = NumberFormat.CARDINAL;
    
    public NumberTransformer(@Nullable final Locale locale) {
        this.bundle = ResourceBundle.getBundle("cz.muni.fi.mathml.mathml2text.numbers.numbers", locale != null ? locale : Locale.getDefault());
    }

    /** 
     * Returns resource bundle with localized messages. 
     */
    private ResourceBundle getBundle() {
        return this.bundle;
    }
    
    /**
     * Returns logger for this class.
     */
    private Logger getLogger() {
        return this.logger;
    }

    /**
     * Return current number format.
     * @return Current number format.
     */
    public NumberFormat getNumberFormat() {
        return numberFormat;
    }

    /**
     * Set current number format.
     * @param numberFormat New number format.
     */
    public void setNumberFormat(NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
    }
    
    /**
     * Converts given number to its spoken form.
     * Based on {@link #getNumberFormat() number format} converts to cardinal
     * or ordinal form.
     * @param value
     * @return 
     */
    public String transform(final String value) {
        switch (this.getNumberFormat()) {
            case CARDINAL: {
                return this.transformNumber(value);
            }
            case ORDINAL: {
                return this.transformOrdinalNumber(value);
            }
            default: {
                return Strings.EMPTY;
            }
        }
    }
    
    /**
     * Converts given number to its spoken cardinal form.
     * @param numberAsString Number.
     * @return Spoken cardinal form.
     * @throws NumberFormatException If input string does not contain valid number.
     */
    public String transformNumber(final String numberAsString) throws NumberFormatException {
        final StringBuilder builder = new StringBuilder();
        
        final String[] numbersArray = numberAsString.split("\\.");
        
        // divide number into integral and decimal parts
        String integralPartString = StringUtils.isNotBlank(numbersArray[0]) ? numbersArray[0] : "0";
        final long integralPartNumber = Long.valueOf(integralPartString);
        String decimalPartString = numbersArray.length > 1 ? numbersArray[1] : null;

        final String integralPartResult = translateNumber(integralPartNumber);
        
        
        builder.append(integralPartResult);
        builder.append(Strings.SPACE);
        if (decimalPartString != null) {
            // if number is a decimal number append decimal deliminator
            if (integralPartNumber == 1 || integralPartNumber == -1) {
                builder.append(this.getBundle().getString("POINT1"));
            } else if (Math.abs(integralPartNumber) > 1 && Math.abs(integralPartNumber) < 5) {
                builder.append(this.getBundle().getString("POINT2"));
            } else {
                builder.append(this.getBundle().getString("POINT3"));
            }
            builder.append(Strings.SPACE);
            
            // the decimal part will be spelled one number at a time
            // for example: 0.0025 -> zero point zero zero two five
            while (decimalPartString.length() > 0) {
                builder.append(this.translateNumber(Long.valueOf(decimalPartString.substring(0, 1))));
                builder.append(Strings.SPACE);
                decimalPartString = decimalPartString.substring(1);
            }
        }
        return builder.toString();
    }
    
    /**
     * Converts given number to its spoken ordinal form.
     * @param numberAsString Number.
     * @return Spoken ordinal form.
     */
    public String transformOrdinalNumber(final String numberAsString) {
        double number = 0.0;
        try {
            number = Double.parseDouble(numberAsString);
            this.getLogger().debug("Transforming ordinal number " + number);
        } catch (NumberFormatException nfe) {
            this.getLogger().error(String.format("Number [%1$s] cannot be transformed.", numberAsString));
        }
        long integralPart = (long) Math.floor(number);
        long decimalPart = 0;
        String decimal = String.valueOf(number).split("\\.")[1];
        try {
            decimalPart = Long.valueOf(decimal);
        } catch (NumberFormatException nfe) {
            this.getLogger().error("ERROR");
        }

        if (decimalPart != 0) {
            return this.transformNumber(numberAsString);
        }
        return this.translateOrdinalNumber(integralPart);
    }
    
    /**
     * Transforms given long number to string, as is its written form (1 -> one,
     * etc.)
     *
     * @param number long number
     * @return Transformed number
     */
    private String translateNumber(long number) {
        StringBuilder ret = new StringBuilder("");
        if (number == 0) {
            return this.getBundle().getString("ZERO");
        }
        if (number < 0) {
            ret.append(this.getBundle().getString("MINUS")).append(" ");
            number *= -1;
        }

        while (number > 0) {
            if (number < 1000) {
                ret.append(translateNumber1000(number, true));
                return ret.toString();
            } else if (number < 1000000) {
                if (number / 1000 < 10) {
                    ret.append(addCountDva((int) number / 1000, false));
                } else {
                    ret.append(translateNumber(number / 1000));
                }
                ret.append(" ").append(this.getBundle().getString("THOUSAND")).append(" ");
                number = number % 1000;
            } else if (number < 1000000000) {
                int i = (int) number / 1000000;
                if (i < 10) {
                    ret.append(addCountDva(i, true));
                } else {
                    ret.append(translateNumber(number / 1000));
                }

                if (i == 1) {
                    ret.append(" ").append(this.getBundle().getString("MILLION")).append(" ");
                } else if (i < 5) {
                    ret.append(" ").append(this.getBundle().getString("MILLIONS")).append(" ");
                } else {
                    ret.append(" ").append(this.getBundle().getString("MILLIONS_2")).append(" ");
                }
                number = number % 1000000;
            } else {
                throw new NumberFormatException("Number [" + number + "]is too big for NumberTransformer to convert.");
            }

        }
        return ret.toString();
    }

    /**
     * Transforms given long number lower then 1000 to string, as is its written
     * form.
     *
     * @param number long number lower then 1000
     * @param dve true ak sa ma pozuit DVA, false ak treba DVE
     * @return Transformed number
     */
    private String translateNumber1000(long number, boolean dva) {
        StringBuilder ret = new StringBuilder("");
        while (number > 0) {
            if (number < 10) {
                switch ((int) number) {
                    case 0:
                        break;
                    case 1:
                        ret.append(this.getBundle().getString("ONE"));
                        break;
                    case 2:
                        ret.append(this.getBundle().getString("TWO_1"));
                        break;
                    case 3:
                        ret.append(this.getBundle().getString("THREE"));
                        break;
                    case 4:
                        ret.append(this.getBundle().getString("FOUR"));
                        break;
                    case 5:
                        ret.append(this.getBundle().getString("FIVE"));
                        break;
                    case 6:
                        ret.append(this.getBundle().getString("SIX"));
                        break;
                    case 7:
                        ret.append(this.getBundle().getString("SEVEN"));
                        break;
                    case 8:
                        ret.append(this.getBundle().getString("EIGHT"));
                        break;
                    case 9:
                        ret.append(this.getBundle().getString("NINE"));
                        break;
                    default:
                        break;
                }
                break;
            } else if (number < 20) {
                switch ((int) number) {
                    case 10:
                        ret.append(this.getBundle().getString("TEN"));
                        break;
                    case 11:
                        ret.append(this.getBundle().getString("ELEVEN"));
                        break;
                    case 12:
                        ret.append(this.getBundle().getString("TWELVE"));
                        break;
                    case 13:
                        ret.append(this.getBundle().getString("THIRTEEN"));
                        break;
                    case 14:
                        ret.append(this.getBundle().getString("FOURTEEN"));
                        break;
                    case 15:
                        ret.append(this.getBundle().getString("FIFTEEN"));
                        break;
                    case 16:
                        ret.append(this.getBundle().getString("SIXTEEN"));
                        break;
                    case 17:
                        ret.append(this.getBundle().getString("SEVENTEEN"));
                        break;
                    case 18:
                        ret.append(this.getBundle().getString("EIGHTEEN"));
                        break;
                    case 19:
                        ret.append(this.getBundle().getString("NINETEEN"));
                        break;
                    default:
                        break;
                }
                break;
            } else if (number < 100) {
                int i = (int) (number / 10);
                switch (i) {
                    case 0:
                        break;
                    case 2: {
                        ret.append(this.getBundle().getString("TWENTY"));
                        break;
                    }
                    case 3: {
                        ret.append(this.getBundle().getString("THIRTY"));
                        break;
                    }
                    case 4: {
                        ret.append(this.getBundle().getString("FOURTY"));
                        break;
                    }
                    case 5: {
                        ret.append(this.getBundle().getString("FIFTY"));
                        break;
                    }
                    case 6: {
                        ret.append(this.getBundle().getString("SIXTY"));
                        break;
                    }
                    case 7: {
                        ret.append(this.getBundle().getString("SEVENTY"));
                        break;
                    }
                    case 8: {
                        ret.append(this.getBundle().getString("EIGHTY"));
                        break;
                    }
                    case 9: {
                        ret.append(this.getBundle().getString("NINETY"));
                        break;
                    }
                    default:
                        break;
                }
                ret.append(" ");
                number = number % 10;
            } else if (number < 1000) {
                ret.append(addCountDva((int) number / 100, false));
                ret.append(this.getBundle().getString("HUNDRED")).append(" ");
                number = number % 100;
            }
        }
        return ret.toString();
    }

    /**
     * Transforms given number lower then 10 to string. Used with
     *
     * @param number Number lower then 10
     * @param dva
     * @return
     */
    private String addCountDva(int number, boolean dva) {

        switch (number) {
            case 0:
            case 1:
                return "";
            case 2:
                if (dva) {
                    return this.getBundle().getString("TWO_1");
                } else {
                    return this.getBundle().getString("TWO_2");
                }
            case 3:
                return this.getBundle().getString("THREE");
            case 4:
                return this.getBundle().getString("FOUR");
            case 5:
                return this.getBundle().getString("FIVE");
            case 6:
                return this.getBundle().getString("SIX");
            case 7:
                return this.getBundle().getString("SEVEN");
            case 8:
                return this.getBundle().getString("EIGHT");
            case 9:
                return this.getBundle().getString("NINE");
            default:
                return null;
        }
    }

    /**
     * Transforms given long ordinal number to string, as is its written form (1
     * -> first, etc.)
     *
     * @param number long ordinal number
     * @return Transformed number
     */
    public String translateOrdinalNumber(long inputNumber) {
        StringBuilder ret = new StringBuilder("");
        long number = 0;
        if (inputNumber < 100) {
            number = inputNumber;
        } else if ((inputNumber % 100) != 0) {
            number = inputNumber % 100;
            ret.append(translateNumber(inputNumber - number));
        } else if (inputNumber % 1000 != 0) {
            ret.append(translateNumber(inputNumber - inputNumber % 1000));
            ret.append(addCountDva(((int) (inputNumber % 1000) / 100), false));
            ret.append(this.getBundle().getString("HUNDRED2"));
        } else if (inputNumber % 10000 != 0) {
            ret.append(translateNumber(inputNumber / 1000)).append(" ");
            ret.append(this.getBundle().getString("THOUSAND2"));
        } else if (inputNumber % 10000000 != 0) {
            ret.append(translateNumber(inputNumber / 1000000)).append(" ");
            ret.append(this.getBundle().getString("MILLION2"));
        }
        while (number > 0) {
            if (number < 10) {
                switch ((int) number) {
                    case 0:
                        break;
                    case 1:
                        ret.append(this.getBundle().getString("ONE2"));
                        break;
                    case 2:
                        ret.append(this.getBundle().getString("TWO2"));
                        break;
                    case 3:
                        ret.append(this.getBundle().getString("THREE2"));
                        break;
                    case 4:
                        ret.append(this.getBundle().getString("FOUR2"));
                        break;
                    case 5:
                        ret.append(this.getBundle().getString("FIVE2"));
                        break;
                    case 6:
                        ret.append(this.getBundle().getString("SIX2"));
                        break;
                    case 7:
                        ret.append(this.getBundle().getString("SEVEN2"));
                        break;
                    case 8:
                        ret.append(this.getBundle().getString("EIGHT2"));
                        break;
                    case 9:
                        ret.append(this.getBundle().getString("NINE2"));
                        break;
                    default:
                        break;
                }
                break;
            } else if (number < 20) {
                switch ((int) number) {
                    case 10:
                        ret.append(this.getBundle().getString("TEN2"));
                        break;
                    case 11:
                        ret.append(this.getBundle().getString("ELEVEN2"));
                        break;
                    case 12:
                        ret.append(this.getBundle().getString("TWELVE2"));
                        break;
                    case 13:
                        ret.append(this.getBundle().getString("THIRTEEN2"));
                        break;
                    case 14:
                        ret.append(this.getBundle().getString("FOURTEEN2"));
                        break;
                    case 15:
                        ret.append(this.getBundle().getString("FIFTEEN2"));
                        break;
                    case 16:
                        ret.append(this.getBundle().getString("SIXTEEN2"));
                        break;
                    case 17:
                        ret.append(this.getBundle().getString("SEVENTEEN2"));
                        break;
                    case 18:
                        ret.append(this.getBundle().getString("EIGHTEEN2"));
                        break;
                    case 19:
                        ret.append(this.getBundle().getString("NINETEEN2"));
                        break;
                    default:
                        break;
                }
                break;
            } else if (number < 100) {
                int i = (int) (number / 10);
                switch (i) {
                    case 0:
                        break;
                    case 2: {
                        ret.append(this.getBundle().getString("TWENTY2"));
                        break;
                    }
                    case 3: {
                        ret.append(this.getBundle().getString("THIRTY2"));
                        break;
                    }
                    case 4: {
                        ret.append(this.getBundle().getString("FOURTY2"));
                        break;
                    }
                    case 5: {
                        ret.append(this.getBundle().getString("FIFTY2"));
                        break;
                    }
                    case 6: {
                        ret.append(this.getBundle().getString("SIXTY2"));
                        break;
                    }
                    case 7: {
                        ret.append(this.getBundle().getString("SEVENTY2"));
                        break;
                    }
                    case 8: {
                        ret.append(this.getBundle().getString("EIGHTY2"));
                        break;
                    }
                    case 9: {
                        ret.append(this.getBundle().getString("NINETY2"));
                        break;
                    }
                    default:
                        break;
                }
                number = number % 10;
            }
        }
        return ret.toString();
    }
}
