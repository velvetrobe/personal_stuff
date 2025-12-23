package com.example.personal_stuff.api

import com.example.personal_stuff.models.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class ApiClient {
    companion object {
        private const val BASE_URL = "http://10.0.2.2:5079/api/" //POOOORT
        private val client = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }).build()
    }

    fun getAllProducts(callback: (List<Product>?, Throwable?) -> Unit) {
        val request = Request.Builder()
            .url(BASE_URL + "Products/GetAllProducts")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null, e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (response.isSuccessful) {
                        val body = response.body?.string()
                        try {
                            val jsonArray = JSONArray(body)
                            val products = mutableListOf<Product>()
                            for (i in 0 until jsonArray.length()) {
                                val jsonObject = jsonArray.getJSONObject(i)
                                val product = Product(
                                    id = jsonObject.getInt("id"),
                                    name = jsonObject.getString("name"),
                                    description = jsonObject.getString("description"),
                                    category = jsonObject.getString("category"),
                                    price = jsonObject.getDouble("price"),
                                    imageUrl = jsonObject.getString("imageUrl")
                                )
                                products.add(product)
                            }
                            callback(products, null)
                        } catch (e: Exception) {
                            callback(null, e)
                        }
                    } else {
                        callback(null, IOException("Error: ${response.code}"))
                    }
                }
            }
        })
    }

    fun getProductById(productId: Int, callback: (Product?, Throwable?) -> Unit) {
        val request = Request.Builder()
            .url(BASE_URL + "Products/GetProduct/$productId")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null, e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (response.isSuccessful) {
                        val body = response.body?.string()
                        try {
                            val jsonObject = JSONObject(body)
                            val product = Product(
                                id = jsonObject.getInt("id"),
                                name = jsonObject.getString("name"),
                                description = jsonObject.getString("description"),
                                category = jsonObject.getString("category"),
                                price = jsonObject.getDouble("price"),
                                imageUrl = jsonObject.getString("imageUrl")
                            )
                            callback(product, null)
                        } catch (e: Exception) {
                            callback(null, e)
                        }
                    } else {
                        callback(null, IOException("Error: ${response.code}"))
                    }
                }
            }
        })
    }

    fun login(email: String, password: String, callback: (LoginResponse?, Throwable?) -> Unit) {
        val loginRequest = JSONObject().apply {
            put("email", email)
            put("password", password)
        }
        val body = loginRequest.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(BASE_URL + "Auth/Login")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null, e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (response.isSuccessful) {
                        val body = response.body?.string()
                        try {
                            val jsonObject = JSONObject(body)
                            val success = jsonObject.getBoolean("success")
                            val message = if (jsonObject.isNull("message")) null else jsonObject.getString("message")
                            val userObj = if (jsonObject.isNull("user")) null else jsonObject.getJSONObject("user")
                            val user = if (userObj != null) {
                                User(
                                    id = userObj.getInt("id"),
                                    name = userObj.getString("name"),
                                    email = userObj.getString("email"),
                                    birthDate = userObj.getString("birthDate")
                                )
                            } else null

                            val loginResponse = LoginResponse(success, message, user)
                            callback(loginResponse, null)
                        } catch (e: Exception) {
                            callback(null, e)
                        }
                    } else {
                        callback(null, IOException("Error: ${response.code}"))
                    }
                }
            }
        })
    }

    fun register(user: User, password: String, callback: (LoginResponse?, Throwable?) -> Unit) {
        val userWithPassword = JSONObject().apply {
            put("name", user.name)
            put("email", user.email)
            put("birthDate", user.birthDate)
            put("password", password)
        }
        val body = userWithPassword.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(BASE_URL + "Auth/Register")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null, e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (response.isSuccessful) {
                        val body = response.body?.string()
                        try {
                            val jsonObject = JSONObject(body)
                            val success = jsonObject.getBoolean("success")
                            val message = if (jsonObject.isNull("message")) null else jsonObject.getString("message")
                            val userObj = if (jsonObject.isNull("user")) null else jsonObject.getJSONObject("user")
                            val newUser = if (userObj != null) {
                                User(
                                    id = userObj.getInt("id"),
                                    name = userObj.getString("name"),
                                    email = userObj.getString("email"),
                                    birthDate = userObj.getString("birthDate")
                                )
                            } else null

                            val registerResponse = LoginResponse(success, message, newUser)
                            callback(registerResponse, null)
                        } catch (e: Exception) {
                            callback(null, e)
                        }
                    } else {
                        callback(null, IOException("Error: ${response.code}"))
                    }
                }
            }
        })
    }

    fun getCart(userId: Int, callback: (List<CartItem>?, Throwable?) -> Unit) {
        val request = Request.Builder()
            .url(BASE_URL + "Cart/GetCart/$userId")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null, e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (response.isSuccessful) {
                        val body = response.body?.string()
                        try {
                            val jsonObject = JSONObject(body)
                            val itemsArray = jsonObject.getJSONArray("items")
                            val itemsList = mutableListOf<CartItem>()
                            for (i in 0 until itemsArray.length()) {
                                val itemObj = itemsArray.getJSONObject(i)
                                val cartItem = CartItem(
                                    productId = itemObj.getInt("productId"),
                                    quantity = itemObj.getInt("quantity")
                                )
                                itemsList.add(cartItem)
                            }
                            callback(itemsList, null)
                        } catch (e: Exception) {
                            callback(null, e)
                        }
                    } else {
                        callback(null, IOException("Error: ${response.code}"))
                    }
                }
            }
        })
    }

    fun addToCart(userId: Int, productId: Int, quantity: Int, callback: (String?, Throwable?) -> Unit) {
        val request = Request.Builder()
            .url(BASE_URL + "Cart/AddToCart/$userId/$productId/$quantity")
            .post(RequestBody.create(null, "")) // Empty body for POST
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null, e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (response.isSuccessful) {
                        val body = response.body?.string()
                        callback(body, null)
                    } else {
                        callback(null, IOException("Error: ${response.code}"))
                    }
                }
            }
        })
    }

    fun updateQuantity(userId: Int, productId: Int, newQuantity: Int, callback: (String?, Throwable?) -> Unit) {
        val request = Request.Builder()
            .url(BASE_URL + "Cart/UpdateQuantity/$userId/$productId/$newQuantity")
            .put(RequestBody.create(null, ""))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null, e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (response.isSuccessful) {
                        val body = response.body?.string()
                        // Assuming API returns a simple message or the updated cart
                        callback(body, null)
                    } else {
                        callback(null, IOException("Error: ${response.code}"))
                    }
                }
            }
        })
    }

    fun removeFromCart(userId: Int, productId: Int, callback: (String?, Throwable?) -> Unit) {
        val request = Request.Builder()
            .url(BASE_URL + "Cart/RemoveFromCart/$userId/$productId")
            .delete()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null, e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (response.isSuccessful) {
                        val body = response.body?.string()
                        callback(body, null)
                    } else {
                        callback(null, IOException("Error: ${response.code}"))
                    }
                }
            }
        })
    }

    fun checkout(userId: Int, callback: (CheckoutResponse?, Throwable?) -> Unit) {
        val request = Request.Builder()
            .url(BASE_URL + "Orders/Checkout/$userId")
            .post(RequestBody.create(null, "")) // Empty body for POST
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null, e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (response.isSuccessful) {
                        val body = response.body?.string()
                        try {
                            val jsonObject = JSONObject(body)
                            val success = jsonObject.getBoolean("success")
                            val message = if (jsonObject.isNull("message")) null else jsonObject.getString("message")
                            val orderId = if (jsonObject.isNull("orderId")) null else jsonObject.getInt("orderId")

                            val checkoutResponse = CheckoutResponse(success, message, orderId)
                            callback(checkoutResponse, null)
                        } catch (e: Exception) {
                            callback(null, e)
                        }
                    } else {
                        callback(null, IOException("Error: ${response.code}"))
                    }
                }
            }
        })
    }
}