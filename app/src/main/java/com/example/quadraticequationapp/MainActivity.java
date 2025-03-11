package com.example.quadraticequationapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private EditText inputA, inputB, inputC;
    private Button ButtonSolve;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inputA = findViewById(R.id.editA_Coef);
        inputB = findViewById(R.id.editB_Coef);
        inputC = findViewById(R.id.editC_Coef);
        ButtonSolve = findViewById(R.id.buttonSolve);
        resultTextView = findViewById(R.id.textSolution);

        ButtonSolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solveQuadraticEquation();
            }
        });
    }

    private void solveQuadraticEquation()
    {
        try {
            String strA = inputA.getText().toString();
            String strB = inputB.getText().toString();
            String strC = inputC.getText().toString();

            if (strA.isEmpty() || strB.isEmpty() || strC.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, введите все коэффициенты", Toast.LENGTH_SHORT).show();
                return;
            }

            validateDecimalPoints(strA);
            validateDecimalPoints(strB);
            validateDecimalPoints(strC);

            double a = parseDoubleSafely(strA);
            double b = parseDoubleSafely(strB);
            double c = parseDoubleSafely(strC);

            if (a == 0) {
                if (b == 0) {
                    if (c == 0) {
                        resultTextView.setText("Бесконечные решения (тождество)");
                    } else {
                        resultTextView.setText("Нет решения (противоречние)");
                    }
                } else {
                    double root = -c / b;
                    resultTextView.setText("Недопустимый результат (переполнение или неопределенность)");
                }
            } else {

                double discriminant = b * b - 4 * a * c;

                if (discriminant > 0) {
                    double root1 = (-b + Math.sqrt(discriminant)) / (2 * a);
                    double root2 = (-b - Math.sqrt(discriminant)) / (2 * a);
                    resultTextView.setText("Корни квадратного уравнения:\nx1 = " + formatNumber(root1) + "\nx2 = " + formatNumber(root2));

                } else if (discriminant == 0) {
                    double root = -b / (2 * a);
                    resultTextView.setText("Корень: x = " + formatNumber(root));

                } else
                {
                    resultTextView.setText("Нет корней");
                    }
                }
            }
            catch (NumberFormatException e) {
            Toast.makeText(this, "Неверный ввод. Пожалуйста, вводите численные значения", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
            Toast.makeText(this, "Возникла ошибка:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private double parseDoubleSafely(String str)
    {
        try {
            double value = Double.parseDouble(str);
            if (Double.isInfinite(value))
            {
                throw new NumberFormatException("Значение слишком большое или слишком маленькое");
            }
            return value;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Неверный формат чисел");
        }
    }

    private void validateDecimalPoints(String input) throws NumberFormatException {
        int decimalPointCount = 0;
        for (char ch : input.toCharArray()) {
            if (ch == '.') {
                decimalPointCount++;
                if (decimalPointCount > 1) {
                    throw new NumberFormatException("Неверный формат чисел: несколько десятичных точек");
                }
            }
        }
    }

    private String formatNumber(double value)
    {
        String strValue = Double.toString(value);
        int decimalIndex = strValue.indexOf('.');

        if (decimalIndex == -1)
        {
            return strValue;
        }
        else
        {
            int decimalPlaces = strValue.length() - decimalIndex - 1;

            if (decimalPlaces > 4)
            {
                DecimalFormat df = new DecimalFormat("#.#####");
                df.setDecimalSeparatorAlwaysShown(true);
                return df.format(value);
            }
            else
            {
                return strValue;
            }
        }
    }

}