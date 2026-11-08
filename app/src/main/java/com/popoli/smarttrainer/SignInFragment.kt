package com.popoli.smarttrainer

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignInFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().applicationContext.getSharedPreferences("sp", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", null)
        if(username != null){
            val navController = view.findNavController()
            val action = SignInFragmentDirections.succesfullSignIn(username)
            navController.navigate(action)
        }

        val tvSignUp: TextView = view.findViewById(R.id.TvSignIn)
        val etUsername: EditText = view.findViewById(R.id.EtUsername)
        val etPassword: EditText = view.findViewById(R.id.EtPassword)
        val btSignIn: Button = view.findViewById(R.id.BtSignUp)
        val cbRemember: CheckBox = view.findViewById(R.id.CbRemember)
        val navController = view.findNavController()
        val myCoroutineScope = CoroutineScope(Dispatchers.IO)
        val dbDAO = AppDatabase.getInstance(requireContext().applicationContext).getDao()

        tvSignUp.setOnClickListener( object : View.OnClickListener {
            override fun onClick (v: View?){
                navController.navigate(R.id.notRegistered)
            }
        })

        btSignIn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(V: View?){
                myCoroutineScope.launch(Dispatchers.IO) {
                    try {
                        val u = dbDAO.findUser(etUsername.text.toString())
                        if (u?.password == etPassword.text.toString() && u.username == etUsername.text.toString()) {
                            val username = etUsername.text.toString()
                            val action = SignInFragmentDirections.succesfullSignIn(username)
                            if(cbRemember.isChecked){
                                sharedPreferences.edit().apply {
                                    putString("username", username)
                                    apply()
                                }
                            }
                            withContext(Dispatchers.Main) {
                                navController.navigate(action)
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(requireContext(), "Error: username or password not valid", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: Exception) {
                            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
                        }
                }
            }
        })
    }
}