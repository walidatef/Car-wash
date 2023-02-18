package com.example.carwasher.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.carwasher.IsSuccessOrder
import com.example.carwasher.R
import com.example.carwasher.databinding.ActivityMainBinding
import com.example.carwasher.databinding.BottomSheetDialogBinding
import com.example.carwasher.model.Order
import com.example.carwasher.viewModel.ViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity(), IsSuccessOrder {
    private lateinit var binding: ActivityMainBinding
    private lateinit var waitDialog: WaitDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        waitDialog = WaitDialog(this)
        binding.progressBar.show()

        val viewModel = ViewModelProvider(this)[ViewModel::class.java]
        viewModel.fetchData(this)

        viewModel.positionOrder.observe(this) {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }

        viewModel.queueData.observe(this) {
            binding.carFrontTv.text = it["Cars_front"].toString()
            val t: Int = it["total_time"].toString().toInt()
            val hours = t / 60
            val minutes = t % 60
            binding.expectedTimeTv.text = "$hours:$minutes"
            if (t > 60) {
                binding.expectedTimeTypeTv.text = resources.getString(R.string.hour)
            }
            binding.progressBar.hide()
            binding.progressBarTV.visibility = View.GONE
            binding.mainLayout.visibility = View.VISIBLE
        }
        viewModel.phasesData.observe(this) {
            val firstPhase = it["first"] as Map<*, *>
            val secondPhase = it["second"] as Map<*, *>
            val thirdPhase = it["third"] as Map<*, *>

            binding.phase1.text = firstPhase["name"].toString()
            binding.desPhase1.text = firstPhase["description"].toString()
            binding.timePhase1Tv.text = firstPhase["time"].toString()
            binding.pricePhase1.text = firstPhase["price"].toString()

            binding.phase2.text = secondPhase["name"].toString()
            binding.desPhase2.text = secondPhase["description"].toString()
            binding.timePhase2Tv.text = secondPhase["time"].toString()
            binding.pricePhase2.text = secondPhase["price"].toString()

            binding.phase3.text = thirdPhase["name"].toString()
            binding.desPhase3.text = thirdPhase["description"].toString()
            binding.timePhase3Tv.text = thirdPhase["time"].toString()
            binding.pricePhase3.text = thirdPhase["price"].toString()
        }


        val insertFun: (android.content.Context, Order) -> Unit =
            { a: android.content.Context, b: Order -> viewModel.insertOrder(a, b) }

        binding.cardPhase1.setOnClickListener {
            showBottomSheet(
                "1",
                binding.pricePhase1.text.toString().toInt(),
                binding.timePhase1Tv.text.toString().toInt(), insertFun
            )

        }
        binding.cardPhase2.setOnClickListener {
            showBottomSheet(
                "2",
                binding.pricePhase2.text.toString().toInt(),
                binding.timePhase2Tv.text.toString().toInt(), insertFun
            )
        }
        binding.cardPhase3.setOnClickListener {
            showBottomSheet(
                "3",
                binding.pricePhase3.text.toString().toInt(),
                binding.timePhase3Tv.text.toString().toInt(), insertFun
            )
        }


    }

    @SuppressLint("InflateParams")
    private fun showBottomSheet(
        phase: String,
        price: Int,
        time: Int,
        insert: (android.content.Context, Order) -> Unit
    ) {

        val dialog = BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme)
        //dialog.window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN)

        val sheetBinding: BottomSheetDialogBinding =
            BottomSheetDialogBinding.inflate(layoutInflater)

        dialog.setCancelable(false)
        dialog.setContentView(sheetBinding.root)
        dialog.show()

        sheetBinding.cancelBtn.setOnClickListener { dialog.dismiss() }
        sheetBinding.orderBtn.setOnClickListener {

            if (sheetBinding.nameEdT.text?.isEmpty() == true) {
                sheetBinding.nameEdT.error = resources.getString(R.string.empty)
            }
            if (sheetBinding.phoneEdT.text?.isEmpty() == true) {
                sheetBinding.phoneEdT.error = resources.getString(R.string.empty)
            }
            if (sheetBinding.carNumberEdT.text?.isEmpty() == true) {
                sheetBinding.carNumberEdT.error = resources.getString(R.string.empty)
            }
            if (sheetBinding.carTypeEdT.text?.isEmpty() == true) {
                sheetBinding.carTypeEdT.error = resources.getString(R.string.empty)
            }

            if (sheetBinding.nameEdT.text?.isNotEmpty() != false && sheetBinding.phoneEdT.text?.isNotEmpty() != false
                && sheetBinding.carNumberEdT.text?.isNotEmpty() != false && sheetBinding.carTypeEdT.text?.isNotEmpty() != false
            ) {
                val order = Order(
                    sheetBinding.nameEdT.text.toString(),
                    sheetBinding.phoneEdT.text.toString(),
                    sheetBinding.carNumberEdT.text.toString(),
                    sheetBinding.carTypeEdT.text.toString(),
                    phase,
                    time,
                    price
                )

                dialog.dismiss()
                waitDialog.startWaitingDialog()
                IsSuccessOrder.setListener(this)
                insert(this, order)


            }

        }
    }

    override fun isSuccess(flag: Boolean) {
        if (flag)
            waitDialog.showSuccessLayout()
    }
}