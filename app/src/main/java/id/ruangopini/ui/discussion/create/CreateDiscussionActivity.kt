package id.ruangopini.ui.discussion.create

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.textfield.TextInputLayout
import id.ruangopini.R
import id.ruangopini.data.model.Discussion
import id.ruangopini.databinding.ActivityCreateDiscussionBinding
import id.ruangopini.ui.base.reference.ReferenceViewModel
import id.ruangopini.utils.Helpers.afterTextChanged
import id.ruangopini.utils.Helpers.getPlainText
import id.ruangopini.utils.Helpers.hideView
import id.ruangopini.utils.Helpers.showError
import id.ruangopini.utils.Helpers.validateError
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateDiscussionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateDiscussionBinding
    private val refModel: ReferenceViewModel by viewModel()
    private val model: CreateDiscussionViewModel by viewModel()
    private val selectedItem = mutableListOf<String>()
    private var isPolicyType = false
    private var currentPolicyName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateDiscussionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Buat Ruang Diskusi"
            setDisplayHomeAsUpEnabled(true)
        }

        intent.extras?.getString(EXTRA_POLICY).let {
            if (it != null) {
                binding.tvPolicyName.text = it
                isPolicyType = true
                currentPolicyName = it
            } else with(binding) {
                tvPolicyName.hideView()
                tvTitlePolicyName.hideView()
            }
        }
        refModel.getAllPolicyCategory()
        refModel.policyCategory.observe(this, {
            val data = mutableListOf<String>()
            it.listCategory.forEach { category -> data.add(category.name) }

            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                data.filterNot { d -> selectedItem.contains(d) })
            (binding.edtAddCategory as? AutoCompleteTextView)?.apply {
                setAdapter(adapter)
                setOnItemClickListener { _, view, _, _ ->
                    onItemClick(data, view, adapter)
                }
            }
        })

        refModel.isLoading.observe(this, {
            binding.edtAddCategory.apply {
                isEnabled = !it
                setText(if (it) "Sedang Memuat data ..." else "")
            }
        })

        model.isComplete.observe(this, { binding.btnCreate.isEnabled = it })

        with(binding) {
            lifecycleScope.launch {
                edtRoomName.afterTextChanged { validateNotEmpty(it, tilRoomName, 0) }
            }
            lifecycleScope.launch {
                edtDesc.afterTextChanged { validateNotEmpty(it, tilDesc, 1) }
            }

            btnCreate.setOnClickListener {
                val discussion = Discussion(
                    edtRoomName.getPlainText(),
                    edtDesc.getPlainText(),
                    selectedItem,
                    issueName = if (isPolicyType) currentPolicyName else null
                )
                model.createNewDiscussion(discussion, isPolicyType, this@CreateDiscussionActivity)
            }
        }
    }

    private fun validateNotEmpty(it: String, til: TextInputLayout, progress: Int) {
        val valid = it.isEmpty()
        model.setProgress(progress, !valid)
        if (valid) til.showError()
        else validateError(til)
    }

    private fun onItemClick(
        data: MutableList<String>,
        view: View,
        adapter: ArrayAdapter<String>
    ) {
        val itemPos = data.indexOf((view as TextView).text)

        binding.chipGroup.addView(Chip(this@CreateDiscussionActivity)
            .apply {
                setChipDrawable(
                    ChipDrawable.createFromAttributes(
                        this@CreateDiscussionActivity,
                        null,
                        0,
                        R.style.Widget_MaterialComponents_Chip_Entry
                    )
                )
                chipBackgroundColor = ContextCompat.getColorStateList(context, R.color.primary_20)
                chipStrokeWidth = 2f
                chipStrokeColor = ContextCompat.getColorStateList(context, R.color.primary)
                closeIconTint = ContextCompat.getColorStateList(context, R.color.primary)

                text = data[itemPos]
                selectedItem.add(data[itemPos])
                onChipItemChange(selectedItem)
                adapter.apply {
                    clear()
                    addAll(data.filterNot { selectedItem.contains(it) })
                }

                setOnCloseIconClickListener {
                    binding.chipGroup.apply {
                        val index = indexOfChild(it)
                        selectedItem.removeAt(index)
                        onChipItemChange(selectedItem)
                        removeViewAt(index)
                        adapter.apply {
                            clear()
                            addAll(data.filterNot { selectedItem.contains(it) })
                        }
                    }
                }
                isCheckable = false
            })
        binding.edtAddCategory.setText("")
    }

    private fun onChipItemChange(selectedItem: MutableList<String>) {
        model.setProgress(2, selectedItem.isNotEmpty())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_POLICY = "extra_policy"
    }
}