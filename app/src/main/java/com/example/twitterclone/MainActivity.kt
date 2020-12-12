package com.example.twitterclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.twitterclone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadFragment(HomeFragment())
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
//         Do Some Work
                    loadFragment(HomeFragment())
                    true
                }
                R.id.search-> {
                    loadFragment(SearchFragment())
                    true
                }
                else->{
                    loadFragment(ProfileFragment())
                    true
                }

            }
        }

    }
    private fun loadFragment(fragment:Fragment){
        if(fragment!=null){
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,fragment).commit()
        }
    }
}