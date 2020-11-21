package uz.kahero.prng

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startFragment(MainFragment.newInstance())
    }


    fun startFragment(fragment: Fragment, backStack: Boolean = false) {
        val tr = supportFragmentManager.beginTransaction()
        tr.replace(R.id.container, fragment)
        if (backStack) tr.addToBackStack(fragment::class.java.name)
        tr.commit()
    }
}