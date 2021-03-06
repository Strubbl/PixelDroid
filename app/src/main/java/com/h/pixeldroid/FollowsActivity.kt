package com.h.pixeldroid

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.h.pixeldroid.api.PixelfedAPI
import com.h.pixeldroid.fragments.feeds.AccountListFragment
import com.h.pixeldroid.objects.Account
import com.h.pixeldroid.objects.Account.Companion.ACCOUNT_ID_TAG
import com.h.pixeldroid.objects.Account.Companion.FOLLOWING_TAG
import com.h.pixeldroid.utils.DBUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowsActivity : AppCompatActivity() {
    private var followsFragment = AccountListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_followers)

        // Get account id
        val id = intent.getSerializableExtra(ACCOUNT_ID_TAG) as String?
        val following = intent.getSerializableExtra(FOLLOWING_TAG) as Boolean

        if(id == null) {
            val db = DBUtils.initDB(applicationContext)

            val user = db.userDao().getActiveUser()

            val domain = user?.instance_uri.orEmpty()
            val accessToken = user?.accessToken.orEmpty()
            db.close()

            val pixelfedAPI = PixelfedAPI.create(domain)

            pixelfedAPI.verifyCredentials("Bearer $accessToken").enqueue(object :
                Callback<Account> {
                override fun onFailure(call: Call<Account>, t: Throwable) {
                    Log.e("Cannot get account id", t.toString())
                }

                override fun onResponse(call: Call<Account>, response: Response<Account>) {
                    if(response.code() == 200) {
                        val id = response.body()!!.id
                        launchActivity(id, following)
                    }
                }
            })
        } else {
            launchActivity(id, following)
        }
    }

    private fun launchActivity(id : String, following : Boolean) {val arguments = Bundle()
        arguments.putSerializable(ACCOUNT_ID_TAG, id)
        arguments.putSerializable(FOLLOWING_TAG, following)
        followsFragment.arguments = arguments

        supportFragmentManager.beginTransaction()
            .add(R.id.followsFragment, followsFragment).commit()

    }
}
