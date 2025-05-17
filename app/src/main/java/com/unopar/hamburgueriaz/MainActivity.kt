package com.unopar.hamburgueriaz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editTextNomeCliente: EditText
    private lateinit var checkBoxAlface: CheckBox
    private lateinit var checkBoxQueijo: CheckBox
    private lateinit var checkBoxPickles: CheckBox
    private lateinit var checkBoxCebola: CheckBox
    private lateinit var checkBoxTomate: CheckBox
    private lateinit var buttonRemove: Button
    private lateinit var buttonAdd: Button
    private lateinit var buttonEnviarPedido: Button
    private lateinit var textViewQuantidadeValor: TextView
    private lateinit var textViewPrecoValor: TextView

    private var quantidade = 1
    private val precoBase = 20.00
    private var precoTotal = 20.00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inicializarComponentes()

        configurarListeners()
    }

    private fun inicializarComponentes() {
        editTextNomeCliente = findViewById(R.id.editTextNomeCliente)

        checkBoxAlface = findViewById(R.id.checkBoxAlface)
        checkBoxQueijo = findViewById(R.id.checkBoxQueijo)
        checkBoxPickles = findViewById(R.id.checkBoxPickles)
        checkBoxCebola = findViewById(R.id.checkBoxCebola)
        checkBoxTomate = findViewById(R.id.checkBoxTomato)

        buttonRemove = findViewById(R.id.buttonRemove)
        buttonAdd = findViewById(R.id.buttonAdd)
        textViewQuantidadeValor = findViewById(R.id.textViewQuantidadeValor)

        textViewPrecoValor = findViewById(R.id.textViewPrecoValor)

        buttonEnviarPedido = findViewById(R.id.buttonEnviarPedido)
    }

    private fun configurarListeners() {
        checkBoxAlface.setOnCheckedChangeListener { _, _ -> calcularPrecoTotal() }
        checkBoxQueijo.setOnCheckedChangeListener { _, _ -> calcularPrecoTotal() }
        checkBoxPickles.setOnCheckedChangeListener { _, _ -> calcularPrecoTotal() }
        checkBoxCebola.setOnCheckedChangeListener { _, _ -> calcularPrecoTotal() }
        checkBoxTomate.setOnCheckedChangeListener { _, _ -> calcularPrecoTotal() }

        buttonRemove.setOnClickListener {
            if (quantidade > 1) {
                quantidade--
                atualizarQuantidadeEPreco()
            }
        }

        buttonAdd.setOnClickListener {
            quantidade++
            atualizarQuantidadeEPreco()
        }

        buttonEnviarPedido.setOnClickListener {
            enviarPedido()
        }
    }

    private fun atualizarQuantidadeEPreco() {
        textViewQuantidadeValor.text = quantidade.toString()

        calcularPrecoTotal()
    }

    private fun calcularPrecoTotal() {
        var precoItem = precoBase

        if (checkBoxAlface.isChecked) precoItem += 2.00
        if (checkBoxQueijo.isChecked) precoItem += 2.00
        if (checkBoxPickles.isChecked) precoItem += 3.00
        if (checkBoxCebola.isChecked) precoItem += 1.00
        if (checkBoxTomate.isChecked) precoItem += 1.50

        precoTotal = precoItem * quantidade

        textViewPrecoValor.text = "R$ %.2f".format(precoTotal)
    }

    private fun enviarPedido() {
        val nomeCliente = editTextNomeCliente.text.toString().trim()
        if (nomeCliente.isEmpty()) {
            Toast.makeText(this, "Digite seu nome antes de enviar o pedido", Toast.LENGTH_SHORT).show()
            return
        }

        val mensagem = StringBuilder()
        mensagem.append("Pedido de Hambúrguer\n\n")
        mensagem.append("Cliente: $nomeCliente\n\n")
        mensagem.append("Ingredientes:\n")
        if (checkBoxAlface.isChecked) mensagem.append("- Alface\n")
        if (checkBoxQueijo.isChecked) mensagem.append("- Queijo\n")
        if (checkBoxPickles.isChecked) mensagem.append("- Pickles\n")
        if (checkBoxCebola.isChecked) mensagem.append("- Cebola\n")
        if (checkBoxTomate.isChecked) mensagem.append("- Tomate\n")
        mensagem.append("\n")
        mensagem.append("Quantidade: $quantidade\n")
        mensagem.append("Preço Total: R$ %.2f".format(precoTotal))

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822" // Tipo MIME para email
            putExtra(Intent.EXTRA_EMAIL, arrayOf("contato@hamburgueria.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Pedido de Hambúrguer - Cliente: $nomeCliente")
            putExtra(Intent.EXTRA_TEXT, mensagem.toString())
        }

        try {
            startActivity(Intent.createChooser(intent, "Enviar pedido por email..."))
            Toast.makeText(this, "Escolha seu aplicativo de email", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Não foi possível abrir um app de email", Toast.LENGTH_LONG).show()
        }
    }
}