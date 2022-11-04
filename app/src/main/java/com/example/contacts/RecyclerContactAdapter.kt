package com.example.contacts

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_item.view.*


class RecyclerContactAdapter(private val context: Context, private val arrContacts: ArrayList<ModelContact>) :
    RecyclerView.Adapter<RecyclerContactAdapter.ViewHolder>() {
    private var arrExpandValue = ArrayList<Boolean>()
    var hiddentxt = ""

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

//        obtaining all the required entities from the itemView
        val contactname = itemView.contactname!!
        val contactno = itemView.expandablecontactno!!
        val contactimg = itemView.contactimg!!
        val expandablebtn = itemView.expandablebuttons!!
        val hr = itemView.hr!!
        val info = itemView.expandablebuttons.info!!
        val hiddentext = itemView.positiontxt!!
        val phone = itemView.phone!!
        val sms = itemView.sms!!
        val delete = itemView.delete!!


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        binding the views of the holder
        holder.contactname.text = arrContacts[position].name
        holder.contactno.text = arrContacts[position].no
        holder.contactimg.setImageResource(arrContacts[position].img)

        //updating the layout of expanded itemView and that of collapsed
        if (arrContacts[position].expand) {

            holder.expandablebtn.visibility = View.VISIBLE
            holder.delete.visibility = View.VISIBLE
            holder.hr.margin(top = 100F)

        } else if (!arrContacts[position].expand) {

            holder.expandablebtn.visibility = View.GONE
            holder.delete.visibility = View.GONE
            holder.hr.margin(top = 50F)
        }

        //on info btn click
        holder.info.setOnClickListener {
            val i = Intent(context, ContactDetailsActivity::class.java)
            i.putExtra("name", holder.contactname.text)
            i.putExtra("no", holder.contactno.text)
            i.putExtra("img", arrContacts[position].img.toString())

            context.startActivity(i)
        }

//        on phone btn click
        holder.phone.setOnClickListener {

            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:" + arrContacts[position].no)
            context.startActivity(dialIntent)

        }

//        on sms btn click
        holder.sms.setOnClickListener {
            val sendIntent = Intent(Intent.ACTION_SENDTO)
            sendIntent.data = Uri.parse("smsto:" + Uri.encode(arrContacts[position].no))
            context.startActivity(sendIntent)
        }

        //on contact clicked
        holder.contactname.setOnClickListener {

            //set the position of the contact in 'gone' visibility textview so that we can retrieve the position of the contact in mainactivity
            holder.hiddentext.text = position.toString()
            hiddentxt = holder.hiddentext.text.toString()

            //creating an araylist of the values of expand parameter of each contact item
            arrContacts.forEach {
                arrExpandValue.add(it.expand)
            }

            //creating conditions so that the clicked contact expands and collapses and if the item expands then no other item should be expanded
            if (arrExpandValue.all { !it }) {
                arrContacts[position].expand = true
                notifyItemChanged(position)

            } else if (!arrExpandValue.all { !it } && arrContacts[position].expand) {
                arrContacts[position].expand = false
                notifyItemChanged(position)
            } else {
                arrContacts.forEach {
                    it.expand = false

                }
                notifyDataSetChanged()
                arrContacts[position].expand = true
                notifyItemChanged(position)
            }


        }


    }

    override fun getItemCount(): Int {
        return arrContacts.size
    }


    //below code is for setting the margin which we used when setting margin top of hr while updating layout of expanded and collapsed contact item
    private fun View.margin(
        left: Float? = null,
        top: Float? = null,
        right: Float? = null,
        bottom: Float? = null
    ) {
        layoutParams<ViewGroup.MarginLayoutParams> {
            left?.run { leftMargin = dpToPx(this) }
            top?.run { topMargin = dpToPx(this) }
            right?.run { rightMargin = dpToPx(this) }
            bottom?.run { bottomMargin = dpToPx(this) }
        }
    }

    private inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) {
        if (layoutParams is T) block(layoutParams as T)
    }

    private fun View.dpToPx(dp: Float): Int = context.dpToPx(dp)
    private fun Context.dpToPx(dp: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()

}