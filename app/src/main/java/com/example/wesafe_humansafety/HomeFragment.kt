package com.example.wesafe_humansafety

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private val listContacts: ArrayList<ContactModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("FetchContact89", "onViewCreated: ")
        val listMembers = listOf<MemberModel>(
            MemberModel("Bheetika Gahlot",
                address = "43/1 @nd floor kapda mill,panchwati,Ghaziabad",
                battery = "90%",
                distance = "220"
            ),
            MemberModel("Ansh Gahlot",
                address = "43/1 @nd floor kapda mill,panchwati,Ghaziabad",
                battery = "80%",
                distance = "210"
            ),
            MemberModel("Shivang Gahlot",
                address = "43/1 @nd floor kapda mill,panchwati,Ghaziabad",
                battery = "79%",
                distance = "200"
            ),

        )
        val adapter = MemberAdapter(listMembers)

        val recycler = requireView().findViewById<RecyclerView>(R.id.recycler_member)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter



        Log.d("FetchContact89", "fetchContacts: Start")
        Log.d("FetchContact89", "fetchContacts: Start ${listContacts.size}")
        val inviteAdapter = InviteAdapter(listContacts)
        Log.d("FetchContact89", "fetchContacts: end")

        CoroutineScope(Dispatchers.IO).launch{
            Log.d("FetchContact89", "fetchContacts: Coroutine start hone wala hai")
            listContacts.addAll(fetchContacts())
            insertDatabaseContacts(listContacts)
            withContext(Dispatchers.Main){
                inviteAdapter.notifyDataSetChanged()
            }
            Log.d("FetchContact89", "fetchContacts: Coroutine end ${listContacts.size}")
        }


        val inviteRecycler = requireView().findViewById<RecyclerView>(R.id.recycler_invite)
        inviteRecycler.layoutManager =
            LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        inviteRecycler.adapter = inviteAdapter

    }

    @SuppressLint("SuspiciousIndentation")
    private suspend fun insertDatabaseContacts(listContacts: ArrayList<ContactModel>) {
        val database = MyFamilyDatabase.getDatabase((requireContext()))


            database.contactDao().insertAll(listContacts)

    }

    @SuppressLint("Range")
    private fun fetchContacts(): ArrayList<ContactModel> {

        Log.d("FetchContacts", "fetchContacts: Start ho gya hai")
        val cr = requireActivity().contentResolver
        val cursor = cr.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null)

        val listContacts: ArrayList<ContactModel> = ArrayList()
        if(cursor!=null && cursor.count>0){

            while(cursor!=null && cursor.moveToNext()){
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val hasPhoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                if(hasPhoneNumber > 0.toString()){

                    val pCur = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=?",
                        arrayOf(id),
                        ""
                        )
                    if(pCur != null && pCur.count>0){
                        while(pCur!=null && pCur.moveToNext()){

                            val phoneNum =
                                pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            listContacts.add(ContactModel(name,phoneNum))
                        }
                        pCur.close()
                    }
                }
            }
            if(cursor!=null){
                cursor.close()
            }

        }
        Log.d("FetchContacts", "fetchContacts: End ho gya hai")
        return listContacts

    }

    companion object {

        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}