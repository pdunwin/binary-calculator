package pdunwin.binarycalculator;

import android.util.Log;

/**
 * Created by Paul Unwin on 2/27/2016.
 */
public class CalcRegister {

    // Primarily just a value in memory
    private int value;
    // Need to know whether or not it "exists" yet
    private boolean isEntered;

    // Flag whether or not the register contains an answer
    private boolean answerFlag;

    // the binary version of the value
    String binaryString;

    // can be declared with no initial value
    public CalcRegister(){

    }

    // can be declared with an initial value
    public CalcRegister(int i){
        this.setValue(i);
    }

    // can be declared with an initial value and an isEntered setting.
    public CalcRegister(int i, boolean b){
        value = i;
        isEntered = b;
    }

    // You can construct it using the value of another register
    public CalcRegister(CalcRegister other){
        value = other.value;
    }

    // Getter for value
    public int getValue(){
        return value;
    }

    // Setter for value
    public String setValue(int i){
        // Limits the value to numbers of 17 binary digits in length or less
        // 131071 is 11111111111111111
        // 2^-15 is 0.000000000000001
        // -2^-15 is -0.00000000000001
        // -65535 is -1111111111111111

        // convert the number into a binary string
        String tempString = generateBinaryString(i);
        // if the binary string is less than or equal to 17 characters
        if (tempString.length() <= 16) {
        //if (!(i <= 131071 && i >= Math.pow(2,-15) && i <= Math.pow(-2,-14) && i >= -65535)) {
            // allow the register value to be set as the number
            this.value = i;
            // return a string of the number as an indication of success
            return tempString;
        } else{
            // return an error message.
            return "TOO LONG";
        }


    }

    // marks the register as containing an answer
    public void setAnswerFlag(boolean flagValue) {
        answerFlag = flagValue;
    }

    // check if the register contains an answer
    public boolean isAnswer() {
        return answerFlag;
    }

    // check if the value exists
    public boolean hasBeenEntered(){
        return isEntered;
    }

    // set the register as having being entered
    public void setAsEntered(){
        isEntered = true;
    }

    // set the register as having NOT been entered
    public void setAsNotEntered(){
        isEntered = false;
    }

    // copy the value of another register into this one.
    public void copyOf(CalcRegister other){
        value = other.value;
    }

    private String generateBinaryString(int number) {

        binaryString = "";

        if (number == 0) {
            // Can't convert 0 to binary this way, so the answer is just 0
            binaryString = "0";
        } else {
            // can't take a log of a negative number, so
            // if it's negative, make it positive and start the string with a "-".
            if (number < 0) {
                number *= -1;
                binaryString = "-";
            }

            // Take the log base 2 of the number
            // i.e. any log of the number, divided by that same log of 2
            double log2 = Math.log(number) / Math.log(2);
            // Log.i("convertToBinaryString", "Log base 2 of " + number + " is " + log2);

            // Round the log to the next higher integer, to make sure you get every binary place
            int roundedLog = (int) Math.ceil(log2);

            // For each binary exponent
            for (int i = roundedLog; i >= 0; i--) {
                // if 2 to that exponent is less than or equal to the number
                // boolean ltet = Math.pow(2, i) <= number;
                if (Math.pow(2, i) <= number) {
                    // Log.i("convertToBinaryString", "ltet is " + ltet + " because number is " + number + ", log2 is " + roundedLog + ", power is " + Math.pow(2, i));
                    // Then that power is in the number, so add a 1 to the string
                    binaryString += "1";
                    // subtract the power from the number, otherwise all lower powers will be found as well.
                    number -= Math.pow(2, i);
                } else {
                    // Otherwise, the number is smaller than that power and there's a 0 in that place.
                    // Log.i("convertToBinaryString", "ltet is " + ltet + " because number is " + number + ", log2 is " + roundedLog + ", power is " + Math.pow(2, i));
                    // otherwise add a 0 to the string
                    if (!binaryString.equals("")) {
                        binaryString += "0";
                    }
                }
            }

        }

        return binaryString;

    }

    public int getBinaryLength(){
        return 0;
    }
}
