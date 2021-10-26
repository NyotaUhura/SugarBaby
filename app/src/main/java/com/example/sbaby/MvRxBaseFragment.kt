package com.example.sbaby

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyRecyclerView
import com.airbnb.mvrx.MavericksView

abstract class MvRxBaseFragment(@LayoutRes layout: Int) : MavericksView, Fragment(layout) {

    protected val recyclerView: EpoxyRecyclerView
     get()  {
         return requireView().findViewById(R.id.recycler_view)
     }
    protected val epoxyController: EpoxyController by lazy { epoxyController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        epoxyController.onRestoreInstanceState(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.setController(epoxyController)
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    abstract fun epoxyController(): EpoxyController

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        epoxyController.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        epoxyController.cancelPendingModelBuild()
        super.onDestroyView()
        recyclerView.adapter = null
    }

    override fun invalidate() {
        recyclerView.requestModelBuild()
    }
}
