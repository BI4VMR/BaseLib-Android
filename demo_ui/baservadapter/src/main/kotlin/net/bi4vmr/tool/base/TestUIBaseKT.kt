package net.bi4vmr.tool.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import net.bi4vmr.tool.android.ui.baservadapter.base.ListItem
import net.bi4vmr.tool.databinding.TestuiViewBinding

class TestUIBaseKT : AppCompatActivity() {

    companion object {
        private val TAG: String = "TestApp-${TestUIBaseKT::class.java.simpleName}"
    }

    private val binding: TestuiViewBinding by lazy {
        TestuiViewBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            recyclerview.layoutManager = LinearLayoutManager(this@TestUIBaseKT)
            val adapter = MyAdapter()
            recyclerview.adapter = adapter

            adapter.submit(makeTestData())
        }
    }

    private fun makeTestData(): List<ListItem> {
        return listOf(
            Type1VO("表项一", "这是类型一"),
            Type1VO("表项二", "这是类型一"),
            Type2VO("表项三", "这是类型二"),
            Type2VO("表项四", "这是类型二"),
            Type1VO("表项五", "这是类型一"),
        )
    }
}
