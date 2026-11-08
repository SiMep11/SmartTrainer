package com.popoli.smarttrainer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated (view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        val btSignUp: Button = view.findViewById(R.id.BtSignUp)
        val tvSignIn: TextView = view.findViewById(R.id.TvSignIn)
        val etUsername: EditText = view.findViewById(R.id.EtUsername)
        val etPassword: EditText = view.findViewById(R.id.EtPassword)
        val etConfirmPassword: EditText = view.findViewById(R.id.EtConfirmPass)
        val navController = view.findNavController()
        val myCoroutineScope = CoroutineScope(Dispatchers.IO)
        val dbDAO = AppDatabase.getInstance(requireContext().applicationContext).getDao()
        btSignUp.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?){
                myCoroutineScope.launch {
                    try {
                        val u = dbDAO.getAllUsernames()
                        if(etUsername.text.toString() != "" &&  u.contains(etUsername.text.toString())){
                            withContext(Dispatchers.Main) {
                                Toast.makeText(requireContext(), "Error: username already registered", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            if((etPassword.text.toString() != "" && etConfirmPassword.text.toString() != "") && etPassword.text.toString() == etConfirmPassword.text.toString()){
                                val newUser = User(username = etUsername.text.toString(), password = etPassword.text.toString())
                                dbDAO.insUser(newUser)
                                withContext(Dispatchers.Main){
                                    Toast.makeText(requireContext(), "Username registered", Toast.LENGTH_SHORT).show()
                                    navController.navigate(R.id.justRegistered)
                                }
                            }else{
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }catch(e: Exception){
                        Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
        tvSignIn.setOnClickListener( object : View.OnClickListener {
            override fun onClick (v: View?){
                navController.navigate(R.id.alreadyRegistered)
            }
        })
    }
}