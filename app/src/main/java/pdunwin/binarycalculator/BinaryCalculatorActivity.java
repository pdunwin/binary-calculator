package pdunwin.binarycalculator;

//import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BinaryCalculatorActivity extends AppCompatActivity {

    // What's currently in the binary text field.
    String currentBinaryText;

    // the current value being entered.
    int currentValue = 0;

    // the value stored for the operation
    int storedValue = 0;

    // the first (left, top) value of the equation
    CalcRegister firstRegister = new CalcRegister();
    // the second (right, bottom) value of the equation
    // as in firstRegister [operator] secondRegister =
    // And what it equals is the new firstRegister
    CalcRegister secondRegister = new CalcRegister();

    // a temporary value
    CalcRegister answerRegister = new CalcRegister();
    int temp;
    // meant as a reference to the current register
    CalcRegister currentRegister = firstRegister;

    // the character for the current operator
    String currentOperator = "";

    // the state of the calculator: true if an entry has not been made, false otherwise, hopefully
    boolean awaitingSecondNumber = false;
    boolean entryInProgress = true;

    // Checks whether or not there is a stored value
    // Note: make a class/function that stores the value and sets thereIsAStoredValue to true.
    boolean thereIsAStoredValue = false;

    // the last key pressed
    char currentKey;

    // should onCreate be protected or public?
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setHasOptionsMenu(true);
        setContentView(R.layout.activity_binary_calculator);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        //myToolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        /*myToolbar.setNavigationOnClickListener(
        new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BinaryCalculatorActivity.this, "clicking the toolbar!", Toast.LENGTH_SHORT).show();
                }
        }
        );
        */

        // Are the "finals" required?
        final Button plus_key = (Button) findViewById(R.id.plus_key);
        final Button minus_key = (Button) findViewById(R.id.minus_key);
        final Button times_key = (Button) findViewById(R.id.times_key);
        final Button zero_key = (Button) findViewById(R.id.zero_key);
        final Button one_key = (Button) findViewById(R.id.one_key);
        final Button clear_key = (Button) findViewById(R.id.clear_key);
        final Button equals_key = (Button) findViewById(R.id.equals_key);
        final TextView textBinary = (TextView) findViewById(R.id.textBinary);
        textBinary.setText("0");
        final TextView textDecimal = (TextView) findViewById(R.id.textDecimal);
        final TextView textOperator = (TextView) findViewById(R.id.textCurrentOperator);

        zero_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if operator is showing and first register is not current register
                if (currentRegister == firstRegister && !textOperator.getText().equals(""))
                {
                    // clear the display
                    textDecimal.setText("");
                    textBinary.setText("");
                    // clear second register
                    secondRegister.setValue(0);
                    // set current register to second register
                    currentRegister = secondRegister;
                }

                // if the answer is showing and there's no operator
                if (currentRegister.isAnswer() && textOperator.getText().equals("")){
                    // initialize the registers
                    firstRegister.setValue(0);
                    firstRegister.setAsNotEntered();
                    firstRegister.setAnswerFlag(false);
                    secondRegister.setValue(0);
                    secondRegister.setAsNotEntered();
                    secondRegister.setAnswerFlag(false);
                    currentRegister = firstRegister;
                    textOperator.setText("");
                }

                // Double the value of the current register
                currentRegister.setValue(2 * currentRegister.getValue());

                // Update the decimal display.
                textDecimal.setText(Integer.toString(currentRegister.getValue()));

                // Update the binary display.
                textBinary.setText(convertToBinaryString(currentRegister.getValue()));


            }
        });


        one_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if operator is showing and first register is not current register
                if (currentRegister == firstRegister && !textOperator.getText().equals(""))
                {
                    // clear the display
                    textDecimal.setText("");
                    textBinary.setText("");
                    // clear second register
                    secondRegister.setValue(0);
                    // set current register to second register
                    currentRegister = secondRegister;
                }

                // if the answer is showing and there's no operator
                if (currentRegister.isAnswer() && textOperator.getText().equals("")){
                    // initialize the registers
                    firstRegister.setValue(0);
                    firstRegister.setAsNotEntered();
                    firstRegister.setAnswerFlag(false);
                    secondRegister.setValue(0);
                    secondRegister.setAsNotEntered();
                    secondRegister.setAnswerFlag(false);
                    currentRegister = firstRegister;
                    textOperator.setText("");
                }
                // shorthand, so I don't have to keep using the get method.
                currentValue = currentRegister.getValue();

                // TODO: Handle values with a smaller absolute value than 1.
                // TODO: Check to make sure that changing the value won't make it too long.

                // Double the value of the current register and add 1
                currentRegister.setValue(2 * currentRegister.getValue() + 1);

                // Update the decimal display.
                textDecimal.setText(Integer.toString(currentRegister.getValue()));

                // Update the binary display.
                textBinary.setText(convertToBinaryString(currentRegister.getValue()));

            }
        });

        clear_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Set the value of both displays to 0
                textBinary.setText("0");
                textDecimal.setText("0");

                //set the current value to 0
                currentValue = 0;

                //set the current operator to ""
                textOperator.setText("");
                currentOperator = "";

                //There is no stored value
                storedValue = 0;
                thereIsAStoredValue = false;

                // no entry currently in progress.
                entryInProgress = false;

                firstRegister.setValue(0);
                firstRegister.setAsNotEntered();
                firstRegister.setAnswerFlag(false);
                secondRegister.setValue(0);
                secondRegister.setAsNotEntered();
                firstRegister.setAnswerFlag(false);
                currentRegister = firstRegister;
                answerRegister.setValue(0);

            }
        });

        // The onClickListeners for the operator buttons. They all do the same thing so I'd like
        // to have a single onClickListener that calls the same function: find the operator symbol,
        // store the current value, zero the displays.
        plus_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // set operator
                textOperator.setText("+");
                // set operator text
                currentOperator = (String) textOperator.getText();

                // set the current register to the second register
                // currentRegister = secondRegister;

            }
        });

        minus_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // set operator
                textOperator.setText("-");
                // set operator text
                currentOperator = (String) textOperator.getText();

                // set the current register to the second register
                // currentRegister = secondRegister;

            }
        });

        times_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // set operator
                textOperator.setText("×");
                // set operator text
                currentOperator = (String) textOperator.getText();

                // set the current register to the second register
                // currentRegister = secondRegister;

            }
        });

        equals_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if an operator has been selected and two arguments have been entered
                if (!textOperator.getText().equals("")) {
                    // if the current register is the first register
                    if (currentRegister == firstRegister) {
                        // set the value of the second register to the value of the first register
                        secondRegister.setValue(firstRegister.getValue());
                    }
                    // retain the current value
                    temp = currentValue;
                    String answerString;
                    String operator = textOperator.getText().toString();

                    // setValue returns a string, which is unused elsewhere but
                    // in the case of the answer, is used to report an error.
                    answerString = answerRegister.setValue(getAnswer(operator, firstRegister.getValue(), secondRegister.getValue()));

                    // set the decimal value to the current value
                    textDecimal.setText(Integer.toString(answerRegister.getValue()));
                    // Convert the current value to a binary string - or should it be storedValue?
                    textBinary.setText(answerString);

                    // set the first register to the be answer
                    firstRegister.setValue(answerRegister.getValue());
                    firstRegister.setAnswerFlag(true);

                    // set first register to current register
                    currentRegister = firstRegister;

                    // Initialization will now take place when a number key is pressed after evaluation.
                    /*
                    // initialize the registers
                    firstRegister.setValue(0);
                    firstRegister.setAsNotEntered();
                    secondRegister.setValue(0);
                    secondRegister.setAsNotEntered();
                    currentRegister = firstRegister;
                    */
                    textOperator.setText("");
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            //case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                //return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

            }
    }

    private int getAnswer(String operator, int first, int second) {

        // make the default the current value
        int result = second;
        // String binaryResult = "";

        switch (operator) {
            case "+":
                result = first + second;
                break;
            case "-":
                result = first - second;
                break;
            case "×":
                result = first * second;
                break;
            //case '÷':
            //    result = first / second;
            //    break;
        }
        // binaryResult = convertToBinaryString(result);
        // return binaryResult;

        return result;

    }

    private String convertToBinaryString(int number) {

        String binaryString = "";

        if (number == 0){
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
            double log2 = Math.log(number)/Math.log(2);
            Log.i("convertToBinaryString", "Log base 2 of " + number + " is " + log2);

            // Round the log to the next higher integer, to make sure you get every binary place
            int roundedLog = (int) Math.ceil(log2);

            // For each binary exponent
            for (int i = roundedLog; i >= 0; i--) {
                // if 2 to that exponent is less than or equal to the number
                boolean ltet = Math.pow(2, i) <= number;
                if (Math.pow(2, i) <= number) {
                    Log.i("convertToBinaryString", "ltet is " + ltet + " because number is " + number + ", log2 is " + roundedLog + ", power is " + Math.pow(2, i));
                    // Then that power is in the number, so add a 1 to the string
                    binaryString += "1";
                    // subtract the power from the number, otherwise all lower powers will be found as well.
                    number -= Math.pow(2, i);
                } else {
                    // Otherwise, the number is smaller than that power and there's a 0 in that place.
                    Log.i("convertToBinaryString", "ltet is " + ltet + " because number is " + number + ", log2 is " + roundedLog + ", power is " + Math.pow(2, i));
                    // otherwise add a 0 to the string
                    // If the string is currently empty and doesn't begin with a "-" then set it to 0.
                    if (!binaryString.equals("") && !binaryString.equals("-")) {
                        binaryString += "0";
                    }
                }
            }

        }

        return binaryString;
    }


}
