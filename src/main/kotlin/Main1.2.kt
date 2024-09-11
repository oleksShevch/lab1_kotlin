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

class Main2 : Application() {

    override fun start(primaryStage: Stage) {
        primaryStage.title = "Завдання 1.2"
        primaryStage.isResizable = false

        val grid = GridPane().apply {
            padding = Insets(10.0)
            hgap = 10.0
            vgap = 12.0
            alignment = Pos.CENTER
        }

        val labels = listOf("H %", "C %", "S %", "O %", "V мг/кг", "W %", "A %", "Q, МДж/кг")
        val inputFields = labels.map {
            Label(it).apply { prefWidth = 200.0; alignment = Pos.CENTER_RIGHT } to TextField().apply { prefWidth = 100.0; maxWidth = 100.0 }
        }

        inputFields.forEachIndexed { index, (label, textField) ->
            grid.add(label, 0, index)
            grid.add(textField, 1, index)
        }

        val outputLabels = listOf(
            Label("Склад робочої маси мазуту").apply { prefWidth = 300.0 },
            Label("Нижча теплота згоряння для робочої маси").apply { prefWidth = 300.0 },
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

        val scene = Scene(grid, 600.0, 500.0)
        primaryStage.scene = scene
        primaryStage.show()
    }

    private fun performCalculations(inputs: List<Float>): List<String> {
        print("Performing calculations with inputs: $inputs")
        var H = inputs[0]
        var C = inputs[1]
        var S = inputs[2]
        var O = inputs[3]
        var V = inputs[4]
        var W = inputs[5]
        var A = inputs[6]
        var Q = inputs[7]

        val H_work = H * (100 - W - A) / 100
        val C_work = C * (100 - W - A) / 100
        val S_work = S * (100 - W - A) / 100
        val O_work = O * (100 - W - A) / 100
        val V_work = V * (100 - W) / 100
        val A_work = A * (100 - W) / 100

        val Q_final = Q * (100 - W - A) / 100 - 0.025 * W

        return listOf(
            String.format("H = %.2f%%; C = %.2f%%; S = %.2f%%;\nO = %.2f%%; V = %.2f%%; A = %.2f%%", H_work, C_work, S_work, O_work, V_work, A_work),
            String.format("Q = %.2f МДж/кг", Q_final)
        )
    }

}

fun main() {
    Application.launch(Main2::class.java)
}
