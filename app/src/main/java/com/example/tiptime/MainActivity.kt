package com.example.tiptime

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat

/**
 * Aktivitas yang menampilkan kalkulator tip.
 */
class MainActivity : AppCompatActivity() {

    // Mengikat instance objek dengan akses ke tampilan di layout activity_main.xml
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mengembalikan instance objek yang mengikat
        binding = ActivityMainBinding.inflate(layoutInflater)

        // Tetapkan tampilan konten Aktivitas menjadi tampilan letak root
        setContentView(binding.root)

        // Siapkan click listener pada tombol hitung untuk menghitung tip
        binding.calculateButton.setOnClickListener { calculateTip() }

        // Siapkan key listener  untuk mendengarkan penekanan tombol "enter".
        binding.costOfServiceEditText.setOnKeyListener { view, keyCode, _ -> handleKeyEvent(view, keyCode)
        }
    }

    /**
     * Menghitung tip berdasarkan input pengguna.
     */
    private fun calculateTip() {
        // Dapatkan nilai desimal dari EditText
        val stringInTextField = binding.costOfServiceEditText.text.toString()
        val cost = stringInTextField.toDoubleOrNull()

        // Jika biayanya null atau 0, maka tampilkan 0 tip dan keluar dari fungsi ini lebih awal.
        if (cost == null || cost == 0.0) {
            displayTip(0.0)
            return
        }

        // Dapatkan persentase tip berdasarkan tombol radio mana yang dipilih
        val tipPercentage = when (binding.tipOptions.checkedRadioButtonId) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15

        }
        // Perhitungan Tip
        var tip = tipPercentage * cost

        // Jika switch untuk membulatkan tip diaktifkan, maka bulatkan ke atas
        // Jika tidak, jangan ubah nilai tip.
        if (binding.roundUpSwitch.isChecked) {
            // Ambil nilai tip dan dibulatkan ke bilangan bulat, lalu simpan nilai
            // dalam variabel tip.
            tip = kotlin.math.ceil(tip)
        }

        // Menampilkan nilai tip di layar berdasarkan fungsi displayTip
        displayTip(tip)
    }

    /*
    * Format jumlah tip sesuai dengan mata uang lokal dan tampilkan di layar.
    */
    private fun displayTip(tip: Double) {
        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)
    }

    /**
     * Key Listener untuk menyembunyikan keyboard saat tombol "Enter" diketuk.
     */
    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {

            // Sembunyikan keyboard
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }
}