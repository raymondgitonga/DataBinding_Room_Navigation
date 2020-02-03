package com.tosh.roomnav.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.tosh.roomnav.R
import com.tosh.roomnav.room.Note
import kotlinx.android.synthetic.main.item_note.view.*

class NotesAdapter(val notes: List<Note>): RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.view.titleTv.text = notes[position].title
        holder.view.bodyTv.text = notes[position].body
        holder.view.setOnClickListener {
            val action = HomeFragmentDirections.actionAddNote()
            action.note = notes[position]

            Navigation.findNavController(it).navigate(action)
        }
    }

    class NoteViewHolder(val view: View): RecyclerView.ViewHolder(view)
}