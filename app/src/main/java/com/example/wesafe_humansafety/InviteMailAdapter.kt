package com.example.wesafe_humansafety

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wesafe_humansafety.databinding.ItemInviteMailBinding

class InviteMailAdapter(
    private val listInvites: List<String>,
    private val onActionClick: OnActionClick
):
    RecyclerView.Adapter<InviteMailAdapter.ViewHolder>() {

        class ViewHolder(val binding: ItemInviteMailBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InviteMailAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val item= ItemInviteMailBinding.inflate(inflater, parent, false)
        return ViewHolder(item)
    }


    override fun onBindViewHolder(holder: InviteMailAdapter.ViewHolder, position: Int) {
        val item = listInvites[position]
        holder.binding.mail.text = item

        holder.binding.accept.setOnClickListener {
            onActionClick.onAcceptClick(item)

        }

        holder.binding.deny.setOnClickListener {
            onActionClick.onDenyClick(item)

        }
    }

    override fun getItemCount(): Int {
        return listInvites.size
    }


    interface OnActionClick{
        fun onAcceptClick(mail:String)
        fun onDenyClick(mail: String)
    }

}