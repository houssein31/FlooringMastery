package dev.houssein.FlooringMastery.view;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface UserIO {
    void print(String msg);
    double readDouble(String prompt);
    double readDouble(String prompt, double min, double max);
    float readFloat(String prompt);
    float readFloat(String prompt, float min, float max);
    int readInt(String prompt, boolean nullAllowed);
    int readInt(String prompt, int min, int max, boolean nullAllowed);
    long readLong(String prompt);
    long readLong(String prompt, long min, long max);
    String readString(String prompt);
    BigDecimal readBigDecimal(String prompt);
    BigDecimal readBigDecimalForEdit(String prompt);
    LocalDate readDate(String prompt);
}