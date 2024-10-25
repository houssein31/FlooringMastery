package dev.houssein.FlooringMastery.view;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

@Component
public class UserConsoleImpl implements UserIO{

    private final Scanner console = new Scanner(System.in);

    @Override
    public void print(String msg) {
        System.out.println(msg);
    }

    @Override
    public double readDouble(String prompt) {
        System.out.print(prompt);
        return Double.parseDouble(console.nextLine());
    }

    @Override
    public double readDouble(String prompt, double min, double max) {
        double result;
        do {
            result = readDouble(prompt);
        } while (result < min || result > max);
        return result;
    }

    @Override
    public float readFloat(String prompt) {
        while (true) {
            try {
                return Float.parseFloat(this.readString(prompt));
            } catch (NumberFormatException e) {
                this.print("Input error. Please try again.");
            }
        }
    }

    @Override
    public float readFloat(String prompt, float min, float max) {
        float result;
        do {
            result = readFloat(prompt);
        } while (result < min || result > max);

        return result;
    }

    @Override
    public int readInt(String prompt, boolean nullAllowed) {
        boolean invalidInput = true;
        int num = 0;
        while (invalidInput) {
            try {
                String stringValue = this.readString(prompt);
                if(nullAllowed && stringValue.equals("")){
                    return -1;
                }
                // Get the input line, and try and parse
                num = Integer.parseInt(stringValue); // if it's 'bob' it'll break
                invalidInput = false; // or you can use 'break;'
            } catch (NumberFormatException e) {
                this.print("Input error. Please try again.");
            }
        }
        return num;

    }

    @Override
    public int readInt(String prompt, int min, int max, boolean nullAllowed) {
        int result;
        do {
            result = readInt(prompt, nullAllowed);
        } while (result < min || result > max);

        return result;
    }

    @Override
    public long readLong(String prompt) {
        while (true) {
            try {
                return Long.parseLong(this.readString(prompt));
            } catch (NumberFormatException e) {
                this.print("Input error. Please try again.");
            }
        }
    }

    @Override
    public long readLong(String prompt, long min, long max) {
        long result;
        do {
            result = readLong(prompt);
        } while (result < min || result > max);

        return result;
    }

    @Override
    public String readString(String prompt) {
        System.out.println(prompt);
        return console.nextLine();
    }

    @Override
    public BigDecimal readBigDecimal(String prompt) {
        while (true) {
            try {
                BigDecimal bigD = new BigDecimal(this.readString(prompt));
                return bigD;
            } catch (NumberFormatException e) {
                this.print("Input error. Please try again.");
            }
        }
    }

    @Override
    public BigDecimal readBigDecimalForEdit(String prompt) {
        while (true) {
            try {
                BigDecimal bigD = new BigDecimal(this.readString(prompt));
                return bigD;
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }

    @Override
    public LocalDate readDate(String prompt) {
        while (true) {
            try {
                String dateString = this.readString(prompt);
                return LocalDate.parse(dateString); // Assuming date format is ISO-8601 (yyyy-MM-dd)
            } catch (DateTimeParseException e) {
                this.print("Invalid date format. Please enter date in yyyy-MM-dd format.");
            }
        }
    }
}
