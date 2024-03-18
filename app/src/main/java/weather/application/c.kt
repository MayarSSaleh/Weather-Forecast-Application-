//package weather.application
//
//import android.content.Context
//import android.net.ConnectivityManager
//import android.net.NetworkInfo
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.databinding.DataBindingUtil
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.lifecycleScope
//import androidx.work.OneTimeWorkRequestBuilder
//import androidx.work.WorkInfo
//import androidx.work.WorkManager
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//class c {
//}
//
//
//class FirstFragment : Fragment() {
//
////        if (isNetworkAvailable(context)) {
//////            var workManger = WorkManager.getInstance(requireActivity().application)
//////            val request = OneTimeWorkRequestBuilder<DownloadWorker>()
//////                .build()
//////            workManger.enqueue(request)
//////            workManger.getWorkInfoByIdLiveData(request.id).observe(viewLifecycleOwner) { workInfo ->
//////                when (workInfo.state) {
//////                    WorkInfo.State.SUCCEEDED -> {
//////                        val productListJson = workInfo.outputData.getString(MyConstants.theList)
//////                        val productListType = object : TypeToken<List<Product>>() {}.type
//////                        val productList =
//////                            Gson().fromJson<List<Product>>(productListJson, productListType)
//////                        adapter.submitList(productList)
//////                    }
//////                    else -> {
//////                        Log.d("final", "else of when")
//////                    }
////                }
////            }
////        } else {
//////            lifecycleScope.launch(Dispatchers.Main) {
//////                val res = myProdcutDao.getAll()
//////                adapter.submitList(res)
//////            }
//////        }
//////        return binding.root
////    }
////}
//    }
//fun isNetworkAvailable(context: Context?): Boolean {
//    val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//    var activeNetworkInfo: NetworkInfo?
//    activeNetworkInfo = cm.activeNetworkInfo
//    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
//}
//}