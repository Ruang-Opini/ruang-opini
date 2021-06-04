package id.ruangopini.ui.base.reference

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.ruangopini.data.model.Policy
import id.ruangopini.databinding.ActivityReferenceBinding
import id.ruangopini.ui.policy.detail.DetailPolicyActivity

class ReferenceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReferenceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(binding.containerView.id, ReferenceFragment.newInstance(true))
            .commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ReferenceAdapter.REQ_CODE) if (resultCode == RESULT_OK) {
            data?.getParcelableExtra<Policy>(DetailPolicyActivity.RESULT_POLICY).let {
                setResult(RESULT_OK, Intent().apply { putExtra(RESULT_POLICY, it) })
                finish()
            }
        }
    }

    companion object {
        const val RESULT_POLICY = "result_policy"
    }
}