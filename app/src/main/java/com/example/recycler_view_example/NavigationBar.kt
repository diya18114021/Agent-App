package com.example.recycler_view_example

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.recycler_view_example.payment_gateway_helpers.CoreHelper
import com.razorpay.PaymentResultListener

class NavigationBar : AppCompatActivity(), PaymentResultListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_bar)

        val bottomNavigation = findViewById<MeowBottomNavigation>(R.id.bottomNavigation)

        addFragment(HomeFragment.newInstance())
        bottomNavigation.show(0)
        bottomNavigation.add(MeowBottomNavigation.Model(0,R.drawable.ic_baseline_home_24))
        bottomNavigation.add(MeowBottomNavigation.Model(1,R.drawable.ic_baseline_shopping_bag_24))
        bottomNavigation.add(MeowBottomNavigation.Model(2,R.drawable.ic_baseline_shopping_cart_24))
        bottomNavigation.add(MeowBottomNavigation.Model(3,R.drawable.ic_baseline_person_24))

        bottomNavigation.setOnClickMenuListener {
            when(it.id){
                0 -> {
                    replaceFragment(HomeFragment.newInstance())
                }
                1 -> {
                    replaceFragment(CatalogueFragment.newInstance())
                }
                2 -> {
                    replaceFragment(CartFragment.newInstance())
                }
                3 -> {
                    replaceFragment(ProfileFragment.newInstance())
                }
                else -> {
                    replaceFragment(HomeFragment.newInstance())
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.fragmentContainer,fragment).addToBackStack(Fragment::class.java.simpleName).commit()
    }

    private fun addFragment(fragment: Fragment){
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.add(R.id.fragmentContainer,fragment).addToBackStack(Fragment::class.java.simpleName).commit()
    }

    override fun onPaymentSuccess(p0: String?) {
        CoreHelper.showAlertDialog(
            this,
            "Success",
            "Payment successful.\n$p0",
            { dialog: DialogInterface, _ -> dialog.dismiss() })
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        CoreHelper.showAlertDialog(
            this,
            "Failed",
            "Payment failed.\n$p1",
            { dialog, _: Int -> dialog.dismiss() })
    }
}