package com.josehumaneshumanes.codescanner

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.josehumaneshumanes.codescanner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupLayout()
        setupViews()
    }

    private fun setupLayout() {
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }

    private fun setupViews() {
        with(viewBinding) {
            scannButton.setOnClickListener { initScanner() }
            flashSwitch.setOnCheckedChangeListener { _, isChecked -> updateFlashConfig(isChecked) }
            beepSwitch.setOnCheckedChangeListener { _, isChecked -> updateBeepConfig(isChecked) }
        }
    }

    private fun updateBeepConfig(isChecked: Boolean) {
        viewBinding.beepImage.setImageResource(
            if (isChecked) R.drawable.outline_volume_up_24
            else R.drawable.outline_volume_off_24
        )
    }

    private fun updateFlashConfig(isChecked: Boolean) {
        viewBinding.flashImage.setImageResource(
            if (isChecked) R.drawable.outline_flashlight_on_24
            else R.drawable.outline_flashlight_off_24
        )
    }

    private fun initScanner() {
        IntentIntegrator(this).apply {
            setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
            setPrompt(resources.getString(R.string.scanner_prompt))
            setTorchEnabled(viewBinding.flashSwitch.isChecked)
            setBeepEnabled(viewBinding.beepSwitch.isChecked)
            initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {
            if (result.contents == null) {
                viewBinding.valueScanned.text = resources.getString(R.string.no_value)
                Toast.makeText(this, resources.getString(R.string.canceled), Toast.LENGTH_LONG)
                    .show()
            } else {
                viewBinding.valueScanned.text = result.contents
                Toast.makeText(
                    this,
                    resources.getString(R.string.content_scanned, result.contents),
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}
