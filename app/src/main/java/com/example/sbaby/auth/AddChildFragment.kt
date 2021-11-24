package com.example.sbaby.auth

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.sbaby.*
import com.example.sbaby.databinding.FragmentAddChildBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.getKoin

class AddChildFragment : Fragment(R.layout.fragment_add_child) {

    private val binding: FragmentAddChildBinding by viewBinding()
    private val firebaseDataSource: FirebaseDataSource by getKoin().inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            finishButton.setOnClickListener {
                addChild()
            }
        }
    }

    private fun addChild() {
        val name = binding.nameEditText.text.toString()
        if (name != "") {
            var id = ""
            lifecycleScope.launchWhenCreated {
                val currUser = firebaseDataSource.getUser(true) as Parent
                val res = firebaseDataSource.createChild(name, currUser.familyId)
                when (res) {
                    is Result.Success -> {
                        id = res.data
                        val child = firebaseDataSource.getUserDoc(id)
                        when (child) {
                            is ChildFirebaseModel -> {
                                firebaseDataSource.addChildToParent(child.mapToChildModel(), currUser)
                                Snackbar.make(requireView(), getString(R.string.login_in), 2000).show()
                                (activity as AuthActivity).finishAuth(true)
                            }
                        }
                    }
                }
            }
        } else Toast.makeText(context, "Enter name", Toast.LENGTH_SHORT)
    }
}