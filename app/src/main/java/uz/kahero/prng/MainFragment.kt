package uz.kahero.prng

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.*
import kotlin.math.pow
import kotlin.random.Random

class MainFragment : Fragment(R.layout.fragment_main) {
    var a: Long = 0
    var s: Long = 0
    var d: Long = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        generate.setOnClickListener {
            if (isValid(initialKey) && isValid(keyLength))
                generateRandom()
        }

        initialKey.addTextChangedListener {
            setRandomFun()
        }
        type.setOnCheckedChangeListener { _, _ ->
            setRandomFun()
        }
    }


    private fun setRandomFun() {
        d = Random.nextLong(10000000)
        a = Random.nextLong(100000000)
        s = Random.nextLong(1000000000)
        val text =
            when {
                nonLinear.isChecked -> "X(i+1) = ($d * X(i)^2 + $a * X(i)+${s})%2^32"
                multiplicative.isChecked -> "X(i+1) = ($a * X(i))%2^32"
                else -> "X(i+1) = ($a * X(i) + $s)%2^32"
            }
        randomFormula.setText(text)
    }


    private fun generateRandom() {
        val length = keyLength.text.toString().toLong()
        var string = ""
        var initial = initialKey.text.toString().toLong()
        for (i in 0 until length) {
            initial = when {
                nonLinear.isChecked -> ((d * initial.toDouble().pow(2) + a * initial + s) % 2.0.pow(
                    32
                )).toLong()
                multiplicative.isChecked -> ((a * initial + s) % 2.0.pow(32)).toLong()
                else -> ((a * initial) % 2.0.pow(32)).toLong()
            }
            string += (initial % 3 + 1) % 2
        }
        showResult(string)
    }

    private fun showResult(string: String) {
        AlertDialog.Builder(requireContext()).setMessage(string)
            .setTitle(getString(R.string.random_key))
            .setPositiveButton(getString(R.string.ok)) { dialog, whick ->
                dialog.dismiss()
            }.setNegativeButton(getString(R.string.copy)) { dialog, which ->
                val clipboardManager =
                    requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboardManager.setPrimaryClip(
                    ClipData.newPlainText(
                        getString(R.string.random_key),
                        string
                    )
                )
                Toast.makeText(
                    requireContext(),
                    getString(R.string.copied_to_clipboard),
                    Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()
            }
            .show()
    }

    private fun isValid(editText: EditText): Boolean {
        if (editText.text.toString().toIntOrNull() == null) {
            editText.error = getString(R.string.required_field)
            editText.requestFocus()
        }
        return editText.text.toString().toIntOrNull() != null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.about) {
            (activity as MainActivity).startFragment(FragmentWebView.newInstance(), true)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}