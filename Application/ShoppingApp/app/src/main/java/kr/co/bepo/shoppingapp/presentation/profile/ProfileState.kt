package kr.co.bepo.shoppingapp.presentation.profile

import android.net.Uri
import kr.co.bepo.shoppingapp.data.entity.product.ProductEntity

sealed class ProfileState {

    object UnInitialized : ProfileState()

    object Loading : ProfileState()

    data class Login(
        val idToken: String
    ) : ProfileState()

    sealed class Success : ProfileState() {

        data class Registered(
            val userName: String,
            val profileImage: Uri?,
            val productList: List<ProductEntity> = listOf()
        ) : Success()

        object NotRegistered : Success()
    }

    object Error : ProfileState()
}