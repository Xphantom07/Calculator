package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView tvResult;
    String current = "", operator = "";
    double first = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);

        View rootView = findViewById(android.R.id.content);
        setButtonListeners(rootView);

        // Fullscreen mode
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }

    private void setButtonListeners(View view) {
        if (view instanceof Button) {
            ((Button) view).setOnClickListener(this::onButtonClick);
        } else if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                setButtonListeners(group.getChildAt(i));
            }
        }
    }

    private void onButtonClick(View v) {
        Button b = (Button) v;
        String text = b.getText().toString();

        switch (text) {
            case "C":
                current = "";
                first = 0;
                operator = "";
                tvResult.setText("0");
                break;

            case "CE":
                current = "";
                tvResult.setText("0");
                break;

            case "⌫":
                if (!current.isEmpty()) {
                    current = current.substring(0, current.length() - 1);
                    tvResult.setText(current.isEmpty() ? "0" : current);
                }
                break;

            case "+":
            case "-":
            case "*":
            case "/":
            case "%":
                if (!current.isEmpty()) {
                    first = Double.parseDouble(current);
                    operator = text;
                    current = "";
                    tvResult.setText(format(first) + " " + operator); // show 12 +
                }
                break;

            case "=":
                if (!current.isEmpty() && !operator.isEmpty()) {
                    double second = Double.parseDouble(current);
                    double result = calculate(first, second, operator);
                    String expression = format(first) + " " + operator + " " + format(second) + " =";
                    tvResult.setText(expression + "\n" + format(result));
                    current = format(result);
                    operator = "";
                }
                break;

            case ".":
                if (!current.contains(".")) {
                    current += ".";
                    tvResult.setText(current);
                }
                break;

            default: // numbers
                current += text;
                if (!operator.isEmpty()) {
                    tvResult.setText(format(first) + " " + operator + " " + current);
                } else {
                    tvResult.setText(current);
                }
                break;
        }
    }

    private double calculate(double a, double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/": return b != 0 ? a / b : 0;
            case "%": return a * (b / 100);
        }
        return b;
    }

    private String format(double value) {
        return value == (long) value ? String.format("%d", (long) value) : String.format("%s", value);
    }
}
