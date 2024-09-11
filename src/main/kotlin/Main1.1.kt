package com.example

import javafx.application.Application
import javafx.geometry.HPos
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Main1 : Application() {

    override fun start(primaryStage: Stage) {
        primaryStage.title = "Завдання 1.1"
        primaryStage.isResizable = false

        val grid = GridPane().apply {
            padding = Insets(10.0)
            hgap = 10.0
            vgap = 12.0
            alignment = Pos.CENTER
        }

        val labels = listOf("H %", "C %", "S %", "N %", "O %", "W %", "A %")
        val inputFields = labels.map {
            Label(it).apply { prefWidth = 200.0; alignment = Pos.CENTER_RIGHT } to TextField().apply { prefWidth = 100.0; maxWidth = 100.0 }
        }

        inputFields.forEachIndexed { index, (label, textField) ->
            grid.add(label, 0, index)
            grid.add(textField, 1, index)
        }

        val outputLabels = listOf(
            Label("Коефіцієнт переходу від робочої до сухої маси").apply { prefWidth = 300.0 },
            Label("Коефіцієнт переходу від робочої до горючої маси").apply { prefWidth = 300.0 },
            Label("Склад сухої маси палива").apply { prefWidth = 300.0 },
            Label("Склад горючої маси палива").apply { prefWidth = 300.0 },
            Label("Нижча теплота згоряння для робочої маси").apply { prefWidth = 300.0 },
            Label("Нижча теплота згоряння для сухої маси").apply { prefWidth = 300.0 },
            Label("Нижча теплота згоряння для горючої маси").apply { prefWidth = 300.0 },
            )

        val outputFields = outputLabels.map { { it } to Label("").apply { prefWidth = 250.0 } }

        outputFields.forEachIndexed { index, (label, valueField) ->
            grid.add(label(), 0, labels.size + index )
            grid.add(valueField, 1, labels.size + index)
        }

        val button = Button("Розрахувати").apply {
            prefWidth = 200.0
            setOnAction {
                CoroutineScope(Dispatchers.Default).launch {
                    try {
                        val inputs = inputFields.map { it.second.text.toFloatOrNull() ?: 0.0f }
                        val results = performCalculations(inputs)
                        withContext(Dispatchers.Main) {
                            outputFields.forEachIndexed { index, (label, valueField) ->
                                valueField.text = results[index]
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        withContext(Dispatchers.Main) {
                            outputFields.forEach { it.second.text = "Error" }
                        }
                    }
                }
            }
        }
        grid.add(button, 0, labels.size + outputLabels.size, 2, 1)
        GridPane.setHalignment(button, HPos.CENTER)

        val scene = Scene(grid, 600.0, 600.0)
        primaryStage.scene = scene
        primaryStage.show()
    }

    private fun performCalculations(inputs: List<Float>): List<String> {
        print("Performing calculations with inputs: $inputs")
        var H = inputs[0]
        var C = inputs[1]
        var S = inputs[2]
        var N = inputs[3]
        var O = inputs[4]
        var W = inputs[5]
        var A = inputs[6]

        val coef_work_dry = 100 / (100 - W)
        val coef_work_burn = 100 / (100 - W - A)

        val H_dry = H * coef_work_dry
        val C_dry = C * coef_work_dry
        val S_dry = S * coef_work_dry
        val N_dry = N * coef_work_dry
        val O_dry = O * coef_work_dry
        val A_dry = A * coef_work_dry

        val H_burn = H * coef_work_burn
        val C_burn = C * coef_work_burn
        val S_burn = S * coef_work_burn
        val N_burn = N * coef_work_burn
        val O_burn = O * coef_work_burn

        val Q_lower_temp = (339 * C + 1030 * H - 108.8 * (O - S) - 25 * W) / 1000

        val Q_lower_temp_dry = (Q_lower_temp + 0.025 * W) * 100 / (100 - W)

        val Q_lower_temp_burn = (Q_lower_temp + 0.025 * W) * 100 / (100 - W - A)

        return listOf(
            String.format("%.2f", coef_work_dry),
            String.format("%.2f", coef_work_burn),
            String.format("H = %.2f%%; C = %.2f%%; S = %.2f%%;\nN = %.2f%%; O = %.2f%%; A = %.2f%%", H_dry, C_dry, S_dry, N_dry, O_dry, A_dry),
            String.format("H = %.2f%%; C = %.2f%%; S = %.2f%%;\nN = %.2f%%; O = %.2f%%", H_burn, C_burn, S_burn, N_burn, O_burn),
            String.format("%.2f МДж/кг", Q_lower_temp),
            String.format("%.2f МДж/кг", Q_lower_temp_dry),
            String.format("%.2f МДж/кг", Q_lower_temp_burn),
        )
    }

}

fun main() {
    Application.launch(Main1::class.java)
}
