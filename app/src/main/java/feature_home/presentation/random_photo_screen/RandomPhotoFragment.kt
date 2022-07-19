package feature_home.presentation.random_photo_screen

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.DatePicker
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.astronomicalphotooftheday.R
import com.example.astronomicalphotooftheday.databinding.DateLayoutDialogBinding
import com.example.astronomicalphotooftheday.databinding.FragmentRandomPhotosBinding
import com.example.core.base.BaseFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint

class RandomPhotoFragment() : BaseFragment<FragmentRandomPhotosBinding>() {

    private val randomPhotoViewModel: RandomPhotoViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observePhotos()
        observeUiEvents()
        datePickerBottomDialog(view.context);
      /*  binding.apply {
            binding.btPickDate.setOnClickListener(){

            }
        }*/

    }

    private fun observePhotos() {
        randomPhotoViewModel.randomPhotos.observe(viewLifecycleOwner) { photo ->
            binding.item.visibility=View.VISIBLE
            binding.tvTitle.text = photo.title
            binding.tvContent.text = photo.explanation
            binding.tvDate.text = photo.date
            binding.imgPhoto.load(photo.url)
            binding.btPickDate.setOnClickListener(){

            }
            binding.btnAddFavorites.setOnClickListener {   randomPhotoViewModel.onEvent(RandomPhotoEvent.AddButtonClicked(photo))}
            binding.btnDetail.setOnClickListener {
                if (binding.extendedLinearLayout.visibility == View.GONE)
                    binding.extendedLinearLayout.visibility = View.VISIBLE
                else
                    binding.extendedLinearLayout.visibility = View.GONE
            }
        }
    }
    private fun datePickerBottomDialog(context: Context) {
        val dialog: Dialog = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Dialog(context, R.style.DatePickerTheme)
        } else {
            Dialog(context)
        }

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.date_layout_dialog)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER

        dialog.window!!.attributes = lp

        val datePicker=dialog.findViewById(R.id.datepicker) as DatePicker
        datePicker.maxDate = System.currentTimeMillis() - 1000
        val calendar = Calendar.getInstance()
        datePicker.updateDate(
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH]
        )

        val btnDone = dialog.findViewById(R.id.btnDone) as Button
        btnDone.setOnClickListener(View.OnClickListener {
            val year = datePicker.year
            val month = datePicker.month
            val day = datePicker.dayOfMonth
           randomPhotoViewModel.getPhotoByDate(randomPhotoViewModel.formatDate(year, month, day))
            dialog.dismiss();
        })
        dialog.show()

    }

    class DateSelectDialog : DialogFragment(R.layout.date_layout_dialog) {
        private val viewModel: DateSelectViewModel by viewModels()
        private lateinit var binding: DateLayoutDialogBinding

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            binding = DateLayoutDialogBinding.bind(view)

            binding.datepicker.maxDate = System.currentTimeMillis() - 1000
            val calendar = Calendar.getInstance()
                    binding.datepicker.updateDate(
                        calendar[Calendar.YEAR],
                        calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH]
                    )
           binding.btnDone.setOnClickListener {
               val year = binding.datepicker.year
               val month = binding.datepicker.month
               val day = binding.datepicker.dayOfMonth
              val dialogListener = activity as DialogListener?
               dialogListener?.onSelect(viewModel.formatDate(year, month, day))
               dismiss()
           }

        }


    }
    private fun observeUiEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            randomPhotoViewModel.randomPhotosEvent.collect { event ->
                when (event) {
                    is RandomPhotoViewModel.UiRandomPhotoEvent.ShowSnackbar -> {
                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_LONG).show()
                    }
                    is RandomPhotoViewModel.UiRandomPhotoEvent.ShowProgressBar -> {
                        binding.pbLoadingItems.isVisible = event.isLoading
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRandomPhotosBinding.inflate(inflater, container, false)
}
